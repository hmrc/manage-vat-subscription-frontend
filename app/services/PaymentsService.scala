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

package services

import config.AppConfig
import connectors.httpParsers.ResponseHttpParser.HttpGetResult
import connectors.{PaymentsConnector, SubscriptionConnector}
import javax.inject.{Inject, Singleton}
import models.User
import models.circumstanceInfo.CircumstanceDetails
import models.core.ErrorModel
import models.payments.{PaymentRedirectModel, PaymentStartModel}
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import play.api.http.Status.NOT_FOUND

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsService @Inject()(paymentsConnector: PaymentsConnector, subscriptionConnector: SubscriptionConnector) {

  def postPaymentDetails[A](user: User[A])
                           (implicit hc: HeaderCarrier, ec: ExecutionContext, config: AppConfig): Future[Either[ErrorModel, PaymentRedirectModel]] = {

    val convenienceUrl = {
      if (user.isAgent) {
        config.host + controllers.agentClientRelationship.routes.SelectClientVrnController.show()
      } else {
        config.host + controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix)
      }
    }

    subscriptionConnector.getCustomerCircumstanceDetails(user.vrn).flatMap {
      case Left(error) =>
        Logger(getClass.getSimpleName).warn(
          s"[PaymentsService][postPaymentDetails] Error retrieving Customer Circumstance Details with status ${error.status} - ${error.message}"
        )
        Future.successful(Left(error))
      case Right(circumstanceDetails) =>
        val paymentDetails: PaymentStartModel = PaymentStartModel(
          user.vrn,
          user.isAgent,
          config.host + controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix),
          config.host + controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix),
          convenienceUrl,
          circumstanceDetails.partyType,
          circumstanceDetails.customerDetails.welshIndicator
        )
        paymentsConnector.postPaymentsDetails(paymentDetails)
    }
  }
}
