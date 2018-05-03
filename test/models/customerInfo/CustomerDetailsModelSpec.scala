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

package models.customerInfo

import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec


class CustomerDetailsModelSpec extends UnitSpec {

  private val testJson = Json.obj(
    "organisationName" -> "Ancient Antiques",
    "firstName" -> "Fred",
    "lastName" -> "Flintstone",
    "tradingName" -> "a"
  )

  private val testJsonMin = Json.obj()

  "CustomerDetailsModel" should {

    "Deserialize from JSON" when {

      "all optional fields are populated" in {
        val expected = CustomerDetailsModel(Some("Fred"), Some("Flintstone"), Some("Ancient Antiques"), Some("a"))
        testJson.as[CustomerDetailsModel] shouldBe expected
      }

      "no optional fields are returned" in {
        val expected = CustomerDetailsModel(None, None, None, None)
        testJsonMin.as[CustomerDetailsModel] shouldBe expected
      }
    }

    "Serialize to JSON" when {

      "all optional fields are populated" in {
        val model = CustomerDetailsModel(Some("Fred"), Some("Flintstone"), Some("Ancient Antiques"), Some("a"))
        Json.toJson(model) shouldBe testJson
      }

      "no optional fields are returned" in {
        val model = CustomerDetailsModel(None, None, None, None)
        Json.toJson(model) shouldBe testJsonMin
      }
    }
  }

}