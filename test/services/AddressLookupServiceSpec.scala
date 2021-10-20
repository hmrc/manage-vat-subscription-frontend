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

import assets.CustomerAddressTestConstants._
import mocks.connectors.MockAddressLookupConnector
import models.customerAddress.AddressLookupOnRampModel
import utils.TestUtil
import play.api.test.Helpers._

class AddressLookupServiceSpec extends TestUtil with MockAddressLookupConnector {

  "Calling .retrieveAddress" when {

    def setup(addressLookupGetResponse: AddressLookupGetAddressResponse): AddressLookupService = {
      setupMockGetAddress(addressLookupGetResponse)
      new AddressLookupService(mockAddressLookupConnector, messagesApi, mockConfig)
    }

    "connector call is successful" should {
      lazy val service = setup(Right(customerAddressMax))
      lazy val result = service.retrieveAddress("12345")

      "return successful SubscriptionUpdateResponseModel" in {
        await(result) shouldBe Right(customerAddressMax)
      }
    }
  }

  "Calling initialiseJourney" when {

    def setup(addressLookupInitialiseResponse: AddressLookupInitialiseResponse): AddressLookupService = {
      setupMockInitialiseJourney(addressLookupInitialiseResponse)
      new AddressLookupService(mockAddressLookupConnector, messagesApi, mockConfig)
    }

    "connector call is successful" should {

      lazy val service = setup(Right(AddressLookupOnRampModel("redirect-url")))
      lazy val result = service.initialiseJourney(hc, ec, user)

      "return successful SubscriptionUpdateResponseModel" in {
        await(result) shouldBe Right(AddressLookupOnRampModel("redirect-url"))
      }
    }
  }
}
