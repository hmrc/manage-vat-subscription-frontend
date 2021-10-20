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

package connectors

import connectors.httpParsers.ResponseHttpParser.HttpPostResult
import mocks.MockHttp
import models.core.ErrorModel
import models.payments.{PaymentRedirectModel, PaymentStartModel}
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil
import play.api.test.Helpers._

import scala.concurrent.Future

class PaymentsConnectorSpec extends TestUtil with MockHttp {

  val errorModel: HttpResponse = HttpResponse(Status.BAD_REQUEST, "Error Message")

  object TestPaymentsConnector extends PaymentsConnector(mockHttp,mockConfig)

  "PaymentsConnector" when {

    val continueUrl = "continue-url"
    def postPaymentsDetailsResult: Future[HttpPostResult[PaymentRedirectModel]] =
      TestPaymentsConnector.postPaymentsDetails(PaymentStartModel("someVrn", isAgent = true, "returnUrl", "backUrl", "convenienceUrl", None, None))

    "for postPaymentsDetails method" when {

      "when given a successful response" should {

        "return a Right with a PaymentRedirectModel" in {
          val successfulResponse = Right(PaymentRedirectModel(continueUrl))
          setupMockHttpPost(s"${mockConfig.bankAccountCoc}/bank-account-coc/start-journey-of-change-bank-account")(successfulResponse)
          await(postPaymentsDetailsResult) shouldBe Right(PaymentRedirectModel(continueUrl))
        }
      }

      "given an unsuccessful response" should {

        "return an Left with an ErrorModel" in {
          val failedResponse = Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Bad things"))
          setupMockHttpPost(s"${mockConfig.bankAccountCoc}/bank-account-coc/start-journey-of-change-bank-account")(failedResponse)
          await(postPaymentsDetailsResult) shouldBe Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Bad things"))
        }
      }
    }
  }

}
