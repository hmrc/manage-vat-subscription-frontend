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
      assert(unmarshalled.bankDetails.get.sortCode.contains("77****"))
      assert(unmarshalled.bankDetails.get.accountHolderName.contains("***********************"))
      assert(unmarshalled.bankDetails.get.bankAccountNumber.contains("****5274"))
    }

    "succeeds when optional banking details are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noBanking).as[CircumstanceDetails]

      // Only the bank details should be un-populated
      assert(unmarshalled.bankDetails.isEmpty)

      // The rest of the object should be populated
      assert(unmarshalled.businessName.contains("Ancient Antiques"))

      assert(unmarshalled.flatRateScheme.get.FRSCategory.contains("001"))
      assert(unmarshalled.flatRateScheme.get.FRSPercentage.contains(123.12))
      assert(unmarshalled.flatRateScheme.get.limitedCostTrader.contains(true))
      assert(unmarshalled.flatRateScheme.get.startDate.contains("2001-01-01"))

      assert(unmarshalled.ppob.get.address.get.line1.contains("VAT ADDR 1"))
      assert(unmarshalled.ppob.get.address.get.line2.contains("VAT ADDR 2"))
      assert(unmarshalled.ppob.get.address.get.line3.contains("VAT ADDR 3"))
      assert(unmarshalled.ppob.get.address.get.line4.contains("VAT ADDR 4"))
      assert(unmarshalled.ppob.get.address.get.line5.isEmpty)
      assert(unmarshalled.ppob.get.address.get.postCode.contains("SW1A 2BQ"))
      assert(unmarshalled.ppob.get.address.get.countryCode.contains("ES"))

      assert(unmarshalled.returnPeriod.get.stdReturnPeriod.contains("MM"))

    }

    "succeeds when optional business name is not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noBusinessName).as[CircumstanceDetails]

      // Only the business name should be un-populated
      assert(unmarshalled.businessName.isEmpty)

      // The rest of the object should be populated
      assert(unmarshalled.bankDetails.get.sortCode.contains("77****"))
      assert(unmarshalled.bankDetails.get.accountHolderName.contains("***********************"))
      assert(unmarshalled.bankDetails.get.bankAccountNumber.contains("****5274"))

      assert(unmarshalled.flatRateScheme.get.FRSCategory.contains("001"))
      assert(unmarshalled.flatRateScheme.get.FRSPercentage.contains(123.12))
      assert(unmarshalled.flatRateScheme.get.limitedCostTrader.contains(true))
      assert(unmarshalled.flatRateScheme.get.startDate.contains("2001-01-01"))

      assert(unmarshalled.ppob.get.address.get.line1.contains("VAT ADDR 1"))
      assert(unmarshalled.ppob.get.address.get.line2.contains("VAT ADDR 2"))
      assert(unmarshalled.ppob.get.address.get.line3.contains("VAT ADDR 3"))
      assert(unmarshalled.ppob.get.address.get.line4.contains("VAT ADDR 4"))
      assert(unmarshalled.ppob.get.address.get.line5.isEmpty)
      assert(unmarshalled.ppob.get.address.get.postCode.contains("SW1A 2BQ"))
      assert(unmarshalled.ppob.get.address.get.countryCode.contains("ES"))

      assert(unmarshalled.returnPeriod.get.stdReturnPeriod.contains("MM"))
    }

    "succeeds when optional Flat Rate Scheme values are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noFlatRateScheme).as[CircumstanceDetails]
      assert(unmarshalled.businessName == Some("Ancient Antiques"))

      // Only the flat rate scheme should be un-populated
      assert(unmarshalled.flatRateScheme.isEmpty)

      // The rest of the object should be populated
      assert(unmarshalled.businessName.contains("Ancient Antiques"))

      assert(unmarshalled.bankDetails.get.sortCode.contains("77****"))
      assert(unmarshalled.bankDetails.get.accountHolderName.contains("***********************"))
      assert(unmarshalled.bankDetails.get.bankAccountNumber.contains("****5274"))

      assert(unmarshalled.ppob.get.address.get.line1.contains("VAT ADDR 1"))
      assert(unmarshalled.ppob.get.address.get.line2.contains("VAT ADDR 2"))
      assert(unmarshalled.ppob.get.address.get.line3.contains("VAT ADDR 3"))
      assert(unmarshalled.ppob.get.address.get.line4.contains("VAT ADDR 4"))
      assert(unmarshalled.ppob.get.address.get.line5.isEmpty)
      assert(unmarshalled.ppob.get.address.get.postCode.contains("SW1A 2BQ"))
      assert(unmarshalled.ppob.get.address.get.countryCode.contains("ES"))

      assert(unmarshalled.returnPeriod.get.stdReturnPeriod.contains("MM"))


    }

    "succeeds when optional PPOB values are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noPPOB).as[CircumstanceDetails]

      // Only the ppop data should not be populated
      assert(unmarshalled.ppob.isEmpty)

      // The rest of the object should be populated
      assert(unmarshalled.businessName == Some("Ancient Antiques"))

      assert(unmarshalled.bankDetails.get.sortCode.contains("77****"))
      assert(unmarshalled.bankDetails.get.accountHolderName.contains("***********************"))
      assert(unmarshalled.bankDetails.get.bankAccountNumber.contains("****5274"))

      assert(unmarshalled.flatRateScheme.get.FRSCategory.contains("001"))
      assert(unmarshalled.flatRateScheme.get.FRSPercentage.contains(123.12))
      assert(unmarshalled.flatRateScheme.get.limitedCostTrader.contains(true))
      assert(unmarshalled.flatRateScheme.get.startDate.contains("2001-01-01"))

      assert(unmarshalled.returnPeriod.get.stdReturnPeriod.contains("MM"))
    }

    "succeeds when optional Return Period values are not supplied" in {
      val unmarshalled = Json.parse(CircumstanceDetailsTestConstants.noReturnPeriod).as[CircumstanceDetails]

      // Only the return period should not be populated
      assert(unmarshalled.returnPeriod.isEmpty)

      // The rest of the object should be populated
      assert(unmarshalled.businessName.contains("Ancient Antiques"))

      assert(unmarshalled.bankDetails.get.sortCode.contains("77****"))
      assert(unmarshalled.bankDetails.get.accountHolderName.contains("***********************"))
      assert(unmarshalled.bankDetails.get.bankAccountNumber.contains("****5274"))

      assert(unmarshalled.flatRateScheme.get.FRSCategory.contains("001"))
      assert(unmarshalled.flatRateScheme.get.FRSPercentage.contains(123.12))
      assert(unmarshalled.flatRateScheme.get.limitedCostTrader.contains(true))
      assert(unmarshalled.flatRateScheme.get.startDate.contains("2001-01-01"))

      assert(unmarshalled.ppob.get.address.get.line1.contains("VAT ADDR 1"))
      assert(unmarshalled.ppob.get.address.get.line2.contains("VAT ADDR 2"))
      assert(unmarshalled.ppob.get.address.get.line3.contains("VAT ADDR 3"))
      assert(unmarshalled.ppob.get.address.get.line4.contains("VAT ADDR 4"))
      assert(unmarshalled.ppob.get.address.get.line5.isEmpty)
      assert(unmarshalled.ppob.get.address.get.postCode.contains("SW1A 2BQ"))
      assert(unmarshalled.ppob.get.address.get.countryCode.contains("ES"))

    }

  }

}
