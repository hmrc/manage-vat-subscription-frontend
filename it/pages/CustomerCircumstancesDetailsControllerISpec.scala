/*
 * Copyright 2022 HM Revenue & Customs
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
import helpers.IntegrationTestConstants._
import helpers.SessionCookieCrumbler
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._
import stubs.{ServiceInfoStub, VatSubscriptionStub}

class CustomerCircumstancesDetailsControllerISpec extends BasePageISpec {

  val path = "/change-business-details"
  val verifyEmailPath = "/send-verification"
  lazy val mockAppConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  "Calling the .show action" when {

    "the user is a Principle Entity" when {

      def show(sessionVrn: Option[String] = None): WSResponse = get(path, formatSessionVrn(sessionVrn))

      "a success response is received from get customer details" when {

        "user is an Individual" when {

          "minimum customer details are returned" should {

            "render the Customer Circumstances page with options to add missing information" in {

              given.user.isAuthenticated

              And("A succesful partial is returned")
              ServiceInfoStub.stubServiceInfoPartial

              And("A successful response with minimum details returned for an Individual")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(individual))

              When("I call to show the Customer Circumstances page")
              val res = show()

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("There is an option to add bank details")
              res should have(
                elementText("#bank-details")(expectedValue = "Not provided"),
                elementText("#bank-details-status > span:nth-of-type(1)")(expectedValue = "Add")
              )

              And("There is no business name row")
              res should have(
                isElementVisible("#business-name")(isVisible = false)
              )

              And("There is no VAT return dates section")
              res should have(
                isElementVisible("#return-details-section > h2")(isVisible = false),
                isElementVisible("#vat-return-dates")(isVisible = false)
              )

              And("Business address is displayed")
              res should have(
                isElementVisible("#businessAddress")(isVisible = true)
              )
            }

          }

          "maximum customer details are returned" should {

            "render the Customer Circumstances page with correct details shown" in {

              given.user.isAuthenticated

              And("A succesful partial is returned")
              ServiceInfoStub.stubServiceInfoPartial

              And("A successful response with all details is returned for an Individual")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(individual, Some("1")))

              When("I call to show the Customer Circumstances page")
              val res = show()

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("No Business name is displayed")
              res should have(
                isElementVisible("#business-name")(isVisible = false)
              )

              And("Business address is displayed")
              res should have(
                elementText("#businessAddress")(
                  s"${ppobMax.address.line1} ${ppobMax.address.line2.get} ${ppobMax.address.postCode.get}"
                )
              )

              And("Bank details is displayed")
              res should have(
                isElementVisible("#bank-details")(isVisible = true)
              )

              And("Return frequency is displayed")
              res should have(
                elementText("#vat-return-dates")("January, April, July and October"),
                elementText("#vat-return-dates-text")("VAT Return dates"),
                elementText("#return-details-section > h2")("Return details")
              )

              And("Registration status is not displayed")
              res should have(
                isElementVisible("#registration-status-text")(isVisible = false),
                isElementVisible("#registration-status")(isVisible = false)
              )
            }
          }
        }

        "user is an Organisation" when {

          "organisation name is returned" should {

            "render the Business name row" in {

              given.user.isAuthenticated

              And("A succesful partial is returned")
              ServiceInfoStub.stubServiceInfoPartial

              And("A successful response with minimum details returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(organisation, partyType = Some("50")))

              When("I call to show the Customer Circumstances page")
              val res = show()

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("Business name is displayed")
              res should have(
                isElementVisible("#business-name")(isVisible = true)
              )
            }
          }
        }
      }

      "an error response is received" should {

        "Render the Internal Server Error page" in {

          given.user.isAuthenticated

          And("An unsuccessful response is returned")
          VatSubscriptionStub.getClientDetailsError(VRN)

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR),
            pageTitle(titleThereIsAProblem + titleSuffixUser)
          )
        }
      }
    }

    "the user is an Agent" when {

      def show(sessionVrn: Option[String] = None): WSResponse = get(path, formatSessionVrn(sessionVrn))

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a clients VRN is held in session cookie" when {

          "a success response is received" should {

            "render the Customer Circumstances page with correct details shown" in {

              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Individual")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(organisation, partyType = Some("50")))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(VRN))

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("Business name is displayed")
              res should have(
                isElementVisible("#business-name")(isVisible = true)
              )

              And("Business address is displayed")
              res should have(
                elementText("#businessAddress")(
                  s"${ppobMax.address.line1} ${ppobMax.address.line2.get} ${ppobMax.address.postCode.get}"
                )
              )

              And("Bank details is displayed")
              res should have(
                isElementVisible("#bank-details")(isVisible = false)
              )

              And("Return frequency is displayed")
              res should have(
                elementText("#vat-return-dates")("January, April, July and October"),
                elementText("#vat-return-dates-text")("VAT Return dates"),
                elementText("#return-details-section > h2")("Return details")
              )

            }
          }

          "an error response is received" should {

            "Render the Internal Server Error page" in {

              given.agent.isSignedUpToAgentServices

              And("An unsuccessful response is returned")
              VatSubscriptionStub.getClientDetailsError(VRN)

              When("I call to show the Customer Circumstances page")
              val res = show(Some(VRN))

              res should have(
                httpStatus(INTERNAL_SERVER_ERROR),
                pageTitle(titleThereIsAProblem + titleSuffixAgent)
              )
            }
          }
        }

        "no client VRN is held in the session cookie" when {

          "stub Agent Client Lookup is enabled" should {

            "Redirect to the stubbed Client Lookup page" in {

              mockAppConfig.features.stubAgentClientLookup(true)

              given.agent.isSignedUpToAgentServices

              When("I call to show the Customer Circumstances page without a VRN being in session for the Client")
              val res = show()

              res should have(
                httpStatus(SEE_OTHER),
                redirectURI(
                  testOnly.controllers.routes.StubAgentClientLookupController.show(controllers.routes.CustomerCircumstanceDetailsController.show.url).url
                )
              )
            }
          }

          "stub Agent Client Lookup is disabled" when {

            "VACLF is enabled" should {

              "Redirect to the VACLF Client Lookup page" in {

                mockAppConfig.features.stubAgentClientLookup(false)

                given.agent.isSignedUpToAgentServices

                When("I call to show the Customer Circumstances page without a VRN being in session for the Client")
                val res = show()

                res should have(
                  httpStatus(SEE_OTHER),
                  redirectURI(mockAppConfig.vatAgentClientLookupHandoff(controllers.routes.CustomerCircumstanceDetailsController.show.url))
                )
              }
            }

            "VACLF is disabled" should {

              "Redirect to the stubbed Client Lookup page" in {

                mockAppConfig.features.stubAgentClientLookup(true)

                given.agent.isSignedUpToAgentServices

                When("I call to show the Customer Circumstances page without a VRN being in session for the Client")
                val res = show()

                res should have(
                  httpStatus(SEE_OTHER),
                  redirectURI(
                    testOnly.controllers.routes.StubAgentClientLookupController.show(controllers.routes.CustomerCircumstanceDetailsController.show.url).url
                  )
                )
              }
            }
          }
        }
      }

      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {

        "Render the unauthorised view asking them to sign up for Agent Services" in {

          given.agent.isNotSignedUpToAgentServices

          When("I call to show the Customer Circumstances page")
          val res = show(Some(clientVRN))

          res should have(
            httpStatus(FORBIDDEN),
            pageTitle(Messages("unauthorised.agent.title") + titleSuffixAgent)
          )
        }
      }
    }

    getAuthenticationTests(path)
  }

  "Calling the sendEmailVerification action" when {

    def sendEmailVerification(sessionVrn: Option[String] = None): WSResponse = get(verifyEmailPath, formatSessionVrn(sessionVrn))

    "a success response is received from get customer details" when {

      "the user has no pending ppob/contact details changes" should {

        "redirect to somewhere and store session keys" in {
          given.user.isAuthenticated

          And("A successful response with minimum details returned for an Individual")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(individual).copy(pendingChanges = None))

          When("the sendEmailVerification call is made")
          val res = sendEmailVerification()

          Then("Status should be SEE_OTHER")
          res should have(httpStatus(SEE_OTHER))

          And("redirect location should be something")
          redirectLocation(res) shouldBe Some(mockAppConfig.vatCorrespondenceSendVerificationEmail)

          And("the email will be stored in session")
          SessionCookieCrumbler.getSessionMap(res).get(SessionKeys.vatCorrespondencePrepopulationEmailKey) shouldBe Some(email)

          And("the inFlightContactDetailsChangeKey will be stored in session")
          SessionCookieCrumbler.getSessionMap(res).get(SessionKeys.inFlightContactDetailsChangeKey) shouldBe Some("false")
        }
      }
    }
  }
}
