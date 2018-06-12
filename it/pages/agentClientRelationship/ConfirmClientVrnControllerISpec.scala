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
import helpers.BaseIntegrationSpec
import helpers.IntegrationTestConstants._
import models.customerAddress.CountryCodes
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._
import stubs.VatSubscriptionStub

class ConfirmClientVrnControllerISpec extends BaseIntegrationSpec {

  "Calling the .show action" when {

    def target(sessionVrn: Option[String] = None): WSResponse =
      get("/confirm-client-vat-number", sessionVrn.fold(Map.empty[String, String])(x => Map(SessionKeys.CLIENT_VRN -> x)))

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a clients VRN is held in session cookie" when {

          "a success response is received for the Customer Details" should {

            "Render the Confirm Client View with the correction information" in {

              given.agent.isSignedUpToAgentServices

              And("I stub a successful response Individual response from ")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMax(individual))

              When("I call the Confirm Client VRN page with the clients VRN in the session")
              val res = target(Some(clientVRN))

              res should have(
                httpStatus(OK),
                //TODO: Future Story to be played to decide whether we show individual name instead of ""
                elementText("article p:nth-of-type(1)")(individual.firstName.get + " " + individual.lastName.get),
                elementText("article p:nth-of-type(2)")(clientVRN)
              )
            }
          }

          "an error response is received for the Customer Details" should {

            "Render the Internal Server Error view" in {

              given.agent.isSignedUpToAgentServices

              And("I stub an error response Individual response from ")
              VatSubscriptionStub.getClientDetailsError(clientVRN)

              When("I call the Confirm Client VRN page with the clients VRN in the session")
              val res = target(Some(clientVRN))

              res should have(
                httpStatus(INTERNAL_SERVER_ERROR),
                pageTitle(Messages("global.error.InternalServerError500.title"))
              )
            }
          }
        }

        "NO client VRN is held in the session cookie" should {

          "Redirect to the Select Client VRN view" in {

            given.agent.isSignedUpToAgentServices

            When("I call the Confirm Client VRN page with NO client VRN held in the session")
            val res = target(None)

            res should have(
              httpStatus(SEE_OTHER),
              redirectURI(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
            )
          }
        }
      }

      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {

        "Render the Internal Server Error page" in {

          given.agent.isNotSignedUpToAgentServices

          When("I call the Confirm Client VRN page with the clients VRN in the session")
          val res = target(Some(clientVRN))

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR),
            pageTitle(Messages("global.error.InternalServerError500.title"))
          )
        }
      }
    }

    "the user is a Principle Entity and not an Agent" should {

      "Redirect to the Select Client Details page (this bounces them to Customer Details after)" in {

        given.user.isAuthenticated

        When("I call the Confirm Client VRN page")
        val res = target()

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
        )
      }
    }

    "the user is timed out (not authenticated)" should {

      "Render the session timeout view" in {

        given.user.isNotAuthenticated

        When("I call the Confirm Client VRN page")
        val res = target(Some(clientVRN))

        res should have(
          httpStatus(UNAUTHORIZED),
          pageTitle(Messages("sessionTimeout.title"))
        )
      }
    }

    "the user is logged in without an Affinity Group" should {

      "Render the Internal Server Error view" in {

        given.user.isAuthenticated

        When("I call the Confirm Client VRN page")
        val res = target(Some(clientVRN))

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR),
          pageTitle(Messages("global.error.InternalServerError500.title"))
        )
      }
    }
  }
}
