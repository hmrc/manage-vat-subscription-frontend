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
import models.circumstanceInfo._
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import models.customerAddress.AddressLookupOnRampModel
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import stubs.BusinessAddressStub

class BusinessAddressControllerISpec extends BasePageISpec {

  val session: Map[String, String] = Map(SessionKeys.CLIENT_VRN -> VRN)

  "Calling BusinessAddressController.initialiseJourney" when {

    def show(sessionVrn: String): WSResponse = get("/change-business-address", session)

    "A valid AddressLookupOnRampModel is returned from Address Lookup" should {

      "render the page Redirect to AddressLookup" in {

        given.user.isAuthenticated

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(ACCEPTED, AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Business Address change page")
        val res = show(VRN)

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("redirect/url")
        )
      }

      "render the page for a agent signed up to agent services" in {

        given.agent.isSignedUpToAgentServices

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(ACCEPTED,AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Customer Circumstances page")
        val res = show(VRN)

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("redirect/url")
        )
      }
    }

    "An Error Model is returned from Address Lookup" should {

      "show internal server error" in {
        given.agent.isSignedUpToAgentServices

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(INTERNAL_SERVER_ERROR,AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Customer Circumstances page")
        val res = show(VRN)

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }

    }
  }

  "Calling BusinessAddressController.callback" when {

    val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
      MTDfBMandated,
      CustomerDetails(None, None, None, None),
      None,
      PPOB(PPOBAddress("add line 1",None,None,None,None,None,"GB"),None,None),
      None,
      None,
      None
    )

    "the user is an individual" should {

      "render the ChangeAddressConfirmationPage page" in {

        given.user.isAuthenticated

        And("An address is returned address lookup service")
        BusinessAddressStub.getAddress(OK, Json.obj("lines" -> Json.arr("line1","line2")))

        And("a valid CircumstanceDetails model is returned")
        BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

        And("A response model is returned from the backend")
        BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

        When("I initiate a return frequency update journey")
        val res: WSResponse = get("/change-business-address/confirmation?id=111111111",session)

        res should have(
          httpStatus(OK),
          elementText("h1")("We have received the new business address"),
          isElementVisible("#change-client-text")(isVisible = false)
        )
      }
    }

    "the user is an agent" should {

      "render the ChangeAddressConfirmationPage page" in {

        given.agent.isSignedUpToAgentServices

        And("An address is returned address lookup service")
        BusinessAddressStub.getAddress(OK, Json.obj("lines" -> Json.arr("line1","line2")))

        And("a valid CircumstanceDetails model is returned")
        BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

        And("A response model is returned from the backend")
        BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

        When("I initiate a return frequency update journey")
        val res: WSResponse = get("/change-business-address/confirmation?id=111111111",session)

        res should have(
          httpStatus(OK),
          elementText("h1")("We have received the new business address"),
          isElementVisible("#change-client-text")(isVisible = true)
        )
      }
    }

    "the user is authenticated" should {

      "show internal server error" when {

        "vat-subscription does not return a SubscriptionUpdateResponseModel" in {

          given.agent.isSignedUpToAgentServices

          And("An address is returned address lookup service")
          BusinessAddressStub.getAddress(OK, Json.obj("lines" -> Json.arr("line1", "line2")))

          And("a valid CircumstanceDetails model is returned")
          BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

          And("A response model is returned from the backend")
          BusinessAddressStub.putSubscription(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR,"Bad times")))

          When("I initiate a return frequency update journey")
          val res: WSResponse = get("/change-business-address/confirmation?id=111111111", session)

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }

        "vat-subscription does not return a CircumstanceDetails model" in {

          given.agent.isSignedUpToAgentServices

          And("An address is returned address lookup service")
          BusinessAddressStub.getAddress(OK, Json.obj("lines" -> Json.arr("line1", "line2")))

          And("a valid CircumstanceDetails model is returned")
          BusinessAddressStub.getFullInformation(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR,"Bad times")))

          And("A response model is returned from the backend")
          BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

          When("I initiate a return frequency update journey")
          val res: WSResponse = get("/change-business-address/confirmation?id=111111111", session)

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }

        "Address Lookup returns an ErrorModel" in {

          given.agent.isSignedUpToAgentServices

          And("An address is returned address lookup service")
          BusinessAddressStub.getAddress(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR,"Bad times")))

          And("a valid CircumstanceDetails model is returned")
          BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

          And("A response model is returned from the backend")
          BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

          When("I initiate a return frequency update journey")
          val res: WSResponse = get("/change-business-address/confirmation?id=111111111", session)

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }
      }
    }
  }
}