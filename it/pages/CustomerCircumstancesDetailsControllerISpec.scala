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

import config.FrontendAppConfig
import helpers.IntegrationTestConstants._
import models.customerAddress.CountryCodes
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._
import stubs.VatSubscriptionStub

class CustomerCircumstancesDetailsControllerISpec extends BasePageISpec {

  val path = "/change-business-details"
  lazy val mockAppConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  "Calling the .show action" when {

    "the user is a Principle Entity" when {

      def show(sessionVrn: Option[String] = None): WSResponse = get(s"$path/non-agent", formatSessionVrn(sessionVrn))

      "a success response is received from get customer details" when {

        "user is an Individual" when {

          "minimum customer details are returned" should {

            "render the Customer Circumstances page with options to add missing information" in {

              given.user.isAuthenticated

              And("A successful response with minimum details returned for an Individual")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(individual))

              When("I call to show the Customer Circumstances page")
              val res = show()

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("There is an option to add bank details and an email")
              res should have(
                elementText("#bank-details")(expectedValue = "None"),
                elementText("#bank-details-status")(expectedValue = "Add"),

                elementText("#vat-email-address")(expectedValue = "None"),
                elementText("#vat-email-address-status")(expectedValue = "Add")
              )

              And("There is no business name or VAT return dates row")
              res should have(
                isElementVisible("#business-name")(isVisible = false),
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
                elementText("#businessAddress li:nth-of-type(1)")(ppobMax.address.line1),
                elementText("#businessAddress li:nth-of-type(2)")(ppobMax.address.line2.get),
                elementText("#businessAddress li:nth-of-type(3)")(ppobMax.address.line3.get),
                elementText("#businessAddress li:nth-of-type(4)")(ppobMax.address.line4.get),
                elementText("#businessAddress li:nth-of-type(5)")(ppobMax.address.line5.get),
                elementText("#businessAddress li:nth-of-type(6)")(ppobMax.address.postCode.get),
                elementText("#businessAddress li:nth-of-type(7)")(
                  CountryCodes.getCountry(ppobMax.address.countryCode).get
                )
              )

              And("Bank details is displayed")
              res should have(
                elementText("#bank-details li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
                elementText("#bank-details li:nth-of-type(4)")(bankDetails.sortCode.get)
              )

              And("Return frequency is displayed")
              res should have(
                elementText("#vat-return-dates")("January, April, July and October")
              )

              And("Email address is displayed")
              res should have(
                elementText("#vat-email-address")("test@test.com")
              )
            }
          }
        }

        "user is an Organisation" when {

          "organisation name is returned" should {

            "render the Business name row" in {

              given.user.isAuthenticated

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

          "organisation name is not returned" should {

            "not render the Business name row" in {

              given.user.isAuthenticated

              And("A successful response with minimum details returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

              When("I call to show the Customer Circumstances page")
              val res = show()

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("No Business name is displayed")
              res should have(
                isElementVisible("#business-name")(isVisible = false)
              )
            }
          }
        }

        "the Registration Status Feature is enabled" should {

          "Render the Customer Circumstances page with the Registration section visible" in {
            mockAppConfig.features.registrationStatus(true)
            given.user.isAuthenticated

            And("A successful response with all details is returned for an Organisation")
            VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(organisation))

            When("I call to show the Customer Circumstances page")
            val res = show(Some(VRN))

            res should have(
              httpStatus(OK),
              elementText("#registration-status-text")(expectedValue = "Status"),
              elementText("#registration-status")(expectedValue = "Deregistration requested")
            )
          }
        }

        "the Registration Status Feature is disabled" should {

          "Render the Customer Circumstances page without the Registration section visible" in {
            mockAppConfig.features.registrationStatus(false)
            given.user.isAuthenticated

            And("A successful response with all details is returned for an Organisation")
            VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(organisation))

            When("I call to show the Customer Circumstances page")
            val res = show(Some(VRN))

            res should have(
              httpStatus(OK),
              isElementVisible("#registration-status-text")(isVisible = false)
            )
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
            pageTitle(Messages("global.error.InternalServerError500.title"))
          )
        }
      }
    }

    "the user is an Agent" when {

      def show(sessionVrn: Option[String] = None): WSResponse = get(path + "/agent", formatSessionVrn(sessionVrn))

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a clients VRN is held in session cookie" when {

          "a success response is received" should {

            "render the Customer Circumstances page with correct details shown" in {

              given.user.isAuthenticated

              And("A successful response with all details is returned for an Individual")
              VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(organisation, partyType = Some("50")))

              When("I call to show the Customer Circumstances page")
              val res = show()

              Then("Status should be OK")
              res should have(httpStatus(OK))

              And("Business name is displayed")
              res should have(
                isElementVisible("#business-name")(isVisible = true)
              )

              And("Business address is displayed")
              res should have(
                elementText("#businessAddress li:nth-of-type(1)")(ppobMax.address.line1),
                elementText("#businessAddress li:nth-of-type(2)")(ppobMax.address.line2.get),
                elementText("#businessAddress li:nth-of-type(3)")(ppobMax.address.line3.get),
                elementText("#businessAddress li:nth-of-type(4)")(ppobMax.address.line4.get),
                elementText("#businessAddress li:nth-of-type(5)")(ppobMax.address.line5.get),
                elementText("#businessAddress li:nth-of-type(6)")(ppobMax.address.postCode.get),
                elementText("#businessAddress li:nth-of-type(7)")(
                  CountryCodes.getCountry(ppobMax.address.countryCode).get
                )
              )

              And("Bank details is displayed")
              res should have(
                elementText("#bank-details li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
                elementText("#bank-details li:nth-of-type(4)")(bankDetails.sortCode.get)
              )

              And("Return frequency is displayed")
              res should have(
                elementText("#vat-return-dates")("January, April, July and October")
              )

              And("Email address is displayed")
              res should have(
                elementText("#vat-email-address")("test@test.com")
              )
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
                pageTitle(Messages("global.error.InternalServerError500.title"))
              )
            }
          }
        }

        "no client VRN is held in the session cookie" when {

          "stub Agent Client Lookup is enabled" should {

            "Redirect to the stubbed Client Lookup page" in {

              mockAppConfig.features.agentAccess(true)
              mockAppConfig.features.stubAgentClientLookup(true)

              given.agent.isSignedUpToAgentServices

              When("I call to show the Customer Circumstances page without a VRN being in session for the Client")
              val res = show()

              res should have(
                httpStatus(SEE_OTHER),
                redirectURI(
                  testOnly.controllers.routes.StubAgentClientLookupController.show(controllers.routes.CustomerCircumstanceDetailsController.redirect().url).url
                )
              )
            }
          }

          "stub Agent Client Lookup is disabled" when {

            "VACLF is enabled" should {

              "Redirect to the VACLF Client Lookup page" in {

                mockAppConfig.features.agentAccess(true)
                mockAppConfig.features.stubAgentClientLookup(false)
                mockAppConfig.features.useAgentClientLookup(true)

                given.agent.isSignedUpToAgentServices

                When("I call to show the Customer Circumstances page without a VRN being in session for the Client")
                val res = show()

                res should have(
                  httpStatus(SEE_OTHER),
                  redirectURI(mockAppConfig.vatAgentClientLookupHandoff(controllers.routes.CustomerCircumstanceDetailsController.redirect().url))
                )
              }
            }

            "VACLF is disabled" should {

              "Redirect to the Client Lookup page" in {

                mockAppConfig.features.agentAccess(true)
                mockAppConfig.features.stubAgentClientLookup(false)
                mockAppConfig.features.useAgentClientLookup(false)

                given.agent.isSignedUpToAgentServices

                When("I call to show the Customer Circumstances page without a VRN being in session for the Client")
                val res = show()

                res should have(
                  httpStatus(SEE_OTHER),
                  redirectURI(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
                )
              }
            }
          }
        }
      }

      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {

        "Render the unauthorised view asking them to sign up for Agent Services" in {

          mockAppConfig.features.agentAccess(true)
          given.agent.isNotSignedUpToAgentServices

          When("I call to show the Customer Circumstances page")
          val res = show(Some(clientVRN))

          res should have(
            httpStatus(FORBIDDEN),
            pageTitle(Messages("unauthorised.agent.title"))
          )
        }
      }
    }

    getAuthenticationTests(s"$path/non-agent")
  }
}
