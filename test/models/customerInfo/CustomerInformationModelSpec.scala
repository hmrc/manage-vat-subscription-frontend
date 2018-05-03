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

import assets.BaseTestConstants._
import models.customerInfo.CustomerInformationModel._
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec


class CustomerInformationModelSpec extends UnitSpec {

  private val testJson = Json.obj(
    "mandationStatus" -> MTDfBMandated.status,
    "customerDetails" -> Json.obj(
      "organisationName" -> organisationName,
      "firstName" -> firstName,
      "lastName" -> lastName,
      "tradingName" -> tradingName
    )
  )

  private val testJsonMin = Json.obj(
    "mandationStatus" -> MTDfBMandated.status,
    "customerDetails" -> Json.obj()
  )

  "CustomerInformationModel" should {

    "Deserialize from JSON" when {

      "all optional fields are populated" in {
        val expected = CustomerInformationModel(MTDfBMandated, CustomerDetailsModel(Some(firstName), Some(lastName), Some(organisationName), Some(tradingName)))
        testJson.as[CustomerInformationModel] shouldBe expected
      }

      "no optional fields are returned" in {
        val expected = CustomerInformationModel(MTDfBMandated, CustomerDetailsModel(None, None, None, None))
        testJsonMin.as[CustomerInformationModel] shouldBe expected
      }
    }

    "Serialize to JSON" when {

      "all optional fields are populated" in {
        val model = CustomerInformationModel(MTDfBMandated, CustomerDetailsModel(Some(firstName), Some(lastName), Some(organisationName), Some(tradingName)))
        Json.toJson(model) shouldBe testJson
      }

      "no optional fields are returned" in {
        val model = CustomerInformationModel(MTDfBMandated, CustomerDetailsModel(None, None, None, None))
        Json.toJson(model) shouldBe testJsonMin
      }
    }
  }

}
