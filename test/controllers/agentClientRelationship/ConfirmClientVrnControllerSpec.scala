/*
 * Copyright 2022 HM Revenue & Customs
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

package controllers.agentClientRelationship

import common.SessionKeys
import controllers.ControllerBaseSpec
import play.api.http.Status
import play.api.test.Helpers._

class ConfirmClientVrnControllerSpec extends ControllerBaseSpec {

  object TestConfirmClientVrnControllerSpec extends ConfirmClientVrnController(
    mockAuthAsAgentWithClient,
    mockCustomerDetailsService,
    serviceErrorHandler,
    mockAuditingService,
    mcc,
    mockConfig
  )

  "Calling the .changeClient action" when {

    "the user is an Agent" when {

      "the Agent is authorised and signed up to HMRC-AS-AGENT" when {

        "a Clients VRN is held in Session" should {

          lazy val result = TestConfirmClientVrnControllerSpec.changeClient(fakeRequestWithVrnAndReturnFreq)

          "return status redirect SEE_OTHER (303)" in {
            mockAgentAuthorised()
            status(result) shouldBe Status.SEE_OTHER
          }

          "redirect to the Select Your Client show action" in {
            redirectLocation(result) shouldBe Some(mockConfig.agentClientLookupUrl)
          }

          "have removed the Clients VRN from session" in {
            session(result).get(SessionKeys.mtdVatvcClientVrn) shouldBe None
          }

          "have removed the ReturnFrequency from session" in {
            session(result).get(SessionKeys.mtdVatvcNewReturnFrequency) shouldBe None
          }

          "have removed the CurrentReturnFrequency from session" in {
            session(result).get(SessionKeys.mtdVatvcCurrentReturnFrequency) shouldBe None
          }
        }
      }
    }

    "the user is not authenticated" should {

      "return 401 (Unauthorised)" in {
        mockMissingBearerToken()
        val result = TestConfirmClientVrnControllerSpec.changeClient(fakeRequestWithClientsVRN)
        status(result) shouldBe Status.SEE_OTHER
      }
    }
  }

}
