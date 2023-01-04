/*
 * Copyright 2023 HM Revenue & Customs
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
import utils.TestUtil

class CircumstanceDetailsSpec extends TestUtil {

  "CircumstanceDetails" should {

    "return ppobAddress" in {
      customerInformationModelMaxOrganisation.ppobAddress shouldBe ppobAddressModelMax
    }

    "return the correct data" when {

      "there are pending address changes" in {
        customerInformationModelMaxOrganisation.pendingPPOBAddress shouldBe Some(ppobAddressModelMax)
      }

      "there are pending bank changes" in {
        customerInformationModelMaxOrganisation.pendingBankDetails shouldBe Some(bankDetailsModelMax)
      }

      "there are pending return period changes" in {
        customerInformationModelMaxOrganisation.hasPendingReturnPeriod shouldBe true
        customerInformationModelMaxOrganisation.pendingReturnPeriod shouldBe Some(Mar)
      }

      "there are no pending address changes" in {
        customerInformationNoPendingIndividual.pendingPPOBAddress shouldBe None
      }

      "there are no pending bank changes" in {
        customerInformationNoPendingIndividual.pendingBankDetails shouldBe None
      }

      "there are no pending return period changes" in {
        customerInformationNoPendingIndividual.hasPendingReturnPeriod shouldBe false
        customerInformationNoPendingIndividual.pendingReturnPeriod shouldBe None
      }
    }
  }

  "calling .validPartyType" when {

    "the party type is listed in the appConfig sequence 'partyTypes'" should {

      "return true" in {
        mockConfig.partyTypes.foreach { party =>
          customerInformationModelMaxOrganisation.copy(partyType = Some(party)).validPartyType shouldBe true
        }
      }
    }

    "the party type is not listed in the appConfig sequence 'partyTypes'" should {

      "return false" in {
        customerInformationModelMaxOrganisation.copy(partyType = Some("99")).validPartyType shouldBe false
      }
    }

    "there is no party type" should {

      "return 'false'" in {
        customerInformationModelMaxOrganisation.copy(partyType = None).validPartyType shouldBe false
      }
    }
  }

  "calling .nspItmpPartyType" when {

    "the partyType is listed in the appConfig sequence 'partyTypesNspItmp'" should {

      "return 'true'" in {
        mockConfig.partyTypesNspItmpOrSAMastered.foreach { party =>
          customerInformationModelMaxOrganisation.copy(partyType = Some(party)).nspItmpPartyType shouldBe true
        }
      }
    }

    "the partyType is not listed in the appConfig sequence 'partyTypesNspItmp'" should {

      "return 'false'" in {
        customerInformationModelMaxOrganisation.nspItmpPartyType shouldBe false
      }
    }

    "there is no party type" should {

      "return 'false'" in {
        customerInformationModelMaxOrganisation.copy(partyType = None).nspItmpPartyType shouldBe false
      }
    }
  }

  "calling .trustPartyType" when {

    "the partyType is listed in the appConfig sequence 'partyTypesTrusts'" should {

      "return true" in {
        mockConfig.partyTypesTrusts.foreach { party =>
          customerInformationModelMaxOrganisation.copy(partyType = Some(party)).trustPartyType shouldBe true
        }
      }
    }

    "the partyType is not listed in the appConfig sequence 'partyTypesTrusts'" should {

      "return false" in {
        customerInformationModelMaxOrganisation.trustPartyType shouldBe false
      }
    }

    "there is no partyType" should {

      "return false" in {
        customerInformationModelMaxOrganisation.copy(partyType = None).trustPartyType shouldBe false
      }
    }
  }

  "Deserialize from JSON" when {

    "succeeds when all registration fields are populated" in {
      customerInformationJsonMaxOrganisation.as[CircumstanceDetails](CircumstanceDetails.reads) shouldBe customerInformationModelMaxOrganisation
    }

    "succeeds when optional values are not supplied" in {
      customerInformationJsonMin.as[CircumstanceDetails](CircumstanceDetails.reads) shouldBe customerInformationModelMin
    }
  }
}
