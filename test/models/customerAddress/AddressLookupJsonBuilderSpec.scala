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

package models.customerAddress

import assets.CustomerAddressTestConstants._
import play.api.libs.json.Json
import utils.TestUtil

class AddressLookupJsonBuilderSpec extends TestUtil {

  "AddressLookupJsonBuilder" should {

    "Serialize to JSON" when {

      "the continueUrl is given and the user is not an agent" in {
        Json.toJson(AddressLookupJsonBuilder("/lookup-address/confirmed")(user,messages)) shouldBe clientAddressLookupJson
      }

      "the continueUrl is given and the user is an agent" in {
        Json.toJson(AddressLookupJsonBuilder("/lookup-address/confirmed")(agentUser,messages)) shouldBe agentAddressLookupJson
      }

    }
  }

}

