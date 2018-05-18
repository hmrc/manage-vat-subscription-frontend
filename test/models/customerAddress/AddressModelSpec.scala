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
import models.core.ErrorModel
import play.api.http.Status
import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.play.test.UnitSpec

class AddressModelSpec extends UnitSpec {

  "CustomerAddressModel" should {

    "Deserialize from JSON" when {

      "all optional fields are populated" in {
        Json.fromJson(customerAddressJsonMax)(AddressModel.customerAddressReads) shouldBe
          JsSuccess(customerAddressMax)
      }

      "some optional fields are populated" in {
        Json.fromJson(customerAddressJsonSome)(AddressModel.customerAddressReads) shouldBe
          JsSuccess(customerAddressSome)
      }

      "no optional fields are returned" in {
        Json.fromJson(customerAddressJsonMin)(AddressModel.customerAddressReads) shouldBe
          JsSuccess(customerAddressMin)
      }

    }

    "Serialize to JSON" when {

      "all optional fields are populated" in {
        Json.toJson(customerAddressMax) shouldBe customerAddressToJsonMax
      }

      "no optional fields are returned" in {
        Json.toJson(customerAddressMin) shouldBe customerAddressToJsonMin
      }
    }
  }

}
