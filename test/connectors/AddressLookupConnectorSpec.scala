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

package connectors

import assets.BaseTestConstants._
import assets.CustomerAddressTestConstants._
import connectors.httpParsers.ResponseHttpParser.{HttpGetResult, HttpPostResult}
import mocks.MockHttp
import models.core.ErrorModel
import models.customerAddress.{AddressLookupJsonBuilder, AddressLookupOnRampModel, AddressModel}
import play.api.http.HeaderNames.LOCATION
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

import scala.concurrent.Future

class AddressLookupConnectorSpec extends TestUtil with MockHttp{

  val errorModel = HttpResponse(Status.BAD_REQUEST, responseString = Some("Error Message"))

  object TestAddressLookupConnector extends AddressLookupConnector(mockHttp,mockConfig)

  "AddressLookupConnector" should {

    def getAddressResult: Future[HttpGetResult[AddressModel]] = TestAddressLookupConnector.getAddress(vrn)

    "format the getAddressUrl correctly for" when {
      "calling getCustomerDetailsUrl" in {
        val testUrl = TestAddressLookupConnector.getAddressUrl(vrn)
        testUrl shouldBe s"${mockConfig.addressLookupService}/api/confirmed?id=$vrn"
      }
    }

    "for getAddress method" when {

      "called for a Right with CustomerDetails" should {

        "return a CustomerAddressModel" in {
          setupMockHttpGet(TestAddressLookupConnector.getAddressUrl(vrn))(Right(customerAddressMax))
          await(getAddressResult) shouldBe Right(customerAddressMax)
        }
      }

      "given an error should" should {

        "return an Left with an ErrorModel" in {
          setupMockHttpGet(TestAddressLookupConnector.getAddressUrl(vrn))(Left(errorModel))
          await(getAddressResult) shouldBe Left(errorModel)
        }
      }
    }

    val continueUrl = "continue-url"
    def initaliseJourneyResult: Future[HttpPostResult[AddressLookupOnRampModel]] =
      TestAddressLookupConnector.initialiseJourney(AddressLookupJsonBuilder(continueUrl)(user,messages))

    "for initialiseJourney method" when {

      "when given a successful response" should {

        "return a Right with an AddressLookupOnRampModel" in {
          val successfulResponse = HttpResponse(Status.ACCEPTED, responseHeaders = Map(LOCATION -> Seq(continueUrl)))
          setupMockHttpPost(s"${mockConfig.addressLookupService}/api/init")(successfulResponse)
          await(initaliseJourneyResult) shouldBe successfulResponse
        }
      }

      "given a non successful response should" should {

        "return an Left with an ErrorModel" in {
          val failedResponse = HttpResponse(Status.INTERNAL_SERVER_ERROR, responseHeaders = Map(LOCATION -> Seq(continueUrl)))
          setupMockHttpPost(s"${mockConfig.addressLookupService}/api/init")(failedResponse)
          await(initaliseJourneyResult) shouldBe failedResponse
        }
      }
    }
  }
}
