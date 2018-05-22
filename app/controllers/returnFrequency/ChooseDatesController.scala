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
import forms.chooseDatesForm.datesForm
import javax.inject.{Inject, Singleton}
import models.returnFrequency.{Jan, ReturnDatesModel}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.CustomerDetailsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class ChooseDatesController @Inject()(val messagesApi: MessagesApi,
                                      val authenticate: AuthPredicate,
                                      val customerDetailsService: CustomerDetailsService,
                                      val serviceErrorHandler: ServiceErrorHandler,
                                      implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async { implicit user =>

    customerDetailsService.getCustomerDetails(user.vrn) map {
      case Right(_) =>
        val frequency = Jan // this will eventually come from the call to getCustomerDetails

        val form: Form[ReturnDatesModel] = if (user.session.data.contains(SessionKeys.RETURN_FREQUENCY)) {
          user.session(SessionKeys.RETURN_FREQUENCY) match {
            case value if value.nonEmpty => datesForm.fill(ReturnDatesModel(value))
            case _ => datesForm
          }
        }
        else {
          datesForm
        }

        Ok(views.html.returnFrequency.chooseDates(form, frequency))

      case _ => serviceErrorHandler.showInternalServerError
    }
  }

  val submit: Action[AnyContent] = authenticate.async { implicit user =>

    datesForm.bindFromRequest().fold(
      errors => {
        customerDetailsService.getCustomerDetails(user.vrn) map {
          case Right(_) =>
            val frequency = Jan // this will eventually come from the call to getCustomerDetails
            BadRequest(views.html.returnFrequency.chooseDates(errors, frequency))
          case _ => serviceErrorHandler.showInternalServerError
        }
      },
      success => {
        Future.successful(
          Redirect(controllers.returnFrequency.routes.ConfirmVatDatesController.show())
            .withSession(user.session + (SessionKeys.RETURN_FREQUENCY -> success.current))
        )
      }
    )
  }

}
