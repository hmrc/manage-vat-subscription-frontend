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

package models.customerInfo

import assets.BaseTestConstants._
import assets.CustomerDetailsTestConstants._
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class CustomerDetailsModelSpec extends UnitSpec {

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
          CustomerDetailsModel(Some(firstName), None, None, None).userName shouldBe Some(s"$firstName")
        }
      }
      "LastName is present" should {
        "return 'Lastname'" in {
          CustomerDetailsModel(None, Some(lastName), None, None).userName shouldBe Some(s"$lastName")
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
          customerDetailsMax.businessName shouldBe Some(s"$organisationName")
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

    "Deserialize from JSON" when {

      "all optional fields are populated" in {
        customerDetailsJsonMax.as[CustomerDetailsModel] shouldBe customerDetailsMax
      }

      "no optional fields are returned" in {
        customerDetailsJsonMin.as[CustomerDetailsModel] shouldBe customerDetailsMin
      }
    }

    "Serialize to JSON" when {

      "all optional fields are populated" in {
        Json.toJson(customerDetailsMax) shouldBe customerDetailsJsonMax
      }

      "no optional fields are returned" in {
        Json.toJson(customerDetailsMin) shouldBe customerDetailsJsonMin
      }
    }
  }

}
