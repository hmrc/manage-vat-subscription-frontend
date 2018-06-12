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
import helpers.IntegrationTestConstants._
import helpers.SessionCookieCrumbler
import models.agentClientRelationship.ClientVrnModel
import pages.BasePageISpec
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._

class SelectClientVrnControllerISpec extends BasePageISpec {

  val path = "/client-vat-number"

  "Calling the .show action" when {

    def show: WSResponse = get(path)

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" should {

        "Render the Select a Client page" in {

          given.agent.isSignedUpToAgentServices

          When("I call the show Select Client VRN page")
          val res = show

          res should have(
            httpStatus(OK),
            elementText("h1")("What is your client's VAT number?"),
            isElementVisible("#vrn")(isVisible = true)
          )
        }
      }

      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {

        "Render the Sign up for Agent Services unauthorised view" in {

          given.agent.isNotSignedUpToAgentServices

          When("I call the show Select Client VRN page")
          val res = show

          res should have(
            httpStatus(FORBIDDEN),
            pageTitle(Messages("unauthorised.agent.title"))
          )
        }
      }
    }

    "the user is a Principle Entity and not an Agent" should {

      "Redirect to the Customer Details page" in {

        given.user.isAuthenticated

        When("I call the show Select Client VRN page")
        val res = show

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI(controllers.routes.CustomerCircumstanceDetailsController.show().url)
        )
      }
    }


  }


  "Calling the .submit action" when {

    val validData = ClientVrnModel(clientVRN)
    def submit(data: ClientVrnModel): WSResponse = post(path)(toFormData(ClientVrnForm.form, data))

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a valid VRN is submitted" should {

          lazy val result = submit(validData)

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

          lazy val result = submit(validData)

          "return status 403 (Forbidden)" in {
            given.agent.isNotSignedUpToAgentServices
            result.status shouldBe FORBIDDEN
          }

          "render the Agent Unauthorised page" in {
            document(result).title shouldBe Messages("unauthorised.agent.title")
          }
        }
      }
    }

    "the user is a Principle Entity and not an Agent" should {

      lazy val result = submit(validData)

      "have a redirect status SEE_OTHER (303)" in {
        given.user.isAuthenticated
        result.status shouldBe SEE_OTHER
      }

      "have the redirect location header set to the Customer Details home page" in {
        redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.show().url)
      }
    }

    postAuthenticationTests(path)(toFormData(ClientVrnForm.form, validData))
  }
}
