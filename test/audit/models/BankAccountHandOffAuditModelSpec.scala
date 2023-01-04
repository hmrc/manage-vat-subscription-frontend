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

package audit.models

import play.api.libs.json.Json
import utils.TestUtil

class BankAccountHandOffAuditModelSpec extends TestUtil {

  val transactionName: String = "start-change-repayment-bank-account-journey"
  val auditType: String = "StartChangeOfRepaymentBankAccount"

  lazy val agentHandOff = BankAccountHandOffAuditModel(agentUser, "/redirect-url")
  lazy val userHandOff = BankAccountHandOffAuditModel(user, "/redirect-url")

  "BankAccountHandOffAuditModel" should {

    s"have the correct transaction name $transactionName" in {
      agentHandOff.transactionName shouldBe transactionName
    }

    s"have the correct audit type $auditType" in {
      agentHandOff.auditType shouldBe auditType
    }

    "user is an Agent" should {

      "have the correct details for the audit event" in {
        agentHandOff.detail shouldBe Json.obj(
          "isAgent" -> true,
          "agentReferenceNumber" -> agentHandOff.user.arn.get,
          "vrn" -> agentHandOff.user.vrn,
          "repaymentsRedirectUrl" -> agentHandOff.repaymentsRedirectUrl
        )
      }
    }

    "user is a non-Agent" should {

      "have the correct details for the audit event" in {
        userHandOff.detail shouldBe Json.obj(
          "isAgent" -> false,
          "vrn" -> userHandOff.user.vrn,
          "repaymentsRedirectUrl" -> userHandOff.repaymentsRedirectUrl
        )
      }
    }
  }
}
