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

import mocks.MockAuth
import play.api.http.Status
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.Results.Ok

import scala.concurrent.Future

class AuthoriseAsAgentSpec extends MockAuth {

  "The AuthoriseAgentPredicate" when {

    def target: Action[AnyContent] = {
      mockAuthPredicate.async{
        implicit request => Future.successful(Ok("test"))
      }
    }

    "the agent is authorised" should {

      "return 200" in {
        mockAgentAuthorised()
        val result = target(fakeRequest)
        status(result) shouldBe Status.OK
      }
    }

    "an agent attempts to sign in without 'HMRC_AS_AGENT' enrolment" should {

      "throw an ISE (500)" in {
        mockAgentWithoutEnrolment()
        val result = target(fakeRequest)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "an agent attempts to sign in without an affinity group" should {

      "throw an ISE (500)" in {
        mockAgentWithoutAffinity()
        val result = target(fakeRequest)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the agent is not authenticated" should {

      "return 401 (Unauthorised)" in {
        mockUnauthenticated
        val result = target(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }

    "the agent is not authorised" should {

      "return 403 (Forbidden)" in {
        mockUnauthorised
        val result = target(fakeRequest)
        status(result) shouldBe Status.FORBIDDEN
      }
    }
  }

}