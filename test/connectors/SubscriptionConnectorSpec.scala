/*
 * Copyright 2023 HM Revenue & Customs
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
import assets.CircumstanceDetailsTestConstants._
import assets.UpdatePPOBAddressTestConstants._
import connectors.httpParsers.ResponseHttpParser.HttpGetResult
import mocks.MockHttp
import models.circumstanceInfo.CircumstanceDetails
import models.core.SubscriptionUpdateResponseModel
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil
import play.api.test.Helpers._

import scala.concurrent.Future

class SubscriptionConnectorSpec extends TestUtil with MockHttp{

  val errorModel: HttpResponse = HttpResponse(Status.BAD_REQUEST, "Error Message")

  object TestSubscriptionConnector extends SubscriptionConnector(mockHttp, mockConfig)

  "SubscriptionConnector" when {

    "calling .getCustomerDetailsUrl" should {

      "format the url correctly" in {
        val testUrl = TestSubscriptionConnector.getCustomerDetailsUrl(vrn)
        testUrl shouldBe s"${mockConfig.vatSubscriptionUrl}/vat-subscription/$vrn/full-information"
      }
    }

    "calling .getCustomerDetails" when {

      def result: Future[HttpGetResult[CircumstanceDetails]] = TestSubscriptionConnector.getCustomerCircumstanceDetails(vrn)

      "called for a Right with CustomerDetails" should {

        "return a CustomerDetailsModel" in {
          setupMockHttpGet(TestSubscriptionConnector.getCustomerDetailsUrl(vrn))(Right(customerInformationModelMaxOrganisation))
          await(result) shouldBe Right(customerInformationModelMaxOrganisation)
        }
      }

      "given an error should" should {

        "return a Left with an ErrorModel" in {
          setupMockHttpGet(TestSubscriptionConnector.getCustomerDetailsUrl(vrn))(Left(errorModel))
          await(result) shouldBe Left(errorModel)
        }
      }
    }

    "calling .updatePPOB" when {

      def result: Future[HttpGetResult[SubscriptionUpdateResponseModel]] =
        TestSubscriptionConnector.updatePPOB(vrn, updatePPOBModelMax)

      "called with a Right SubscriptionUpdateResponseModel" should {

        "return a SubscriptionUpdateResponseModel" in {
          setupMockHttpPut(s"${mockConfig.vatSubscriptionUrl}/vat-subscription/$vrn/ppob")(Right(SubscriptionUpdateResponseModel("12345")))
          await(result) shouldBe Right(SubscriptionUpdateResponseModel("12345"))
        }
      }

      "given an error" should {

        "return a Left with an ErrorModel" in {
          setupMockHttpPut(s"${mockConfig.vatSubscriptionUrl}/vat-subscription/$vrn/ppob")(errorModel)
          await(result) shouldBe errorModel
        }
      }
    }

    "calling .validateBusinessAddress" when {

      def result: Future[HttpGetResult[SubscriptionUpdateResponseModel]] =
        TestSubscriptionConnector.validateBusinessAddress(vrn)

      "called with a Right SubscriptionUpdateResponseModel" should {

        "return a SubscriptionUpdateResponseModel" in {
          setupMockHttpPut(s"${mockConfig.vatSubscriptionUrl}/vat-subscription/$vrn/ppob")(Right(SubscriptionUpdateResponseModel("12345")))
          await(result) shouldBe Right(SubscriptionUpdateResponseModel("12345"))
        }
      }

      "given an error" should {

        "return a Left with an ErrorModel" in {
          setupMockHttpPut(s"${mockConfig.vatSubscriptionUrl}/vat-subscription/$vrn/ppob")(errorModel)
          await(result) shouldBe errorModel
        }
      }

    }
  }
}
