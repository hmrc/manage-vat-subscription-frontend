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

package audit.models

import assets.BaseTestConstants._
import assets.CustomerDetailsTestConstants.orgName
import play.api.libs.json.Json
import utils.TestUtil

class GetClientBusinessNameAuditModelSpec extends TestUtil {

  val transactionName = "agent-authenticated-for-client-confirmation"
  val auditEvent = "AuthenticateAgentForClientConfirmation"

  "The AuthenticationAgentAuditModelSpec" should {

    lazy val testGetClientBusinessNameAuditModel = GetClientBusinessNameAuditModel(arn, vrn, orgName)

    s"Have the correct transaction name of '$transactionName'" in {
      testGetClientBusinessNameAuditModel.transactionName shouldBe transactionName
    }

    s"Have the correct audit event type of '$auditEvent'" in {
      testGetClientBusinessNameAuditModel.auditType shouldBe auditEvent
    }

    "Have the correct details for the audit event" in {
      testGetClientBusinessNameAuditModel.detail shouldBe Json.obj(
        "agentReferenceNumber" -> arn,
        "vrn" -> vrn,
        "businessName" -> orgName,
        "isAgent" -> true
      )
    }
  }
}
