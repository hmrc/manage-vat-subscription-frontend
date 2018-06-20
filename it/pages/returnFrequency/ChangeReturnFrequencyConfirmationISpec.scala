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

package pages.returnFrequency

import helpers.IntegrationTestConstants.clientVRN
import pages.BasePageISpec
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._

class ChangeReturnFrequencyConfirmationISpec extends BasePageISpec {

  val path = "/confirmation-vat-return-dates"

  "Calling .show" when {

    def show(sessionVrn: Option[String] = None): WSResponse = get(path, formatSessionVrn(sessionVrn))

    "the user is a Principle Entity and not an Agent" should {

      "render the return frequency confirmation page" in {

        given.user.isAuthenticated

        When("I call to show the Confirm Return Frequency Page")
        val res = show()

        res should have(
          httpStatus(OK),
          elementText("#page-heading")(Messages("received_frequency.heading"))
        )
      }
    }

    "the user is an Agent" when {

      "a valid VRN is being stored" should {

        "render the return frequency confirmation page" in {

          given.agent.isSignedUpToAgentServices

          When("I call to show the Confirm Return Frequency Page")
          val res = show(Some(clientVRN))

          res should have(
            httpStatus(OK),
            elementText("#page-heading")(Messages("received_frequency.heading"))
          )
        }
      }
    }

    "the user is an Agent" when {

      "no VRN is being stored" should {

        "redirect to Enter Client VRN page" in {

          given.agent.isSignedUpToAgentServices

          When("I call to show the Confirm Return Frequency Page")
          val res = show()

          res should have(
            httpStatus(SEE_OTHER),
            redirectURI(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
          )
        }
      }
    }

    "the user is an Agent" when {

      "who is unauthorised" should {

        "render the unauthorised error page" in {

          given.agent.isNotSignedUpToAgentServices

          When("I call to show the Confirm Return Frequency Page")
          val res = show()

          res should have(
            httpStatus(FORBIDDEN),
            pageTitle(Messages("unauthorised.agent.title"))
          )
        }
      }
    }
  }
}