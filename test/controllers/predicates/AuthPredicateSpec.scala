/*
 * Copyright 2021 HM Revenue & Customs
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
import assets.CircumstanceDetailsTestConstants.{customerInformationInsolvent, customerInformationModelMaxOrganisation}
import assets.CustomerAddressTestConstants.titleSuffixAgent
import assets.messages._
import common.SessionKeys
import mocks.MockAuth
import mocks.services.MockCustomerCircumstanceDetailsService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.mvc.Results.Ok
import play.api.mvc.{Action, AnyContent}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class AuthPredicateSpec extends MockAuth with MockCustomerCircumstanceDetailsService {

  def target: Action[AnyContent] = mockAuthPredicate.async {
    _ => Future.successful(Ok("test"))
  }

  "The AuthPredicateSpec" when {

    "the user does not have affinity group" should {

      "return ISE (500)" in {
        mockUserWithoutAffinity()
        val result = target(request)
        status(target(request)) shouldBe Status.INTERNAL_SERVER_ERROR
        messages(Jsoup.parse(contentAsString(result)).title) shouldBe internalServerErrorTitle
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

        "an authorised exception is returned from Auth" should {

          lazy val result = target(fakeRequestWithClientsVRN)

          "return Internal server error (500)" in {
            mockUnauthorised()
            status(result) shouldBe Status.INTERNAL_SERVER_ERROR
          }

          "render the Internal server error page" in {
            Jsoup.parse(contentAsString(result)).title shouldBe internalServerErrorTitle
          }
        }
      }

      "the Agent does NOT have an Active HMRC-AS-AGENT enrolment" should {

        lazy val result = target(fakeRequestWithClientsVRN)

        "return Forbidden" in {
          mockAgentWithoutEnrolment()
          status(result) shouldBe Status.FORBIDDEN
        }

        "render the Unauthorised Agent page" in {
          Jsoup.parse(contentAsString(result)).title shouldBe AgentUnauthorisedPageMessages.pageHeading + titleSuffixAgent
        }
      }
    }


    "the user is an Individual (Principle Entity)" when {

      "they have an active HMRC-MTD-VAT enrolment" when {

        "they have a value in session for their insolvency status" when {

          "the value is 'true' (insolvent user not continuing to trade)" should {

            "return Forbidden (403)" in {
              status(target(insolventRequest)) shouldBe Status.FORBIDDEN
            }
          }

          "the value is 'false' (user permitted to trade)" should {

            "return OK (200)" in {
              status(target(request)) shouldBe Status.OK
            }
          }
        }

        "they do not have a value in session for their insolvency status" when {

          "they are insolvent and not continuing to trade" should {

            lazy val result = {
              mockCustomerDetailsSuccess(customerInformationInsolvent)
              target(FakeRequest())
            }

            "return Forbidden (403)" in {
              status(result) shouldBe Status.FORBIDDEN
            }

            "add the insolvent flag to the session" in {
              session(result).get(SessionKeys.insolventWithoutAccessKey) shouldBe Some("true")
            }
          }

          "they are permitted to trade" should {

            lazy val result = {
              mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
              target(FakeRequest())
            }

            "return OK (200)" in {
              status(result) shouldBe Status.OK
            }

            "add the insolvent flag to the session" in {
              session(result).get(SessionKeys.insolventWithoutAccessKey) shouldBe Some("false")
            }
          }

          "there is an error returned from the customer information API" should {

            lazy val result = {
              mockCustomerDetailsError()
              target(FakeRequest())
            }

            "return Internal Server Error (500)" in {
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
            }
          }
        }
      }

      "they do NOT have an active HMRC-MTD-VAT enrolment" should {

        lazy val result = target(request)

        "return Forbidden (403)" in {
          mockIndividualWithoutEnrolment()
          status(result) shouldBe Status.FORBIDDEN
        }

        "render the Not Signed Up page" in {
          Jsoup.parse(contentAsString(result)).title shouldBe NotSignedUpPageMessages.title
        }
      }
    }
  }
}
