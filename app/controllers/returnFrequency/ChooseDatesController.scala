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
import models.returnFrequency.ReturnDatesModel
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class ChooseDatesController @Inject()(val messagesApi: MessagesApi,
                                      val authenticate: AuthPredicate,
                                      val customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                      val serviceErrorHandler: ServiceErrorHandler,
                                      implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async { implicit user =>

    customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
      case Right(details) if details.returnPeriod.isDefined =>
        val form: Form[ReturnDatesModel] = user.session.get(SessionKeys.NEW_RETURN_FREQUENCY) match {
          case Some(value) => datesForm.fill(ReturnDatesModel(value))
          case _ => datesForm
        }
        Ok(views.html.returnFrequency.chooseDates(form, details.returnPeriod.get))
      case _ => serviceErrorHandler.showInternalServerError
    }
  }

  val submit: Action[AnyContent] = authenticate.async {
    implicit user => {
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
        case Right(details) if details.returnPeriod.isDefined => {
          datesForm.bindFromRequest().fold(
            errors =>
              BadRequest(views.html.returnFrequency.chooseDates(errors, details.returnPeriod.get)),
            success =>
              Redirect(controllers.returnFrequency.routes.ConfirmVatDatesController.show())
                .addingToSession(
                  SessionKeys.NEW_RETURN_FREQUENCY -> success.current,
                  SessionKeys.CURRENT_RETURN_FREQUENCY -> details.returnPeriod.get.id
                )
          )
        }
        case _ => serviceErrorHandler.showInternalServerError
      }
    }
  }
}
