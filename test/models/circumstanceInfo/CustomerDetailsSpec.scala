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

import assets.CustomerDetailsTestConstants._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.Json

class CustomerDetailsSpec extends AnyWordSpecLike with Matchers {

  "CustomerDetailsModel" when {

    ".isInd" should {
      "Return True when the user is an Individual" in {
        individual.isInd shouldBe true
      }
      "Return False when the user is NOT an Individual" in {
        organisation.isInd shouldBe false
      }
    }

    "calling .isOrg" should {
      "Return True when the user is an Organisation" in {
        organisation.isOrg shouldBe true
      }
      "Return False when the user is NOT an Organisation" in {
        individual.isOrg shouldBe false
      }
    }

    "calling .username" when {
      "FirstName and Lastname are present" should {
        "return 'Firstname Lastname'" in {
          individual.userName shouldBe Some(s"$firstName $lastName")
        }
      }
      "FirstName is present" should {
        "return 'Firstname'" in {
          customerDetailsMin.copy(firstName = Some(firstName)).userName shouldBe Some(s"$firstName")
        }
      }
      "LastName is present" should {
        "return 'Lastname'" in {
          customerDetailsMin.copy(lastName = Some(lastName)).userName shouldBe Some(s"$lastName")
        }
      }
      "No names are present" should {
        "return None" in {
          customerDetailsMin.userName shouldBe None
        }
      }
    }

    "calling .businessName" when {
      "Organisation Name is present" should {
        "return Organisation name" in {
          customerDetailsMax.businessName shouldBe Some(s"$orgName")
        }
      }
      "username is present and org name is missing" should {
        "return username" in {
          individual.businessName shouldBe Some(s"$firstName $lastName")
        }
      }
      "username and organisation anme are missing" should {
        "return username" in {
          customerDetailsMin.businessName shouldBe None
        }
      }
    }

    "calling .clientName" when {
      "Trading name is present" should {
        "return Trading Name" in {
          customerDetailsMax.clientName shouldBe Some(tradingName)
        }
      }
      "Trading name is not present" should {
        "return Business Name" in {
          individual.clientName shouldBe Some(s"$firstName $lastName")
        }
      }
      "Trading name and businessName are not present" should {
        "return None" in {
          customerDetailsMin.businessName shouldBe None
        }
      }
    }

    "calling .isInsolventWithoutAccess" when {

      "the user is insolvent and not continuing to trade" should {

        "return true for a user with no insolvency type" in {
          customerDetailsInsolvent.isInsolventWithoutAccess shouldBe true
        }

        "return true for a user with a blocked insolvency type" in {
          customerDetailsInsolvent.copy(insolvencyType = Some("09")).isInsolventWithoutAccess shouldBe true
        }

        "return false for a user with no insolvency type and no continueToTrade flag" in {
          customerDetailsNotInsolventNotCTT.isInsolventWithoutAccess shouldBe false
        }

        "return false for a user with an allowed insolvency type" in {
          customerDetailsInsolvent.copy(insolvencyType = Some("12")).isInsolventWithoutAccess shouldBe false
        }
      }

      "the user is insolvent but is continuing to trade" should {

        "return false for a user with no insolvency type" in {
          customerDetailsInsolvent.copy(continueToTrade = Some(true)).isInsolventWithoutAccess shouldBe false
        }

        "return true for a user with a blocked insolvency type" in {
          customerDetailsInsolvent.copy(continueToTrade = Some(true), insolvencyType = Some("10")).isInsolventWithoutAccess shouldBe true
        }

        "return false for a user with an allowed insolvency type" in {
          customerDetailsInsolvent.copy(continueToTrade = Some(true), insolvencyType = Some("14")).isInsolventWithoutAccess shouldBe false
        }
      }

      "return false when the user is not insolvent, regardless of the continueToTrade flag" in {
        customerDetailsMax.isInsolventWithoutAccess shouldBe false
        customerDetailsMax.copy(continueToTrade = Some(false)).isInsolventWithoutAccess shouldBe false
        customerDetailsMax.copy(continueToTrade = None).isInsolventWithoutAccess shouldBe false
      }
    }

    "Deserialize from JSON" when {

      "all optional fields are populated" in {
        customerDetailsJsonMax.as[CustomerDetails](CustomerDetails.reads) shouldBe customerDetailsMax
      }

      "no optional fields are returned" in {
        customerDetailsJsonMin.as[CustomerDetails](CustomerDetails.reads) shouldBe customerDetailsMin
      }
    }

    "Serialize to JSON" when {

      "all optional fields are populated" in {
        Json.toJson(customerDetailsMax)(CustomerDetails.writes) shouldBe customerDetailsJsonMax
      }

      "no optional fields are returned" in {
        Json.toJson(customerDetailsMin)(CustomerDetails.writes) shouldBe customerDetailsJsonMin
      }
    }
  }
}
