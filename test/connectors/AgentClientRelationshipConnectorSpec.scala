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

class AgentClientRelationshipConnectorSpec extends TestUtil with MockHttp{

  val errorModel = HttpResponse(Status.BAD_REQUEST, responseString = Some("Error Message"))

  object TargetConnector extends AgentClientRelationshipConnector(mockHttp,frontendAppConfig)

  "AgentClientRelationshipConnector" should {

    def result(): Future[HttpGetResult[Boolean]] = TargetConnector.checkRelationship(arn, vrn)

    "Calling getRelationshipCheckUrl" should {
      "format the url correctly" in {
        val testUrl = TargetConnector.getRelationshipCheckUrl(arn, vrn)
        testUrl shouldBe s"${frontendAppConfig.addressLookupService}/agent/$arn/service/HMRC-MTD-VAT/client/VRN/$vrn"
      }
    }

    "Calling .checkRelationship" when {

      "a relationship exists" should {

        "return true" in {
          setupMockHttpGet(TargetConnector.getRelationshipCheckUrl(arn, vrn))(Right(true))
          await(result()) shouldBe Right(true)
        }
      }

      "a relationship does not exist" should {

        "return false" in {
          setupMockHttpGet(TargetConnector.getRelationshipCheckUrl(arn, vrn))(Right(false))
          await(result()) shouldBe Right(false)
        }
      }

      "given an error should" should {

        "return an Left with an ErrorModel" in {
          setupMockHttpGet(TargetConnector.getRelationshipCheckUrl(arn, vrn))(Left(errorModel))
          await(result()) shouldBe Left(errorModel)
        }
      }
    }
  }
}
