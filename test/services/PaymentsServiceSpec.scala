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

import assets.BaseTestConstants.{errorModel, vrn}
import assets.CircumstanceDetailsTestConstants._
import assets.PaymentsTestConstants._
import mocks.connectors.{MockPaymentsConnector, MockSubscriptionConnector}
import utils.TestUtil

class PaymentsServiceSpec extends TestUtil with MockPaymentsConnector with MockSubscriptionConnector {

  object TestPaymentsService extends PaymentsService(mockPaymentsConnector, mockSubscriptionConnector)

  "PaymentsService" should {

    "for postPaymentDetails method" when {

      "a success response is received" should {

        "for a principal user" should {

          "return a PaymentRedirectModel" in {
            setupMockUserDetails(vrn)(Right(customerInformationWithPartyType(None)))
            setupMockPostPaymentsDetails(principlePaymentStart)(Right(successPaymentsResponseModel))
            await(TestPaymentsService.postPaymentDetails(user)(implicitly, implicitly, mockConfig)) shouldBe Right(successPaymentsResponseModel)
          }
        }

        "for an Agent" should {

          "return a PaymentRedirectModel" in {
            setupMockUserDetails(vrn)(Right(customerInformationWithPartyType(None)))
            setupMockPostPaymentsDetails(agentPaymentStart)(Right(successPaymentsResponseModel))
            await(TestPaymentsService.postPaymentDetails(agentUser)(implicitly, implicitly, mockConfig)) shouldBe Right(successPaymentsResponseModel)
          }
        }
      }

      "given an error should" should {

        "return a Left with an ErrorModel when POST fails" in {
          setupMockUserDetails(vrn)(Right(customerInformationWithPartyType(None)))
          setupMockPostPaymentsDetails(principlePaymentStart)(Left(errorModel))
          await(TestPaymentsService.postPaymentDetails(user)(implicitly, implicitly, mockConfig)) shouldBe Left(errorModel)
        }

        "return a Left with an ErrorModel when get user details fails" in {
          setupMockUserDetails(vrn)(Left(errorModel))
          await(TestPaymentsService.postPaymentDetails(user)(implicitly, implicitly, mockConfig)) shouldBe Left(errorModel)
        }
      }
    }
  }
}
