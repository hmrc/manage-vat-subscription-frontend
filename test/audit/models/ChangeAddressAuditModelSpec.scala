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

package audit.models

import assets.BaseTestConstants._
import assets.PPOBAddressTestConstants._
import assets.CustomerAddressTestConstants._
import assets.CircumstanceDetailsTestConstants.partyType
import play.api.libs.json.Json
import utils.TestUtil

class ChangeAddressAuditModelSpec extends TestUtil {

  val transactionName = "change-vat-business-address"
  val auditType = "ChangeVatSubscriptionDetails"

  lazy val changeAddressMaxModel = ChangeAddressAuditModel(agentUser, ppobAddressModelMax, customerAddressMax, Some(partyType))
  lazy val changeAddressMinModel = ChangeAddressAuditModel(user, ppobAddressModelMin, customerAddressMin, None)

  "The ChangeAddressAuditModel" should {

    "have the correct transactionName" in {
      changeAddressMaxModel.transactionName shouldBe transactionName
    }

    "have the correct auditType" in {
      changeAddressMaxModel.auditType shouldBe auditType
    }

    "For an individual" when {

      "the individual only has the required fields" in {
        changeAddressMinModel.detail shouldBe Json.obj(
          "isAgent" -> false,
          "vrn" -> vrn,
          "currentBusinessAddress" -> Json.obj(
            "line1" -> ppobAddressModelMin.line1,
            "countryCode" -> ppobAddressModelMin.countryCode
          ),
          "requestedBusinessAddress" -> Json.obj(
            "line1" -> customerAddressMin.line1,
            "line2" -> customerAddressMin.line2,
            "countryCode" -> customerAddressMin.countryCode
          )
        )
      }
    }

    "For an agent" when {

      "all fields are filled" in {
        changeAddressMaxModel.detail shouldBe Json.obj(
          "isAgent" -> true,
          "agentReferenceNumber" -> arn,
          "vrn" -> vrn,
          "currentBusinessAddress" -> Json.obj(
            "line1" -> ppobAddressModelMax.line1,
            "line2" -> ppobAddressModelMax.line2,
            "line3" -> ppobAddressModelMax.line3,
            "line4" -> ppobAddressModelMax.line4,
            "line5" -> ppobAddressModelMax.line5,
            "postCode" -> ppobAddressModelMax.postCode,
            "countryCode" -> ppobAddressModelMax.countryCode
          ),
          "requestedBusinessAddress" -> Json.obj(
            "line1" -> customerAddressMax.line1,
            "line2" -> customerAddressMax.line2,
            "line3" -> customerAddressMax.line3,
            "line4" -> customerAddressMax.line4,
            "postCode" -> customerAddressMax.postcode,
            "countryCode" -> customerAddressMax.countryCode
          ),
          "partyType" -> partyType
        )
      }
    }

  }

}
