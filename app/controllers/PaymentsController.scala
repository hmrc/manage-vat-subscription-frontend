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

package controllers

import config.{FrontendAppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import javax.inject.{Inject, Singleton}
import models.payments.PaymentStartModel
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.PaymentsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

@Singleton
class PaymentsController @Inject()(val messagesApi: MessagesApi,
                                   val authenticate: AuthPredicate,
                                   val serviceErrorHandler: ServiceErrorHandler,
                                   val paymentsService: PaymentsService,
                                   val config: FrontendAppConfig) extends FrontendController with I18nSupport {

  private[controllers] def paymentDetails(vrn: String, isAgent: Boolean): PaymentStartModel =
    PaymentStartModel(vrn, isAgent,
      config.signInContinueBaseUrl + controllers.routes.CustomerCircumstanceDetailsController.show(),
      config.signInContinueBaseUrl + controllers.routes.CustomerCircumstanceDetailsController.show(),
      convenienceUrl(isAgent))

  private[PaymentsController] def convenienceUrl(isAgent: Boolean) = {
    if(isAgent) {
      config.signInContinueBaseUrl + controllers.agentClientRelationship.routes.SelectClientVrnController.show()
    } else {
      config.signInContinueBaseUrl + controllers.routes.CustomerCircumstanceDetailsController.show()
    }
  }

  val sendToPayments: Action[AnyContent] = authenticate.async { implicit user =>
    paymentsService.postPaymentDetails(paymentDetails(user.vrn, user.isAgent)) map {
      case Right(response) => Redirect(response.nextUrl)
      case _ =>
        Logger.debug(s"[PaymentsController][callback] Error returned from PaymentsService, Rendering ISE.")
        serviceErrorHandler.showInternalServerError
    }
  }

}
