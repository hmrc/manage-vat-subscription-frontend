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

package services

import assets.BaseTestConstants.errorModel
import assets.PaymentsTestConstants._
import mocks.connectors.MockPaymentsConnector
import models.core.ErrorModel
import models.payments.PaymentRedirectModel
import utils.TestUtil

import scala.concurrent.Future

class PaymentsServiceSpec extends TestUtil with MockPaymentsConnector {

  object TestPaymentsService extends PaymentsService(mockPaymentsConnector)

  "PaymentsService" should {

    def result: Future[Either[ErrorModel, PaymentRedirectModel]] = TestPaymentsService.postPaymentDetails(paymentStart1)

    "for postPaymentDetails method" when {

      "called for a Right with a redirect url" should {

        "return a PaymentRedirectModel" in {
          setupMockPostPaymentsDetails(paymentStart1)(Right(successPaymentsResponseModel))
          await(result) shouldBe Right(successPaymentsResponseModel)
        }
      }

      "given an error should" should {

        "return a Left with an ErrorModel" in {
          setupMockPostPaymentsDetails(paymentStart1)(Left(errorModel))
          await(result) shouldBe Left(errorModel)
        }
      }
    }
  }

}
