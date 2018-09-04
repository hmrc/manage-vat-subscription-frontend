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

package audit.models

import assets.BaseTestConstants._
import models.returnFrequency.{Jan, Monthly}
import play.api.libs.json.Json
import utils.TestUtil

class UpdateReturnFrequencyAuditModelSpec extends TestUtil {

  val transactionName = "change-vat-return-frequency"
  val auditEvent = "ChangeVatSubscriptionDetails"

  lazy val testUpdateReturnFrequencyAgent = UpdateReturnFrequencyAuditModel(agentUser, Jan, Monthly)
  lazy val testUpdateReturnFrequencyPrincipal = UpdateReturnFrequencyAuditModel(user, Jan, Monthly)

  "The UpdateReturnFrequencyAuditModel" should {

    s"Have the correct transaction name of '$transactionName'" in {
      testUpdateReturnFrequencyAgent.transactionName shouldBe transactionName
    }

    s"Have the correct audit event type of '$auditEvent'" in {
      testUpdateReturnFrequencyAgent.auditType shouldBe auditEvent
    }

    "For an Agent submitting the request" should {

      "Have the correct details for the audit event" in {
        testUpdateReturnFrequencyAgent.detail shouldBe Json.obj(
          "isAgent" -> true,
          "agentReferenceNumber" -> arn,
          "vrn" -> vrn,
          "currentReturnFrequency" -> "January, April, July and October",
          "requestedReturnFrequency" -> "Every month"
        )
      }
    }

    "For a User submitting the request" should {

      "Have the correct details for the audit event" in {
        testUpdateReturnFrequencyPrincipal.detail shouldBe Json.obj(
          "isAgent" -> false,
          "vrn" -> vrn,
          "currentReturnFrequency" -> "January, April, July and October",
          "requestedReturnFrequency" -> "Every month"
        )
      }
    }
  }
}
