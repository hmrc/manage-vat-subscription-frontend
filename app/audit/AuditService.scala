/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package audit

import audit.models.ExtendedAuditModel
import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.http.HeaderNames
import play.api.libs.json.{JsObject, JsValue, Json, Writes}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.AuditExtensions
import uk.gov.hmrc.play.audit.http.connector.AuditResult
import uk.gov.hmrc.play.audit.http.connector.AuditResult.{Disabled, Failure, Success}
import uk.gov.hmrc.play.audit.model.ExtendedDataEvent

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuditService @Inject()(appConfig: FrontendAppConfig, auditConnector: FrontendAuditConnector){

  implicit val extendedDataEventWrites: Writes[ExtendedDataEvent] = Json.writes[ExtendedDataEvent]

  val referrer: HeaderCarrier => String = _.headers.find(_._1 == HeaderNames.REFERER).map(_._2).getOrElse("-")

  def extendedAudit(auditModel: ExtendedAuditModel, path: Option[String] = None)(implicit hc: HeaderCarrier, ec: ExecutionContext): Unit = {
    val extendedDataEvent = toExtendedDataEvent(appConfig.appName, auditModel, path.fold(referrer(hc))(x => x))
    Logger.debug(s"Splunk Audit Event:\n\n${Json.toJson(extendedDataEvent)}")
    handleAuditResult(auditConnector.sendExtendedEvent(extendedDataEvent))
  }

  def toExtendedDataEvent(appName: String, auditModel: ExtendedAuditModel, path: String)(implicit hc: HeaderCarrier): ExtendedDataEvent = {

    val details: JsValue =
      Json.toJson(AuditExtensions.auditHeaderCarrier(hc).toAuditDetails()).as[JsObject].deepMerge(auditModel.detail.as[JsObject])

    ExtendedDataEvent(
      auditSource = appName,
      auditType = auditModel.auditType,
      tags = AuditExtensions.auditHeaderCarrier(hc).toAuditTags(auditModel.transactionName, path),
      detail = details
    )
  }

  private def handleAuditResult(auditResult: Future[AuditResult])(implicit ec: ExecutionContext): Unit = auditResult.map {
    //$COVERAGE-OFF$ Disabling scoverage as returns Unit, only used for Debug messages
    case Success =>
      Logger.debug("Splunk Audit Successful")
    case Failure(err, _) =>
      Logger.debug(s"Splunk Audit Error, message: $err")
    case Disabled =>
      Logger.debug(s"Auditing Disabled")
    //$COVERAGE-ON$
  }

}
