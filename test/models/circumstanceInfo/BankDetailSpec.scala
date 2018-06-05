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

class BankDetailsSpec extends UnitSpec {

  private val allFieldsInput = """{"accountHolderName":"**********************","bankAccountNumber":"**1234","sortCode":"500000"}"""

  private val nullFields = """{"accountHolderName":null,"bankAccountNumber":null,"sortCode":null}"""

  private val allFieldsOutput = """{"accountHolderName":"**********************","bankAccountNumber":"**1111","sortCode":"100000"}"""

  private val noFields = "{}"

  "BankDetails Reads" should {
    "parse the json correctly when all optional fields are populated" in {
      val bankDetails = BankDetails.bankReader.reads(Json.parse(allFieldsInput)).get
      assert(bankDetails.accountHolderName.contains("**********************"))
      assert(bankDetails.bankAccountNumber.contains("**1234"))
      assert(bankDetails.sortCode.contains("500000"))
    }

    "parse the json correctly when all fields are null" in {
      val bankDetails = BankDetails.bankReader.reads(Json.parse(nullFields)).get
      assert(bankDetails.accountHolderName.isEmpty)
      assert(bankDetails.bankAccountNumber.isEmpty)
      assert(bankDetails.sortCode.isEmpty)
    }

    "parse the json correctly when no fields are supplied" in {
      val bankDetails = BankDetails.bankReader.reads(Json.parse(noFields)).get
      assert(bankDetails.accountHolderName.isEmpty)
      assert(bankDetails.bankAccountNumber.isEmpty)
      assert(bankDetails.sortCode.isEmpty)
    }
  }

  "BankDetails Writes" should {

    "output a fully populated BankDetails object with all fields populated" in {
      val bankDetails = BankDetails(Some("**********************"), Some("**1111"), Some("100000"))
      val output = BankDetails.bankWriter.writes(bankDetails)
      assert(output.toString() == allFieldsOutput)
    }

    "an empty json object when an empty BankDetails object is marshalled" in {
      val bankDetails = BankDetails(None,None,None)
      val output = BankDetails.bankWriter.writes(bankDetails)
      assert(output.toString() == noFields)
    }
  }
}
