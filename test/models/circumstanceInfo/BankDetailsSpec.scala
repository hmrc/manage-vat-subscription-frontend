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

package models.circumstanceInfo

import play.api.libs.json.Json
import assets.BankDetailsTestConstants._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BankDetailsSpec extends AnyWordSpecLike with Matchers {

  "BankDetails Reads" should {
    "parse the json correctly when all optional fields are populated" in {
      bankDetailsJsonMax.as[BankDetails] shouldBe bankDetailsModelMax
    }

    "parse the json correctly when no fields are supplied" in {
      bankDetailsJsonMin.as[BankDetails] shouldBe bankDetailsModelMin
    }
  }

  "BankDetails Writes" should {

    "output a fully populated BankDetails object with all fields populated" in {
      Json.toJson(bankDetailsModelMax) shouldBe bankDetailsJsonMax
    }

    "an empty json object when an empty BankDetails object is marshalled" in {
      Json.toJson(bankDetailsModelMin) shouldBe bankDetailsJsonMin
    }
  }
}