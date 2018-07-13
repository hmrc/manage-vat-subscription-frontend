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

package controllers.agentClientRelationship

import javax.inject.{Inject, Singleton}

import audit.AuditService
import audit.models.{AuthenticateAgentModel, GetClientBusinessNameModel}
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthoriseAsAgentWithClient
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc._
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class ConfirmClientVrnController @Inject()(val messagesApi: MessagesApi,
                                           val authenticate: AuthoriseAsAgentWithClient,
                                           val customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                           val serviceErrorHandler: ServiceErrorHandler,
                                           val auditService: AuditService,
                                           implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  def show: Action[AnyContent] = authenticate.async {
    implicit user =>
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
        case Right(circumstances) =>
          auditService.extendedAudit(
            GetClientBusinessNameModel(user.isAgent, user.arn, user.vrn, circumstances.customerDetails.tradingName.get),
            Some(controllers.agentClientRelationship.routes.ConfirmClientVrnController.show().url)
          )
          Ok(views.html.agentClientRelationship.confirm_client_vrn(user.vrn, circumstances.customerDetails))
        case _ => serviceErrorHandler.showInternalServerError
      }
  }

  def changeClient: Action[AnyContent] = authenticate.async {
    implicit user =>
      Future.successful(
        Redirect(controllers.agentClientRelationship.routes.SelectClientVrnController.show())
          .removingFromSession(SessionKeys.CLIENT_VRN, SessionKeys.RETURN_FREQUENCY)
      )
  }

}
