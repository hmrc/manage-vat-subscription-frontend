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

import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec
import assets.ReturnPeriodTestConstants._

class ReturnPeriodSpec extends UnitSpec {

  "ReturnPeriod Reads" should {
    "parse the json correctly for MA types" in {
      returnPeriodMA.as[ReturnPeriod] shouldBe MAReturnPeriod
    }

    "parse the json correctly for MB types" in {
      returnPeriodMB.as[ReturnPeriod] shouldBe MBReturnPeriod
    }

    "parse the json correctly for MC types" in {
      returnPeriodMC.as[ReturnPeriod] shouldBe MCReturnPeriod
    }

    "parse the json correctly for MM types" in {
      returnPeriodMM.as[ReturnPeriod] shouldBe MMReturnPeriod
    }
  }

  "ReturnPeriod Writes" should {

    "output a fully populated MA ReturnPeriod object with all fields populated" in {
      Json.toJson(MAReturnPeriod) shouldBe returnPeriodMA
    }

    "output a fully populated MB ReturnPeriod object with all fields populated" in {
      Json.toJson(MBReturnPeriod) shouldBe returnPeriodMB
    }

    "output a fully populated MC ReturnPeriod object with all fields populated" in {
      Json.toJson(MCReturnPeriod) shouldBe returnPeriodMC
    }

    "output a fully populated MM ReturnPeriod object with all fields populated" in {
      Json.toJson(MMReturnPeriod) shouldBe returnPeriodMM
    }
  }
}