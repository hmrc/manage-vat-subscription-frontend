/*
 * Copyright 2024 HM Revenue & Customs
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

package models.viewModels

import assets.CircumstanceDetailsTestConstants.{customerInformationModelMaxOrganisation => customerInfo}
import utils.TestUtil

class AltChangeBusinessNameViewModelSpec extends TestUtil {

  ".trustBusinessNameViewModel" when {

    "the user is a principal entity" should {

      "return the correct view model" in {
        val model = AltChangeBusinessNameViewModel.trustBusinessNameViewModel(customerInfo)(mockConfig, user)
        val expected = AltChangeBusinessNameViewModel(
          customerInfo.customerDetails.organisationName.get,
          mockConfig.govUkTrustNameChangeUrl,
          "change_business_name.alternative.user1",
          "change_business_name.alternative.user.charity"
        )

        model shouldBe expected
      }
    }

    "the user is an agent" should {

      "return the correct view model" in {
        val model = AltChangeBusinessNameViewModel.trustBusinessNameViewModel(customerInfo)(mockConfig, agentUser)
        val expected = AltChangeBusinessNameViewModel(
          customerInfo.customerDetails.organisationName.get,
          mockConfig.govUkTrustNameChangeUrl,
          "change_business_name.alternative.agent1",
          "change_business_name.alternative.agent.charity"
        )

        model shouldBe expected
      }
    }
  }

  ".businessNameViewModel" when {

    "the user is a principal entity" should {

      "return the correct view model" in {
        val model = AltChangeBusinessNameViewModel.businessNameViewModel(customerInfo)(mockConfig, user)
        val expected = AltChangeBusinessNameViewModel(
          customerInfo.customerDetails.organisationName.get,
          mockConfig.govUkChangeToBusinessDetails,
          "change_business_name.alternative.user1",
          "change_business_name.alternative.user2"
        )

        model shouldBe expected
      }
    }

    "the user is an agent" should {

      "return the correct view model" in {
        val model = AltChangeBusinessNameViewModel.businessNameViewModel(customerInfo)(mockConfig, agentUser)
        val expected = AltChangeBusinessNameViewModel(
          customerInfo.customerDetails.organisationName.get,
          mockConfig.govUkChangeToBusinessDetails,
          "change_business_name.alternative.agent1",
          "change_business_name.alternative.agent2"
        )

        model shouldBe expected
      }
    }
  }
}
