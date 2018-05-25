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

package controllers.returnFrequency

import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import javax.inject.{Inject, Singleton}
import models.returnFrequency._
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.{CustomerDetailsService, ReturnFrequencyService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class ConfirmVatDatesController @Inject()(val messagesApi: MessagesApi,
                                          val authenticate: AuthPredicate,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          customerDetailsService: CustomerDetailsService,
                                          returnFrequencyService: ReturnFrequencyService,
                                          implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async { implicit user =>

    getReturnFrequency() match {
      case Some(frequency) => Future.successful(Ok(views.html.returnFrequency.confirm_dates(frequency)))
      case None => Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }

  val submit: Action[AnyContent] = authenticate.async { implicit user =>
    getReturnFrequency() match {
      case Some(frequency) =>
        returnFrequencyService.updateReturnFrequency(user.vrn, frequency).map {
          case Right(_) => Redirect(controllers.returnFrequency.routes.DatesReceivedController.show())
            .withSession(user.session - SessionKeys.RETURN_FREQUENCY)
          case _ => serviceErrorHandler.showInternalServerError
        }
      case None => Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }

  private def getReturnFrequency()(implicit request: RequestHeader): Option[ReturnDateOption] = {
    request.session(SessionKeys.RETURN_FREQUENCY) match {
      case Jan.id => Some(Jan)
      case Feb.id => Some(Feb)
      case Mar.id => Some(Mar)
      case Monthly.id => Some(Monthly)
      case unknown =>
        Logger.warn(s"[ConfirmVatDatesController].[getReturnFrequency] Session contains invalid frequency: $unknown")
        None
    }
  }
}