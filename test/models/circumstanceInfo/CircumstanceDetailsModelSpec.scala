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

package models.circumstanceInfo

import assets.CircumstanceDetailsTestConstants._
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class CircumstanceDetailsModelSpec extends UnitSpec {

  "Deserialize from JSON" when {

    "succeeds when all registration fields are populated" in {
      customerInformationJsonMax.as[CircumstanceDetails] shouldBe customerInformationModelMax
    }


    "succeeds when optional values are not supplied" in {
      customerInformationJsonMin.as[CircumstanceDetails] shouldBe customerInformationModelMin
    }

  }

  "Serialize to JSON" when {

    "succeeds when all registration fields are populated" in {
      Json.toJson(customerInformationModelMax) shouldBe customerInformationJsonMax
    }


    "succeeds when optional values are not supplied" in {
      Json.toJson(customerInformationModelMin) shouldBe customerInformationJsonMin
    }
  }
}
