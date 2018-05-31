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

package controllers.predicates

import assets.messages.{AgentUnauthorisedForClientPageMessages, AgentUnauthorisedPageMessages, UnauthorisedPageMessages}
import mocks.MockAuth
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.mvc.Results.Ok
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.Future

class AuthPredicateSpec extends MockAuth {

  def target: Action[AnyContent] = mockAuthPredicate.async {
    implicit request => Future.successful(Ok("test"))
  }

  "The AuthPredicateSpec" when {

    "the user does not have affinity group" should {

      "return ISE (500)" in {
        mockUserWithoutAffinity()
        status(target(fakeRequest)) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user is an Agent" when {

      "the Agent has an Active HMRC-AS-AGENT enrolment" when {

        "a successful authorisation result is returned from Auth" should {

          "return OK (200)" in {
            mockAgentAuthorised()
            status(target(fakeRequestWithClientsVRN)) shouldBe Status.OK
          }
        }

        "an unauthorised result is returned from Auth" should {

          lazy val result = await(target(fakeRequestWithClientsVRN))

          "return Forbidden (403)" in {
            mockUnauthorised()
            status(result) shouldBe Status.FORBIDDEN
          }

          "render the Unauthorised page" in {
            Jsoup.parse(bodyOf(result)).title shouldBe UnauthorisedPageMessages.title
          }
        }
      }

      "the Agent does NOT have an Active HMRC-AS-AGENT enrolment" should {

        lazy val result = await(target(fakeRequestWithClientsVRN))

        "return Forbidden" in {
          mockAgentWithoutEnrolment()
          status(result) shouldBe Status.FORBIDDEN
        }

        "render the Unauthorised Agent page" in {
          Jsoup.parse(bodyOf(result)).title shouldBe AgentUnauthorisedPageMessages.title
        }
      }
    }
  }

  "the user is an Individual (Principle Entity)" when {

    "they have an active HMRC-MTD-VAT enrolment" should {

      "return OK (200)" in {
        mockIndividualAuthorised()
        status(target(fakeRequest)) shouldBe Status.OK
      }
    }

    "they do NOT have an active HMRC-MTD-VAT enrolment" should {

      "return Forbidden (403)" in {
        mockIndividualWithoutEnrolment()
        status(target(fakeRequest)) shouldBe Status.FORBIDDEN
      }
    }
  }
}
