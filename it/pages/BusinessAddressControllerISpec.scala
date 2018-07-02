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

package pages

import common.SessionKeys
import helpers.IntegrationTestConstants.VRN
import models.core.SubscriptionUpdateResponseModel
import models.customerAddress.AddressModel
import play.api.http.Status.{INTERNAL_SERVER_ERROR, SEE_OTHER, OK}
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.FORBIDDEN
import stubs.BusinessAddressStub

class BusinessAddressControllerISpec extends BasePageISpec {

  val path = "/change-business-address"
  val session: Map[String, String] = Map(SessionKeys.CLIENT_VRN -> VRN)

  "Calling BusinessAddressController.initialiseJourney" when {

    def show(sessionVrn: String): WSResponse = get(path, session)

    "A valid AddressLookupOnRampModel is returned" should {

      "render the page Redirect to AddressLookup" in {
        given.user.isAuthenticated

        When("I call to show the Customer Circumstances page")
        val res = show(VRN)

        res should have(
          httpStatus(SEE_OTHER)
        )
      }

      "render the page for a agent signed up to agent services" in {
        given.agent.isSignedUpToAgentServices

        When("I call to show the Customer Circumstances page")
        val res = show(VRN)

        res should have(
          httpStatus(SEE_OTHER)
        )
      }
    }
  }

  "Calling BusinessAddressController.callback" when {

    "the user is an individual" should {

      "Return a valid Address" should {

        "render the ChangeReturnFrequencyConfirmation page" in {
          given.user.isAuthenticated

          BusinessAddressStub.getAddress(AddressModel("line1","line2"))

          BusinessAddressStub.putSubscriptionSuccess(SubscriptionUpdateResponseModel("Good times"))

          When("I initiate a return frequency update journey")
          val res: WSResponse = get("/change-business-address/confirmation?id=111111111",session)

          res should have(
            httpStatus(SEE_OTHER)
//            redirectURI(controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show().url)
          )
        }
      }
//      "An invalid model is posted" should {
//
//        "Render the Internal Server Error page" in {
//          given.user.isAuthenticated
//          And("I stub an error response from the Payments service")
//          BusinessAddressStub.putSubscriptionError()
//
//          When("I initiate a return frequency update journey")
//          val res: WSResponse = get("/change-business-address/confirmation",session)
//
//          res should have(
//            httpStatus(INTERNAL_SERVER_ERROR)
//          )
//        }
//      }
    }

//    "the user is an agent" should {
//
//      "if a valid ReturnPeriod is returned" should {
//
//        "render the ChangeReturnFrequencyConfirmation page" in {
//          given.agent.isSignedUpToAgentServices
//          And("I stub a successful response from the Payments service")
//          BusinessAddressStub.putSubscriptionSuccess(SubscriptionUpdateResponseModel("Good times"))
//
//          When("I initiate a return frequency update journey")
//          val res: WSResponse = get("/change-business-address/confirmation",session)
//
//          res should have(
//            httpStatus(SEE_OTHER),
//            redirectURI(controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show().url)
//          )
//        }
//      }
//      "if an invalid model is posted" should {
//
//        "Render the Internal Server Error page" in {
//          given.agent.isSignedUpToAgentServices
//          And("I stub an error response from the Payments service")
//          BusinessAddressStub.putSubscriptionError()
//
//          When("I initiate a return frequency update journey")
//          val res: WSResponse = get("/change-business-address/confirmation",session)
//
//          res should have(
//            httpStatus(INTERNAL_SERVER_ERROR)
//          )
//        }
//      }
//
//      "the Agent is NOT signed up for HMRC-AS-AGENT (unauthorised)" when {
//
//        "render the Agent Unauthorised page" in {
//
//          given.agent.isNotSignedUpToAgentServices
//          And("I stub a successful response from the Payments service")
//          BusinessAddressStub.putSubscriptionSuccess(SubscriptionUpdateResponseModel("Good times"))
//
//          When("I submit the Client VRN page with valid data")
//          val res: WSResponse = get("/change-business-address/confirmation",session)
//
//          res should have(
//            httpStatus(FORBIDDEN),
//            pageTitle(Messages("unauthorised.agent.title"))
//          )
//        }
//      }
//    }
  }

}
