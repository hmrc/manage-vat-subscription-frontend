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

class ReturnPeriodSpec extends UnitSpec {

  private val maInput = """{"stdReturnPeriod":"MA"}"""
  private val mbInput = """{"stdReturnPeriod":"MB"}"""
  private val mcInput = """{"stdReturnPeriod":"MC"}"""
  private val mmInput = """{"stdReturnPeriod":"MM"}"""

  "ReturnPeriod Reads" should {
    "parse the json correctly for MA types" in {
      val maPeriod = ReturnPeriod.returnPeriodReader.reads(Json.parse(maInput)).get
      assert(maPeriod.stdReturnPeriod == "MA")
    }

    "parse the json correctly for MB types" in {
      val maPeriod = ReturnPeriod.returnPeriodReader.reads(Json.parse(mbInput)).get
      assert(maPeriod.stdReturnPeriod == "MB")
    }

    "parse the json correctly for MC types" in {
      val maPeriod = ReturnPeriod.returnPeriodReader.reads(Json.parse(mcInput)).get
      assert(maPeriod.stdReturnPeriod == "MC")
    }

    "parse the json correctly for MM types" in {
      val maPeriod = ReturnPeriod.returnPeriodReader.reads(Json.parse(mmInput)).get
      assert(maPeriod.stdReturnPeriod == "MM")
    }
  }

  "ReturnPeriod Writes" should {

    "output a fully populated MA ReturnPeriod object with all fields populated" in {
      val period = MAReturnPeriod
      val output = ReturnPeriod.returnPeriodWriter.writes(period)
      assert(output.toString() == """{"stdReturnPeriod":"MA"}""")
    }

    "output a fully populated MB ReturnPeriod object with all fields populated" in {
      val period = MBReturnPeriod
      val output = ReturnPeriod.returnPeriodWriter.writes(period)
      assert(output.toString() == """{"stdReturnPeriod":"MB"}""")
    }

    "output a fully populated MC ReturnPeriod object with all fields populated" in {
      val period = MCReturnPeriod
      val output = ReturnPeriod.returnPeriodWriter.writes(period)
      assert(output.toString() == """{"stdReturnPeriod":"MC"}""")
    }

    "output a fully populated MM ReturnPeriod object with all fields populated" in {
      val period = MMReturnPeriod
      val output = ReturnPeriod.returnPeriodWriter.writes(period)
      assert(output.toString() == """{"stdReturnPeriod":"MM"}""")
    }
  }

}
