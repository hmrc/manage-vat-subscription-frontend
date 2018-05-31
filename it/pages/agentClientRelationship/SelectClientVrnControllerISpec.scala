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

import common.SessionKeys
import forms.ClientVrnForm
import helpers.{BaseIntegrationSpec, SessionCookieCrumbler}
import models.agentClientRelationship.ClientVrnModel
import play.api.i18n.Messages
import play.api.test.Helpers._
import helpers.IntegrationTestConstants._
import play.api.libs.ws.WSResponse

class SelectClientVrnControllerISpec extends BaseIntegrationSpec {

  "Calling the .show action" when {

    def show: WSResponse = get("/client-vat-number")

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" should {

        "return 200 OK" in {
          given.agent.isSignedUpToAgentServices
          show.status shouldBe OK
        }
      }

      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {

        "return ISE (500)" in {
          given.agent.isNotSignedUpToAgentServices
          show.status shouldBe INTERNAL_SERVER_ERROR
        }
      }
    }

    "the user is a Principle Entity and not an Agent" should {

      "have a redirect status SEE_OTHER (303)" in {
        given.user.isAuthenticated
        show.status shouldBe SEE_OTHER
      }

      "have the redirect location header set to the Customer Details home page" in {
        redirectLocation(show) shouldBe Some(controllers.routes.CustomerDetailsController.show().url)
      }
    }

    "the user is timed out (not authenticated)" should {

      "return status 401 - unauthorised" in {
        given.user.isNotAuthenticated
        show.status shouldBe UNAUTHORIZED
      }

      "render the session timeout view" in {
        document(show).title shouldBe Messages("sessionTimeout.title")
      }
    }

    "the user is logged in without an Affinity Group" should {

      "return status 500 (ISE)" in {
        given.user.noAffinityGroup
        show.status shouldBe INTERNAL_SERVER_ERROR
      }

      "render the Internal Server Error page" in {
        document(show).title shouldBe Messages("global.error.InternalServerError500.title")
      }
    }
  }


  "Calling the .submit action" when {

    def submit(data: ClientVrnModel): WSResponse = post("/client-vat-number")(toFormData(ClientVrnForm.form, data))

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a valid VRN is submitted" should {

          lazy val result = submit(ClientVrnModel(clientVRN))

          "return status redirect SEE_OTHER (303)" in {
            given.agent.isSignedUpToAgentServices
            result.status shouldBe SEE_OTHER
          }

          "redirect to the Confirm Client Controller" in {
            redirectLocation(result) shouldBe Some(controllers.agentClientRelationship.routes.ConfirmClientVrnController.show().url)
          }

          "has the clientVRN added to the session" in {
            SessionCookieCrumbler.getSessionMap(result).get(SessionKeys.CLIENT_VRN) shouldBe Some(clientVRN)
          }
        }

        "an invalid VRN is submitted" should {

          lazy val result = submit(ClientVrnModel("ABC"))

          "return status BAD_REQUEST (404)" in {
            given.agent.isSignedUpToAgentServices
            result.status shouldBe BAD_REQUEST
          }
        }
      }

      "the Agent is NOT signed up for HMRC-AS-AGENT (unauthorised)" when {

        "a valid VRN is submitted" should {

          lazy val result = submit(ClientVrnModel(clientVRN))

          "return status ISE (500)" in {
            given.agent.isNotSignedUpToAgentServices
            result.status shouldBe INTERNAL_SERVER_ERROR
          }

          "render the Internal Server Error page" in {
            document(result).title shouldBe Messages("global.error.InternalServerError500.title")
          }
        }
      }
    }

    "the user is a Principle Entity and not an Agent" should {

      lazy val result = submit(ClientVrnModel(clientVRN))

      "have a redirect status SEE_OTHER (303)" in {
        given.user.isAuthenticated
        result.status shouldBe SEE_OTHER
      }

      "have the redirect location header set to the Customer Details home page" in {
        redirectLocation(result) shouldBe Some(controllers.routes.CustomerDetailsController.show().url)
      }
    }

    "the user is timed out (not authenticated)" should {

      lazy val result = submit(ClientVrnModel(clientVRN))

      "return status 401 - unauthorised" in {
        given.user.isNotAuthenticated
        result.status shouldBe UNAUTHORIZED
      }

      "render the session timeout view" in {
        document(result).title shouldBe Messages("sessionTimeout.title")
      }
    }

    "the user is logged in without an Affinity Group" should {

      lazy val result = submit(ClientVrnModel(clientVRN))

      "return status 500 (ISE)" in {
        given.user.noAffinityGroup
        result.status shouldBe INTERNAL_SERVER_ERROR
      }

      "render the Internal Server Error page" in {
        document(result).title shouldBe Messages("global.error.InternalServerError500.title")
      }
    }
  }
}
