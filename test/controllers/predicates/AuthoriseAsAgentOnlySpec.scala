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

class AuthoriseAsAgentOnlySpec extends MockAuth {

  def target: Action[AnyContent] = {
    mockAgentOnlyAuthPredicate.async{
      implicit request => Future.successful(Ok("test"))
    }
  }

  "The AuthoriseAsAgentOnlySpec" when {

    "the user is authorised" should {

      "return 200" in {
        mockAgentAuthorised()
        val result = target(fakeRequest)
        status(result) shouldBe Status.OK
        await(bodyOf(result)) shouldBe "test"
      }
    }

    "a user attempts to sign in without 'HMRC_AS_AGENT' enrolment" should {

      "return 403 (Forbidden)" in {
        mockUnauthorised
        val result = target(fakeRequest)
        status(result) shouldBe Status.FORBIDDEN
      }
    }

    "a user with no active session" should {

      "return 401 (Unauthorized)" in {
        mockMissingBearerToken
        val result = target(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }
}
