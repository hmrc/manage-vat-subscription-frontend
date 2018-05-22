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

import mocks.connectors.MockSubscriptionConnector
import models.core.SubscriptionUpdateResponseModel
import models.returnFrequency.Jan
import utils.TestUtil

class ReturnFrequencyServiceSpec extends TestUtil with MockSubscriptionConnector {

  def setup(subscriptionResponse: UpdateBusinessAddressResponse): ReturnFrequencyService = {

    setupMockUpdateReturnFrequency(subscriptionResponse)
    new ReturnFrequencyService(mockSubscriptionConnector)
  }

  "Calling .updateReturnFrequency" should {

    val subscriptionResult = SubscriptionUpdateResponseModel("formBundle")

    lazy val service = setup(Right(subscriptionResult))
    lazy val result = service.updateReturnFrequency("", Jan)

    "return successful SubscriptionUpdateResponseModel" in {
      await(result) shouldBe Right(subscriptionResult)
    }
  }
}
