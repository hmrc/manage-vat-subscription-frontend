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
import config.FrontendAppConfig
import helpers.IntegrationTestConstants.VRN
import models.circumstanceInfo._
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import models.customerAddress.AddressLookupOnRampModel
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.{OK, SEE_OTHER}
import stubs.BusinessAddressStub

class BusinessAddressControllerISpec extends BasePageISpec {

  val session: Map[String, String] = Map(SessionKeys.CLIENT_VRN -> VRN)
  lazy val mockAppConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  "Calling the .show action" when {

    def show(sessionVrn: String): WSResponse = get("/change-business-address", session)

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" should {

        "Render the Select a Client page" in {

          mockAppConfig.features.agentAccess(true)
          given.agent.isSignedUpToAgentServices

          When("I call the change business address page")
          val res = show(VRN)

          res should have(
            httpStatus(OK),
            elementText("h1")("Change the ‘principal place of business’")
          )
        }
      }
    }
  }

  "Calling BusinessAddressController.initialiseJourney" when {

    def show(sessionVrn: String): WSResponse = get("/change-business-address/ppob-handoff", session)

    "A valid AddressLookupOnRampModel is returned from Address Lookup" should {

      "render the page Redirect to AddressLookup" in {

        mockAppConfig.features.stubAddressLookup(false)

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

        mockAppConfig.features.stubAddressLookup(false)

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

        mockAppConfig.features.stubAddressLookup(false)

        given.agent.isSignedUpToAgentServices

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(BAD_REQUEST,AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Customer Circumstances page")
        val res = show(VRN)

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }

    }

    "Address Lookup stub is enabled" should {

      "redirect to stub StubAddressLookup page" in {

        mockAppConfig.features.stubAddressLookup(true)

        given.agent.isSignedUpToAgentServices

        BusinessAddressStub.postInitJourney(ACCEPTED, AddressLookupOnRampModel(
          testOnly.controllers.routes.StubAddressLookupController.show().url)
        )

        val res = show(VRN)

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI(testOnly.controllers.routes.StubAddressLookupController.show().url)
        )
      }
    }
  }

  "Calling BusinessAddressController.callback" when {

    val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
      mandationStatus = MTDfBMandated,
      customerDetails = CustomerDetails(None, None, None, None),
      flatRateScheme = None,
      ppob = PPOB(
        address = PPOBAddress(
          line1 = "add line 1",
          line2 = None,
          line3 = None,
          line4 = None,
          line5 = None,
          postCode = None,
          countryCode = "GB"),
        contactDetails = None,
        websiteAddress = None
      ),
      bankDetails = None,
      returnPeriod = None,
      deregistration = None,
      changeIndicators = None,
      pendingChanges = None,
      partyType = None
    )

    "the user is an individual" should {

      "redirect to the ChangeAddressConfirmationPage page" in {

        mockAppConfig.features.stubAddressLookup(false)

        given.user.isAuthenticated

        And("An address is returned address lookup service")
        BusinessAddressStub.getAddress(OK, Json.obj(
          "lines" -> Json.arr("line1","line2"),
          "country" -> Json.obj(
            "name" -> "United Kingdom",
            "code" -> "GB"
          )
        ))

        And("a valid CircumstanceDetails model is returned")
        BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

        And("A response model is returned from the backend")
        BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

        When("I initiate a return frequency update journey")
        val res: WSResponse = get("/change-business-address/callback?id=111111111",session)

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("/vat-through-software/account/change-business-address/confirmation/non-agent")
        )
      }
    }

    "the user is an agent" should {

      "redirect to the ChangeAddressConfirmationPage page" in {

        mockAppConfig.features.agentAccess(true)
        mockAppConfig.features.stubAddressLookup(false)

        given.agent.isSignedUpToAgentServices

        And("An address is returned address lookup service")
        BusinessAddressStub.getAddress(OK, Json.obj(
          "lines" -> Json.arr("line1","line2"),
          "country" -> Json.obj(
            "name" -> "United Kingdom",
            "code" -> "GB"
          )
        ))

        And("a valid CircumstanceDetails model is returned")
        BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

        And("A response model is returned from the backend")
        BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

        When("I initiate a return frequency update journey")
        val res: WSResponse = get("/change-business-address/callback?id=111111111",session)

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("/vat-through-software/account/change-business-address/confirmation/agent")
        )
      }
    }

    "the user is authenticated" should {

      "show internal server error" when {

        "vat-subscription does not return a SubscriptionUpdateResponseModel" in {

          given.agent.isSignedUpToAgentServices

          And("An address is returned address lookup service")
          BusinessAddressStub.getAddress(OK, Json.obj(
            "lines" -> Json.arr("line1", "line2"),
            "country" -> Json.obj(
              "name" -> "United Kingdom",
              "code" -> "GB"
            )
          ))

          And("a valid CircumstanceDetails model is returned")
          BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin))

          And("A response model is returned from the backend")
          BusinessAddressStub.putSubscription(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR,"Bad times")))

          When("I initiate a return frequency update journey")
          val res: WSResponse = get("/change-business-address/callback?id=111111111", session)

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }

        "vat-subscription does not return a CircumstanceDetails model" in {

          given.agent.isSignedUpToAgentServices

          And("An address is returned address lookup service")
          BusinessAddressStub.getAddress(OK, Json.obj(
            "lines" -> Json.arr("line1", "line2"),
            "country" -> Json.obj(
              "name" -> "United Kingdom",
              "code" -> "GB"
            )
          ))

          And("a valid CircumstanceDetails model is returned")
          BusinessAddressStub.getFullInformation(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR,"Bad times")))

          And("A response model is returned from the backend")
          BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

          When("I initiate a return frequency update journey")
          val res: WSResponse = get("/change-business-address/callback?id=111111111", session)

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
          val res: WSResponse = get("/change-business-address/callback?id=111111111", session)

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }
      }
    }
  }
}
