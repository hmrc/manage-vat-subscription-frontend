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

package controllers

import audit.AuditService
import audit.models.BankAccountHandOffAuditModel
import config.{AppConfig, ServiceErrorHandler}
import connectors.SubscriptionConnector
import controllers.predicates.AuthPredicate
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.PaymentsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class PaymentsController @Inject()(val messagesApi: MessagesApi,
                                   val authenticate: AuthPredicate,
                                   val serviceErrorHandler: ServiceErrorHandler,
                                   val paymentsService: PaymentsService,
                                   val auditService: AuditService,
                                   val subscriptionConnector: SubscriptionConnector,
                                   implicit val config: AppConfig) extends FrontendController with I18nSupport {

  val sendToPayments: Action[AnyContent] = authenticate.async { implicit user =>
    subscriptionConnector.getCustomerCircumstanceDetails(user.vrn).flatMap {
      case Right(circumstanceDetails) =>
        paymentsService.postPaymentDetails(user, circumstanceDetails) map {
          case Right(response) =>
            auditService.extendedAudit(
              BankAccountHandOffAuditModel(user, response.nextUrl),
              Some(routes.PaymentsController.sendToPayments().url)
            )
            Redirect(response.nextUrl)
          case _ =>
            Logger.debug(s"[PaymentsController][callback] Error returned from PaymentsService, Rendering ISE.")
            serviceErrorHandler.showInternalServerError
        }
      case Left(error) =>
        Logger(getClass.getSimpleName).warn(
          s"[PaymentsService][postPaymentDetails] Error retrieving Customer Circumstance Details with status ${error.status} - ${error.message}"
        )
        Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }
}
