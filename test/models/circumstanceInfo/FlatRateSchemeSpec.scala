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

class FlatRateSchemeSpec extends UnitSpec {

  private val allFieldsInput =
    """
      |{"FRSCategory":"003",
      |  "FRSPercentage":59.99,
      |  "startDate":"2001-01-01",
      |  "limitedCostTrader":true}
    """.stripMargin

  private val nullFields = """{"FRSCategory":null,"FRSPercentage":null,"limitedCostTrader":null,"startDate":null}"""

  private val allFieldsOutput =
    """{"FRSCategory":"001","FRSPercentage":30.01,"limitedCostTrader":false,"startDate":"2017-12-01"}"""

  private val noFields = "{}"

  "FlatRateScheme Reads" should {
    "parse the json correctly when all optional fields are populated" in {
      val frs = FlatRateScheme.frsReader.reads(Json.parse(allFieldsInput)).get
      assert(frs.FRSCategory.contains("003"))
      assert(frs.FRSPercentage.contains(59.99))
      assert(frs.startDate.contains("2001-01-01"))
      assert(frs.limitedCostTrader.contains(true))
    }

    "parse the json correctly when all fields are null" in {
      val frs = FlatRateScheme.frsReader.reads(Json.parse(nullFields)).get
      assert(frs.FRSCategory.isEmpty)
      assert(frs.FRSPercentage.isEmpty)
      assert(frs.startDate.isEmpty)
      assert(frs.limitedCostTrader.isEmpty)
    }

    "parse the json correctly when no fields are supplied" in {
      val frs = FlatRateScheme.frsReader.reads(Json.parse(noFields)).get
      assert(frs.FRSCategory.isEmpty)
      assert(frs.FRSPercentage.isEmpty)
      assert(frs.startDate.isEmpty)
      assert(frs.limitedCostTrader.isEmpty)
    }
  }

  "FlatRateScheme Writes" should {
    "output a fully populated FlatRateScheme object with all fields populated" in {
      val frs = FlatRateScheme(Some("001"), Some(30.01), Some(false), Some("2017-12-01"))
      val output = FlatRateScheme.frsWriter.writes(frs)
      assert(output.toString() == allFieldsOutput)
    }

    "an empty json object when an empty FlatRateScheme object is marshalled" in {
      val frs = FlatRateScheme(None,None,None,None)
      val output = FlatRateScheme.frsWriter.writes(frs)
      assert(output.toString() == noFields)
    }
  }

}
