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

package audit.models

import assets.BaseTestConstants._
import play.api.libs.json.Json
import utils.TestUtil

class ContactPreferenceAuditModelSpec extends TestUtil {

  val transactionName = "contact-preference"
  val auditType = "ContactPreference"
  val preference = "DIGTIAL"
  val action = "ChangePPOBContactPreference"

  "The ContactPreferenceAuditModel" should {

    lazy val testGetContactPreferenceAuditModel = ContactPreferenceAuditModel(vrn, preference, action)

    s"Have the correct transaction name of '$transactionName'" in {
      testGetContactPreferenceAuditModel.transactionName shouldBe transactionName
    }

    s"Have the correct audit event type of '$auditType'" in {
      testGetContactPreferenceAuditModel.auditType shouldBe auditType
    }

    "Have the correct details for the audit event" in {
      testGetContactPreferenceAuditModel.detail shouldBe Json.obj(
        "vrn" -> vrn,
        "contactPreference" -> preference,
        "action" -> action
      )
    }
  }
}
