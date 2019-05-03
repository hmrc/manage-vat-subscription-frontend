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

package models.circumstanceInfo

import assets.CustomerDetailsTestConstants._
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class CustomerDetailsSpec extends UnitSpec {

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
          CustomerDetails(Some(firstName), None, None, None, None, overseasIndicator = false).userName shouldBe Some(s"$firstName")
        }
      }
      "LastName is present" should {
        "return 'Lastname'" in {
          CustomerDetails(None, Some(lastName), None, None, None, overseasIndicator = false).userName shouldBe Some(s"$lastName")
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

    "Deserialize from JSON" when {

      "all optional fields are populated for release 10" in {
        customerDetailsJsonMax.as[CustomerDetails](CustomerDetails.reads(true)) shouldBe customerDetailsMax
      }

      "all optional fields are populated for release 8" in {
        customerDetailsJsonMax.as[CustomerDetails](CustomerDetails.reads(false)) shouldBe customerDetailsMaxR8
      }

      "no optional fields are returned for release 10" in {
        customerDetailsJsonMin.as[CustomerDetails](CustomerDetails.reads(true)) shouldBe customerDetailsMin
      }

      "no optional fields are returned for release 8" in {
        customerDetailsJsonMin.as[CustomerDetails](CustomerDetails.reads(false)) shouldBe customerDetailsMinR8
      }

      "no optional fields are returned for release 8, and overseas is set to false, even if true in Json" in {
        customerDetailsJsonMinWithTrueOverseas.as[CustomerDetails](CustomerDetails.reads(false)) shouldBe customerDetailsMinR8
      }
    }

    "Serialize to JSON" when {

      "all optional fields are populated for release 10" in {
        Json.toJson(customerDetailsMax)(CustomerDetails.writes(true)) shouldBe customerDetailsJsonMax
      }

      "all optional fields are populated for release 8 (overseas indicator not written to json)" in {
        Json.toJson(customerDetailsMax)(CustomerDetails.writes(false)) shouldBe customerDetailsJsonMaxR8
      }

      "no optional fields are returned for release 10" in {
        Json.toJson(customerDetailsMin)(CustomerDetails.writes(true)) shouldBe customerDetailsJsonMin
      }

      "no optional fields are returned for release 8 (overseas indicator not written to json)" in {
        Json.toJson(customerDetailsMin)(CustomerDetails.writes(false)) shouldBe customerDetailsJsonMinR8
      }
    }
  }

}
