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
import assets.PPOBAddressTestConstants.ppobAddressModelMax
import assets.BankDetailsTestConstants.bankDetailsModelMax
import models.returnFrequency.Mar
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class CircumstanceDetailsModelSpec extends UnitSpec {

  "CircumstanceDetailsModel.pendingPPOBAddress" should{

    "return Some data" when {

      "there is a current address" in {
        customerInformationModelMaxOrganisation.ppobAddress shouldBe Some(ppobAddressModelMax)
      }

      "there are pending address changes" in {
        customerInformationModelMaxOrganisation.pendingPPOBAddress shouldBe Some(ppobAddressModelMax)
      }

      "there are pending bank changes" in {
        customerInformationModelMaxOrganisation.pendingBankDetails shouldBe Some(bankDetailsModelMax)
      }

      "there are pending return period changes" in {
        customerInformationModelMaxOrganisation.pendingReturnPeriod shouldBe Some(Mar)
      }
    }

    "return None" when {

      "there is no current address" in {
        customerInformationModelMin.ppobAddress shouldBe None
      }

      "there are pending address changes" in {
        customerInformationNoPending.pendingPPOBAddress shouldBe None
      }

      "there are pending bank changes" in {
        customerInformationNoPending.pendingBankDetails shouldBe None
      }

      "there are pending return period changes" in {
        customerInformationNoPending.pendingReturnPeriod shouldBe None
      }
    }
  }

  "Deserialize from JSON" when {

    "succeeds when all registration fields are populated" in {
      customerInformationJsonMaxOrganisation.as[CircumstanceDetails] shouldBe customerInformationModelMaxOrganisation
    }


    "succeeds when optional values are not supplied" in {
      customerInformationJsonMin.as[CircumstanceDetails] shouldBe customerInformationModelMin
    }

  }

  "Serialize to JSON" when {

    "succeeds when all registration fields are populated" in {
      Json.toJson(customerInformationModelMaxOrganisation) shouldBe customerInformationJsonMaxOrganisation
    }


    "succeeds when optional values are not supplied" in {
      Json.toJson(customerInformationModelMin) shouldBe customerInformationJsonMin
    }
  }
}
