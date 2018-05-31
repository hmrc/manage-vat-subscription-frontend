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
import models.User
import play.api.http.Status
import play.api.mvc.Results.Ok
import play.api.mvc.{Action, AnyContent, Result}
import play.api.test.Helpers._

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

        "the Agent is authorised to act on behalf of the Client" should {

          "return OK (200)" in {
            mockAgentAuthorised()
            status(target(fakeRequestWithClientsVRN)) shouldBe Status.OK
          }
        }

        "the Agent is unauthorised to act on behalf of the Client" should {

          "return Forbidden (403)" in {
            mockUnauthorised()
            status(target(fakeRequestWithClientsVRN)) shouldBe Status.FORBIDDEN
          }
        }
      }

      "the Agent does NOT have an Active HMRC-AS-AGENT enrolment" should {

        "return Forbidden" in {
          mockAgentWithoutEnrolment()
          status(target(fakeRequestWithClientsVRN)) shouldBe Status.FORBIDDEN
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
