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

package services

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants._
import mocks.connectors.MockSubscriptionConnector
import models.circumstanceInfo.CircumstanceDetails
import models.core.ErrorModel
import utils.TestUtil
import play.api.test.Helpers._

import scala.concurrent.Future

class CustomerCircumstanceDetailsServiceSpec extends TestUtil with MockSubscriptionConnector {

  object TestCustomerCircumstanceDetailsService extends CustomerCircumstanceDetailsService(mockSubscriptionConnector)

  "CustomerDetailsService" should {

    def result: Future[Either[ErrorModel, CircumstanceDetails]] = TestCustomerCircumstanceDetailsService.getCustomerCircumstanceDetails(vrn)

    "for getCustomerDetails method" when {

      "called for a Right with CustomerDetails" should {

        "return a CustomerDetailsModel" in {
          setupMockUserDetails(vrn)(Right(customerInformationModelMaxOrganisation))
          await(result) shouldBe Right(customerInformationModelMaxOrganisation)
        }
      }

      "given an error should" should {

        "return an Left with an ErrorModel" in {
          setupMockUserDetails(vrn)(Left(errorModel))
          await(result) shouldBe Left(errorModel)
        }
      }
    }
  }
}
