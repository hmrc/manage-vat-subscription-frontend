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

package models.circumstanceInfo

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsObject, Json}

class PPOBAddressSpec  extends AnyWordSpecLike with Matchers {
  val correctMaxJson: JsObject = Json.obj(
      "line1" -> "Bedrock Quarry",
      "line2" -> "Bedrock",
      "line3" -> "Graveldon",
      "postCode" -> "GV2 4BB",
      "countryCode" -> "+44"
  )

  val correctMinJson: JsObject = Json.obj(
      "line1" -> "Bedrock Quarry",
      "countryCode" -> "+44"
  )

  val correctJsonNoLine1: JsObject = Json.obj(
    "countryCode" -> "+44")

  "Address" should {

    "correctly parse from max json" in {
      correctMaxJson.as[PPOBAddress] shouldBe PPOBAddress("Bedrock Quarry", Some("Bedrock"), Some("Graveldon"), None, None, Some("GV2 4BB"), "+44")
    }

    "correctly parse from min json" in {
      correctMinJson.as[PPOBAddress] shouldBe PPOBAddress("Bedrock Quarry", None, None, None, None, None, "+44")
    }

    "correctly parse when address line 1 is missing" in {
      correctJsonNoLine1.as[PPOBAddress] shouldBe PPOBAddress("", None, None, None, None, None, "+44")
    }
  }
}
