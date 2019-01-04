/*
 * Copyright 2019 HM Revenue & Customs
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

package models.returnFrequency

import assets.ReturnPeriodTestConstants._
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class ReturnPeriodSpec extends UnitSpec {

  "ReturnPeriod.apply" should {

    "for 'January' should return Jan case object" in {
      ReturnPeriod(returnPeriodJan) shouldBe Some(Jan)
    }

    "for 'February' should return Feb case object" in {
      ReturnPeriod(returnPeriodFeb) shouldBe Some(Feb)
    }

    "for 'March' should return Mar case object" in {
      ReturnPeriod(returnPeriodMar) shouldBe Some(Mar)
    }

    "for 'Monthly' should return Monhtly case object" in {
      ReturnPeriod(returnPeriodMonthly) shouldBe Some(Monthly)
    }

    "for non existent id should return None" in {
      ReturnPeriod("") shouldBe None
    }
  }

  "ReturnPeriod.unapply" should {

    "for Jan case object return 'January'" in {
      ReturnPeriod.unapply(Jan) shouldBe returnPeriodJan
    }

    "for Feb case object return 'February'" in {
      ReturnPeriod.unapply(Feb) shouldBe returnPeriodFeb
    }

    "for Mar case object return 'March'" in {
      ReturnPeriod.unapply(Mar) shouldBe returnPeriodMar
    }

    "for Monthly case object return 'Monthly'" in {
      ReturnPeriod.unapply(Monthly) shouldBe returnPeriodMonthly
    }
  }

  "ReturnPeriod Reads" should {
    "parse the json correctly for MA types" in {
      returnPeriodMAJson.as[ReturnPeriod] shouldBe Jan
    }

    "parse the json correctly for MB types" in {
      returnPeriodMBJson.as[ReturnPeriod] shouldBe Feb
    }

    "parse the json correctly for MC types" in {
      returnPeriodMCJson.as[ReturnPeriod] shouldBe Mar
    }

    "parse the json correctly for MM types" in {
      returnPeriodMMJson.as[ReturnPeriod] shouldBe Monthly
    }
  }

  "ReturnPeriod Writes" should {

    "output a fully populated MA ReturnPeriod object with all fields populated" in {
      Json.toJson(Jan) shouldBe returnPeriodMAJson
    }

    "output a fully populated MB ReturnPeriod object with all fields populated" in {
      Json.toJson(Feb) shouldBe returnPeriodMBJson
    }

    "output a fully populated MC ReturnPeriod object with all fields populated" in {
      Json.toJson(Mar) shouldBe returnPeriodMCJson
    }

    "output a fully populated MM ReturnPeriod object with all fields populated" in {
      Json.toJson(Monthly) shouldBe returnPeriodMMJson
    }
  }
}