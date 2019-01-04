/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers.returnFrequency

import audit.AuditService
import audit.models.UpdateReturnFrequencyAuditModel
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import javax.inject.{Inject, Singleton}
import models.returnFrequency._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.ReturnFrequencyService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class ConfirmVatDatesController @Inject()(val authenticate: AuthPredicate,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          returnFrequencyService: ReturnFrequencyService,
                                          val auditService: AuditService,
                                          implicit val appConfig: AppConfig,
                                          implicit val messagesApi: MessagesApi) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async { implicit user =>
    ReturnPeriod(user.session(SessionKeys.NEW_RETURN_FREQUENCY)) match {
      case Some(newFrequency) => Future.successful(Ok(views.html.returnFrequency.confirm_dates(newFrequency)))
      case None => Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }

  val submit: Action[AnyContent] = authenticate.async { implicit user =>
    (ReturnPeriod(user.session(SessionKeys.CURRENT_RETURN_FREQUENCY)), ReturnPeriod(user.session(SessionKeys.NEW_RETURN_FREQUENCY))) match {
      case (Some(currentFrequency), Some(newFrequency)) =>
        auditService.extendedAudit(
          UpdateReturnFrequencyAuditModel(user, currentFrequency, newFrequency),
          Some(controllers.returnFrequency.routes.ConfirmVatDatesController.submit().url)
        )
        returnFrequencyService.updateReturnFrequency(user.vrn, newFrequency).map {
          case Right(success) => {
            Redirect(controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show(if(user.isAgent) "agent" else "non-agent"))
              .removingFromSession(SessionKeys.NEW_RETURN_FREQUENCY, SessionKeys.CURRENT_RETURN_FREQUENCY)
          }
          case _ => serviceErrorHandler.showInternalServerError
        }
      case _ => Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }
}
