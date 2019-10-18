/*
 * Copyright 2019 HM Revenue & Customs
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

import assets.BusinessAddressITConstants._
import common.SessionKeys
import config.FrontendAppConfig
import helpers.IntegrationTestConstants.{VRN, customerCircumstancesDetailsMin, organisation}
import models.circumstanceInfo._
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import models.customerAddress.AddressLookupOnRampModel
import play.api.http.Status._
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.{OK, SEE_OTHER}
import stubs.{BusinessAddressStub, ContactPreferencesStub, VatSubscriptionStub}

class BusinessAddressControllerISpec extends BasePageISpec {

  val session: Map[String, String] = Map(SessionKeys.CLIENT_VRN -> VRN)
  val sessionVrnAndWelsh: Map[String, String] = Map(SessionKeys.CLIENT_VRN -> VRN, "PLAY_LANG" -> "cy")
  lazy val mockAppConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  override def beforeEach() {
    mockAppConfig.features.useContactPreferences(false)
    mockAppConfig.features.useNewAddressLookupFeature(false)
    super.beforeEach()
  }

  override def afterEach() {
    mockAppConfig.features.useContactPreferences(false)
    super.afterEach()
  }

  "Calling the .show action" when {

    def show(): WSResponse = get("/change-business-address", session)

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" should {

        "Render the Select a Client page" in {

          given.agent.isSignedUpToAgentServices

          And("A successful response with minimum details is returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

          When("I call the change business address page")
          val res = show()

          res should have(
            httpStatus(OK),
            elementText("h1")("Change the ‘principal place of business’")
          )
        }
      }
    }
  }

  "Calling BusinessAddressController.initialiseJourney" when {

    def show(additionalCookies: Map[String, String] = session): WSResponse = get("/change-business-address/ppob-handoff", additionalCookies)

    "A valid AddressLookupOnRampModel is returned from Address Lookup" should {

      "render the page Redirect to AddressLookup" in {

        mockAppConfig.features.stubAddressLookup(false)

        given.user.isAuthenticated

        And("A successful response with minimum details is returned for an Organisation")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(ACCEPTED, AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Business Address change page")
        val res = show()

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("redirect/url")
        )
      }

      "render the page for a agent signed up to agent services" in {

        mockAppConfig.features.stubAddressLookup(false)

        given.agent.isSignedUpToAgentServices

        And("A successful response with minimum details is returned for an Organisation")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(ACCEPTED, AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Customer Circumstances page")
        val res = show()

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("redirect/url")
        )
      }
    }

    "Address-lookup-frontend Version2 is enabled" when {

      "the local language is set to Welsh" should {

        "handoff to address lookup frontend with the correct english and welsh messages" in {

          mockAppConfig.features.useNewAddressLookupFeature(true)
          mockAppConfig.features.stubAddressLookup(false)

          given.user.isAuthenticated

          And("A successful response with minimum details is returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

          And("a url is returned from the Address Lookup Service")
          BusinessAddressStub.postInitV2Journey(ACCEPTED, AddressLookupOnRampModel("redirect/url"))

          When("I call to show the Business Address change page")
          val res = show(sessionVrnAndWelsh)


          val alfBody = Json.stringify(Json.obj(
            "labels" -> Json.obj(
              "en" -> Json.obj(
                "lookupPageLabels" -> Json.obj(
                  "title" -> s"$startHeading"
                )
              ),
              "cy" -> Json.obj(
                "lookupPageLabels" -> Json.obj(
                  "title" -> s"$startHeadingCy"
                )
              )
            )
          ))

          Then("The handoff to Address-Lookup-Frontend has been made with correct english and welsh titles")
          verifyWithBody("/api/v2/init", alfBody)

          res should have(
            httpStatus(SEE_OTHER),
            redirectURI("redirect/url")
          )
        }
      }

      "the local language is set to English" should {

        "handoff to address lookup frontend with the correct english and welsh messages" in {

          mockAppConfig.features.useNewAddressLookupFeature(true)
          mockAppConfig.features.stubAddressLookup(false)

          given.user.isAuthenticated

          And("A successful response with minimum details is returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

          And("a url is returned from the Address Lookup Service")
          BusinessAddressStub.postInitV2Journey(ACCEPTED, AddressLookupOnRampModel("redirect/url"))

          When("I call to show the Business Address change page")
          val res = show()


          val alfBody = Json.stringify(Json.obj(
            "labels" -> Json.obj(
              "en" -> Json.obj(
                "lookupPageLabels" -> Json.obj(
                  "title" -> s"$startHeading"
                )
              ),
              "cy" -> Json.obj(
                "lookupPageLabels" -> Json.obj(
                  "title" -> s"$startHeadingCy"
                )
              )
            )
          ))

          Then("The handoff to Address-Lookup-Frontend has been made with correct english and welsh titles")
          verifyWithBody("/api/v2/init", alfBody)

          res should have(
            httpStatus(SEE_OTHER),
            redirectURI("redirect/url")
          )
        }
      }
    }

    "An Error Model is returned from Address Lookup" should {

      "show internal server error" in {

        mockAppConfig.features.stubAddressLookup(false)

        given.agent.isSignedUpToAgentServices

        And("a url is returned from the Address Lookup Service")
        BusinessAddressStub.postInitJourney(BAD_REQUEST, AddressLookupOnRampModel("redirect/url"))

        When("I call to show the Customer Circumstances page")
        val res = show()

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }
    }
  }

  "Calling BusinessAddressController.callback" when {

    val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
      mandationStatus = MTDfBMandated,
      customerDetails = CustomerDetails(None, None, None, None, None, overseasIndicator = false),
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
          "lines" -> Json.arr("line1", "line2"),
          "country" -> Json.obj(
            "name" -> "United Kingdom",
            "code" -> "GB"
          )
        ))

        And("a valid CircumstanceDetails model is returned")
        BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin)(CircumstanceDetails.writes(true)))

        And("A response model is returned from the backend")
        BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

        When("I initiate a return frequency update journey")
        val res: WSResponse = get("/change-business-address/callback?id=111111111", session)

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("/vat-through-software/account/change-business-address/confirmation/non-agent")
        )
      }
    }

    "the user is an agent" should {

      "redirect to the ChangeAddressConfirmationPage page" in {

        mockAppConfig.features.stubAddressLookup(false)

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
        BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin)(CircumstanceDetails.writes(true)))

        And("A response model is returned from the backend")
        BusinessAddressStub.putSubscription(OK, Json.toJson(SubscriptionUpdateResponseModel("Good times")))

        When("I initiate a return frequency update journey")
        val res: WSResponse = get("/change-business-address/callback?id=111111111", session)

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
          BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin)(CircumstanceDetails.writes(true)))

          And("A response model is returned from the backend")
          BusinessAddressStub.putSubscription(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR, "Bad times")))

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
          BusinessAddressStub.getFullInformation(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR, "Bad times")))

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
          BusinessAddressStub.getAddress(INTERNAL_SERVER_ERROR, Json.toJson(ErrorModel(INTERNAL_SERVER_ERROR, "Bad times")))

          And("a valid CircumstanceDetails model is returned")
          BusinessAddressStub.getFullInformation(OK, Json.toJson(customerInformationModelMin)(CircumstanceDetails.writes(true)))

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

  "Calling BusinessAddressController.nonAgentConfirmation" when {

    def nonAgentConfirmation(): WSResponse = get("/change-business-address/confirmation/non-agent", session)

    "the user is an individual" should {

      "render the confirmation page" in {

        mockAppConfig.features.useContactPreferences(true)

        given.user.isAuthenticated

        And("A successful response is returned from contact preferences")
        ContactPreferencesStub.getContactPrefs(OK, Json.obj("preference" -> "DiGiTaL"))

        When("I call to show the Business Address change page")
        val res = nonAgentConfirmation()

        res should have(
          httpStatus(OK),
          elementText("#preference-message")(Messages("contact_preference.digital"))
        )
      }

    }

  }
}
