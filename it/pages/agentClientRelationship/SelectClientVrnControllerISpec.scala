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

package pages.agentClientRelationship

import helpers.BaseIntegrationSpec
import play.api.http.Status._

class SelectClientVrnControllerISpec extends BaseIntegrationSpec {

  "Calling the .show action" when {

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" should {

        "return 200 OK" in {
          given.agent.isAgentAuthorised
          val result = get("/client-vat-number")
          result.status shouldBe OK
        }
      }

      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {

        "return 403 FORBIDDEN" in {
          given.agent.isAgentUnauthorised
          val result = get("/client-vat-number")
          result.status shouldBe FORBIDDEN
        }
      }
    }
  }
}
