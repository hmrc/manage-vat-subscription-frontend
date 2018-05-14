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

import assets.BaseTestConstants._
import assets.CustomerDetailsTestConstants._
import connectors.httpParsers.ResponseHttpParser.HttpGetResult
import mocks.MockHttp
import models.core.SubscriptionUpdateResponseModel
import models.customerInfo.CustomerDetailsModel
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

import scala.concurrent.Future

class SubscriptionConnectorSpec extends TestUtil with MockHttp{

  val errorModel = HttpResponse(Status.BAD_REQUEST, responseString = Some("Error Message"))

  object TestSubscriptionConnector extends SubscriptionConnector(mockHttp,frontendAppConfig)

  "SubscriptionConnector" when {

    "calling .getCustomerDetailsUrl" should {

      "format the url correctly" in {
        val testUrl = TestSubscriptionConnector.getCustomerDetailsUrl(vrn)
        testUrl shouldBe s"${frontendAppConfig.vatSubscriptionUrl}/vat-subscription/$vrn/customer-details"
      }
    }

    "calling .getCustomerDetails" when {

      def result: Future[HttpGetResult[CustomerDetailsModel]] = TestSubscriptionConnector.getCustomerDetails(vrn)

      "called for a Right with CustomerDetails" should {

        "return a CustomerDetailsModel" in {
          setupMockHttpGet(TestSubscriptionConnector.getCustomerDetailsUrl(vrn))(Right(individual))
          await(result) shouldBe Right(individual)
        }
      }

      "given an error should" should {

        "return a Left with an ErrorModel" in {
          setupMockHttpGet(TestSubscriptionConnector.getCustomerDetailsUrl(vrn))(Left(errorModel))
          await(result) shouldBe Left(errorModel)
        }
      }
    }

    "calling .updateBusinessAddress" should {

      def result: Future[HttpGetResult[SubscriptionUpdateResponseModel]] = TestSubscriptionConnector.updateBusinessAddress()

      "return a SubscriptionUpdateResponseModel" in {
        await(result) shouldBe Right(SubscriptionUpdateResponseModel("12345"))
      }
    }
  }
}
