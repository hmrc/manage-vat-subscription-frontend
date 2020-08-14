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

package controllers.predicates

import assets.BaseTestConstants._
import assets.messages.AgentUnauthorisedPageMessages
import mocks.MockAuth
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.mvc.Results.Ok
import play.api.mvc.{Action, AnyContent}
import play.api.test.Helpers._

import scala.concurrent.Future

class AuthoriseAsAgentOnlySpec extends MockAuth {

  def target: Action[AnyContent] = {
    mockAgentOnlyAuthPredicate.async {
      _ => Future.successful(Ok("test"))
    }
  }

  "The AuthoriseAsAgentOnlySpec" when {

    "the user is an Agent" when {

      "the Agent is signed up to HMRC-AS-AGENT" should {

        "return 200" in {
          mockAgentAuthorised()
          val result = target(request)
          status(result) shouldBe Status.OK
          await(bodyOf(result)) shouldBe "test"
        }
      }

      "the Agent is not signed up to HMRC_AS_AGENT" should {

        lazy val result = target(request)

        "return Forbidden (403)" in {
          mockAgentWithoutEnrolment()
          status(result) shouldBe Status.FORBIDDEN
        }

        "render the Unauthorised Agent page" in {
          messages(Jsoup.parse(bodyOf(result)).select("h1").text) shouldBe AgentUnauthorisedPageMessages.pageHeading
        }
      }
    }

    "the user is not an Agent" should {

      "redirect to the Customer Details Home Page SEE_OTHER (303)" in {
        mockIndividualAuthorised()
        val result = target(request)
        status(result) shouldBe Status.SEE_OTHER
      }
    }

    "the user does not have an affinity group" should {

      "render an ISE (500)" in {
        mockUserWithoutAffinity()
        val result = target(request)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        messages(Jsoup.parse(bodyOf(result)).title) shouldBe internalServerErrorTitle
      }
    }

    "a user with no active session" should {

      lazy val result = await(target(request))

      "return 303 (Redirect)" in {
        mockMissingBearerToken()
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the session-timout view" in {
        redirectLocation(result) shouldBe Some(mockConfig.signInUrl)
      }
    }

    "a user with an authorisation exception" should {

      "return 500 (Internal Server Error)" in {
        mockUnauthorised()
        val result = await(target(request))
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
