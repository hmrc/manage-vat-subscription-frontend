/*
 * Copyright 2020 HM Revenue & Customs
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
import utils.TestUtil

class CircumstanceDetailsModelSpec extends TestUtil {

  "CircumstanceDetailsModel.pendingPPOBAddress" should{

    "return ppobAddress" in {
      customerInformationModelMaxOrganisation.ppobAddress shouldBe ppobAddressModelMax
    }

    "return Some data" when {

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

      "there are pending address changes" in {
        customerInformationNoPendingIndividual.pendingPPOBAddress shouldBe None
      }

      "there are pending bank changes" in {
        customerInformationNoPendingIndividual.pendingBankDetails shouldBe None
      }

      "there are pending return period changes" in {
        customerInformationNoPendingIndividual.pendingReturnPeriod shouldBe None
      }
    }
  }

  "calling .validPartyTypes" when {

    "the party type is valid" should {

      "return true" in {
        customerInformationModelMaxOrganisation.validPartyType shouldBe true
      }
    }

    "the party type is invalid" should {

      "return false" in {
        customerInformationModelMaxOrganisation.copy(partyType = Some("99")).validPartyType shouldBe false
      }
    }
  }

  "calling .nspItmpPartyType" when {

    "the partyType is Z1 or 1" should {

      "return 'true'" in {
        customerInformationModelMaxOrganisation.copy(partyType = Some("Z1")).nspItmpPartyType shouldBe true
      }
    }

    "the partyType is anything other than Z1 or 1" should {

      "return 'false'" in {
        customerInformationModelMaxOrganisation.nspItmpPartyType shouldBe false
      }
    }
  }

  "Deserialize from JSON" when {

    "succeeds when all registration fields are populated for release 10" in {
      customerInformationJsonMaxOrganisation.as[CircumstanceDetails](CircumstanceDetails.reads(true)) shouldBe customerInformationModelMaxOrganisation
    }

    "succeeds when optional values are not supplied for release 10" in {
      customerInformationJsonMin.as[CircumstanceDetails](CircumstanceDetails.reads(true)) shouldBe customerInformationModelMin
    }
  }

  "Serialize to JSON" when {

    "succeeds when all registration fields are populated for release 10" in {
      Json.toJson(customerInformationModelMaxOrganisation)(CircumstanceDetails.writes(true)) shouldBe customerInformationJsonMaxOrganisation
    }

    "succeeds when optional values are not supplied for release 10" in {
      Json.toJson(customerInformationModelMin)(CircumstanceDetails.writes(true)) shouldBe customerInformationJsonMin
    }
  }
}
