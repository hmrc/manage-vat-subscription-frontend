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

package connectors

import config.FrontendAppConfig
import connectors.httpParsers.ResponseHttpParser._
import javax.inject.{Inject, Singleton}
import models.core.ErrorModel
import models.payments.{PaymentRedirectModel, PaymentStartModel}
import play.api.Logger
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import play.api.http.Status
import play.api.http.HeaderNames._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsConnector @Inject()(val http: HttpClient,
                                  val config: FrontendAppConfig) {

  def postPaymentsDetails(paymentStart: PaymentStartModel)
                         (implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[HttpPostResult[PaymentRedirectModel]] = {

    val url = s"${config.bankAccountCoc}/bank-account-coc/start-journey-of-change-bank-account"

    http.POST[PaymentStartModel,HttpResponse](url, paymentStart) map { resp =>
      resp.status match {
        case Status.ACCEPTED =>
          resp.header(LOCATION) match {
            case Some(redirectUrl) => Right(PaymentRedirectModel(redirectUrl))
            case _ =>
              Logger.warn(s"[PaymentsConnector][postPaymentsDetails]: Response Header did not contain location redirect")
              Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Response Header did not contain location redirect"))
          }
        case status =>
          Logger.warn(s"[PaymentsConnector][postPaymentsDetails]: Unexpected Response, Status $status returned")
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Downstream error returned from Payments"))
      }
    }

  }

}
