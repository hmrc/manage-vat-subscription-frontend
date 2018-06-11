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
import connectors.httpParsers.PaymentsHttpParser.PaymentsReads
import connectors.httpParsers.ResponseHttpParser._
import javax.inject.{Inject, Singleton}
import models.payments.{PaymentRedirectModel, PaymentStartModel}
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsConnector @Inject()(val http: HttpClient,
                                  val config: FrontendAppConfig) {

  def postPaymentsDetails(paymentStart: PaymentStartModel)
                         (implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[HttpPostResult[PaymentRedirectModel]] = {

    val url = s"${config.bankAccountCoc}/bank-account-coc/start-journey-of-change-bank-account"
    Logger.debug(s"[PaymentsConnector][postPaymentsDetails]: Calling postPaymentsDetails with URL - $url")
    http.POST[PaymentStartModel,HttpPostResult[PaymentRedirectModel]](url, paymentStart)

  }

}
