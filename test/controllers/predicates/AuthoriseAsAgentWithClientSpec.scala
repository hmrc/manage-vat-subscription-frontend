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

package controllers.predicates

import mocks.MockAuth
import play.api.http.Status
import play.api.mvc.Results.Ok
import play.api.mvc.{Action, AnyContent}
import play.api.test.Helpers._

import scala.concurrent.Future

class AuthoriseAsAgentWithClientSpec extends MockAuth {

  def target: Action[AnyContent] =
    mockAuthAsAgentWithClient.async {
      implicit user =>
        Future.successful(Ok(s"test ${user.vrn}"))
    }

  "The AuthoriseAsAgentWithClientSpec" when {

    "the agent is authorised with a Client VRN in session" should {

      "return 200" in {
        mockAgentAuthorised()
        val result = target(fakeRequestWithClientsVRN)
        status(result) shouldBe Status.OK
      }
    }

    "an agent has not selected their Client (No Client VRN in session)" should {

      lazy val result = target(request)

      "return 303 (SEE_OTHER) redirect" in {
        mockAgentAuthorised()
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the Select Your Client controller" in {
        redirectLocation(result) shouldBe Some(mockConfig.agentClientLookupUrl)
      }
    }

    "the agent is not authenticated" should {

      lazy val result = target(fakeRequestWithClientsVRN)

      "return 303 (Redirect)" in {
        mockMissingBearerToken()
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the session-timout view" in {
        redirectLocation(result) shouldBe Some(mockConfig.signInUrl)
      }
    }

    "the agent is not authorised" should {

      lazy val result = target(fakeRequestWithClientsVRN)

      "return 303 (SEE_OTHER)" in {
        mockUnauthorised()
        status(result) shouldBe Status.SEE_OTHER
      }

      s"redirect location to ${mockConfig.agentClientUnauthorisedUrl}" in {
        redirectLocation(result) shouldBe Some(mockConfig.agentClientUnauthorisedUrl)
      }
    }

    "the user has no affinity group" should {

      lazy val result = target(fakeRequestWithClientsVRN)

      "return 500 (INTERNAL_SERVER_ERROR)" in {
        mockAgentWithoutAffinity()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
