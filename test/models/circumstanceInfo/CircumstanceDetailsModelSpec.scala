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

import assets.CircumstanceDetailsTestConstants
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class CircumstanceDetailsModelSpec extends UnitSpec {

  "Deserialize from JSON" when {

    "succeeds when all registration fields are populated" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.fullPopulation).as[CircumstanceDetails]
      assert(unmarshalled.bankDetails.get.sortCode == Some("77****"))
    }

    "succeeds when optional banking details are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noBanking).as[CircumstanceDetails]
      assert(unmarshalled.bankDetails.isEmpty)
    }

    "succeeds when optional business name is not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noBusinessName).as[CircumstanceDetails]
      assert(unmarshalled.businessName.isEmpty)
    }

    "succeeds when optional Flat Rate Scheme values are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noFlatRateScheme).as[CircumstanceDetails]
      assert(unmarshalled.businessName == Some("Ancient Antiques"))
      assert(unmarshalled.flatRateScheme.isEmpty)
    }

    "succeeds when optional PPOB values are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noPPOB).as[CircumstanceDetails]
      assert(unmarshalled.businessName == Some("Ancient Antiques"))
      assert(unmarshalled.ppob.isEmpty)
    }

    "succeeds when optional Return Period values are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noReturnPeriod).as[CircumstanceDetails]
      assert(unmarshalled.returnPeriod.isEmpty)
    }

  }

}
