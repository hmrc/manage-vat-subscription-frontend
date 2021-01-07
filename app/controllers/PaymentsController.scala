/*
 * Copyright 2021 HM Revenue & Customs
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

import audit.AuditService
import audit.models.BankAccountHandOffAuditModel
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.{AuthPredicate, InFlightRepaymentBankAccountPredicate}
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CustomerCircumstanceDetailsService, PaymentsService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsController @Inject()(val authenticate: AuthPredicate,
                                   val serviceErrorHandler: ServiceErrorHandler,
                                   val paymentsService: PaymentsService,
                                   val auditService: AuditService,
                                   val subscriptionService: CustomerCircumstanceDetailsService,
                                   val inFlightRepaymentBankAccountPredicate: InFlightRepaymentBankAccountPredicate,
                                   val mcc: MessagesControllerComponents,
                                   implicit val config: AppConfig,
                                   implicit val ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport {

  val sendToPayments: Action[AnyContent] = (authenticate andThen inFlightRepaymentBankAccountPredicate).async { implicit user =>
    subscriptionService.getCustomerCircumstanceDetails(user.vrn).flatMap {
      case Right(circumstanceDetails) =>
        paymentsService.postPaymentDetails(user, circumstanceDetails.partyType, circumstanceDetails.customerDetails.welshIndicator) map {
          case Right(response) =>
            auditService.extendedAudit(
              BankAccountHandOffAuditModel(user, response.nextUrl),
              Some(routes.PaymentsController.sendToPayments().url)
            )
            Redirect(response.nextUrl)
          case _ =>
            Logger.debug("[PaymentsController][callback] Error returned from PaymentsService, Rendering ISE.")
            serviceErrorHandler.showInternalServerError
        }
      case Left(_) => Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }
}
