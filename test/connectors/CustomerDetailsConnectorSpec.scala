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

import connectors.httpParsers.CustomerDetailsHttpParser.HttpGetResult
import mocks.MockHttp
import models.customerInfo.{CustomerDetailsModel, CustomerInformationModel, MTDfBMandated}
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

import scala.concurrent.Future

class CustomerDetailsConnectorSpec extends TestUtil with MockHttp{

  val vatNumber = "321321"
  val successJson = HttpResponse(Status.OK, Some(Json.obj(
    "mandationStatus" -> "MTDfB Mandated",
    "customerDetails" -> Json.obj(
      "organisationName" -> "Ancient Antiques",
      "firstName" -> "Fred",
      "lastName" -> "Flintstone",
      "tradingName" -> "a"
    )
  )))

  val successModel = CustomerInformationModel(MTDfBMandated, CustomerDetailsModel(Some("Fred"), Some("Flintstone"), Some("Ancient Antiques"), Some("a")))
  val successResponseBadJson = HttpResponse(Status.OK, Some(Json.parse("{}")))
  val errorModel = HttpResponse(Status.BAD_REQUEST, responseString = Some("Error Message"))

  object TestCustomerDetailsConnector extends CustomerDetailsConnector(mockHttpGet,frontendAppConfig)

  "CustomerDetailsConnector" should {

    def result: Future[HttpGetResult[CustomerInformationModel]] = TestCustomerDetailsConnector.getCustomerDetails(vatNumber)

    "format the url correctly for" when {
      "calling getCustomerDetailsUrl" in {
        val testUrl = TestCustomerDetailsConnector.getCustomerDetailsUrl(vatNumber)
        testUrl shouldBe s"${frontendAppConfig.vatSubscriptionUrl}/$vatNumber/customer-details"
      }
    }

    "for getCustomerDetails method" when {

      "called for a Right with CustomerDetails" should {

        "return a CustomerDetailsModel" in {
          setupMockHttpGet(TestCustomerDetailsConnector.getCustomerDetailsUrl(vatNumber))(Right(successModel))
          await(result) shouldBe Right(successModel)
        }

      }

      "given an error should" should {

        "return an Left with an ErrorModel" in {
          setupMockHttpGet(TestCustomerDetailsConnector.getCustomerDetailsUrl(vatNumber))(Left(errorModel))
          await(result) shouldBe Left(errorModel)
        }

      }

    }

  }

}
