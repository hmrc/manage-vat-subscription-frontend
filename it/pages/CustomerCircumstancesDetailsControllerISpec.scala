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

    "the user is an Agent" when {

      def show(sessionVrn: Option[String] = None): WSResponse = get(path + "/agent", formatSessionVrn(sessionVrn))

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a clients VRN is held in session cookie" when {

          "the Registration Status Feature is enabled" should {

            "Render the Customer Circumstances page with the Registration section visible" in {
              mockAppConfig.features.registrationStatus(true)
              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMax(organisation))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),
                isElementVisible("#registration-status-text")(isVisible = true)
              )
            }
          }

          "the Registration Status Feature is disabled" should {

            "Render the Customer Circumstances page without the Registration section visible" in {
              mockAppConfig.features.registrationStatus(false)
              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMax(organisation))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),
                isElementVisible("#registration-status-text")(isVisible = false)
              )
            }
          }

          "a success response is received for the Customer Details with all details (Organisation)" should {

            "Render the Customer Circumstances page with correct details shown" in {

              mockAppConfig.features.agentAccess(true)
              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMax(organisation))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),

                //Business Name
                elementText("#business-name")(organisation.businessName.get),

                //Business Address
                elementText("#businessAddress li:nth-of-type(1)")(ppob.address.line1),
                elementText("#businessAddress li:nth-of-type(2)")(ppob.address.line2.get),
                elementText("#businessAddress li:nth-of-type(3)")(ppob.address.line3.get),
                elementText("#businessAddress li:nth-of-type(4)")(ppob.address.line4.get),
                elementText("#businessAddress li:nth-of-type(5)")(ppob.address.line5.get),
                elementText("#businessAddress li:nth-of-type(6)")(ppob.address.postCode.get),
                elementText("#businessAddress li:nth-of-type(7)")(
                  CountryCodes.getCountry(ppob.address.countryCode).get
                ),

                //Bank Details
                elementText("#bank-details li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
                elementText("#bank-details li:nth-of-type(4)")(bankDetails.sortCode.get),

                //VAT Return Dates
                elementText("#vat-return-dates")("January, April, July and October")
              )
            }
          }

          "a success response is received for the Customer Details with minimum details (Organisation)" should {

            "Render the Customer Circumstances page with only the business name shown" in {

              mockAppConfig.features.agentAccess(true)
              given.agent.isSignedUpToAgentServices

              And("A successful response with minimum details returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsWithPartyType(organisation))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),

                //Business Name
                elementText("#business-name")(organisation.businessName.get),

                //Business Address
                isElementVisible("#businessAddress")(isVisible = true),

                //Bank Details
                isElementVisible("#bank-details")(isVisible = false),

                //VAT Return Dates
                isElementVisible("#vat-return-dates")(isVisible = false)
              )
            }
          }

          "a success response is received for the Customer Details with all details (Individual)" should {

            "Render the Customer Circumstances page with correct details shown" in {

              mockAppConfig.features.agentAccess(true)
              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMax(individual))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),

                //Business Name
                isElementVisible("#business-name")(isVisible = false),

                //Business Address
                elementText("#businessAddress li:nth-of-type(1)")(ppob.address.line1),
                elementText("#businessAddress li:nth-of-type(2)")(ppob.address.line2.get),
                elementText("#businessAddress li:nth-of-type(3)")(ppob.address.line3.get),
                elementText("#businessAddress li:nth-of-type(4)")(ppob.address.line4.get),
                elementText("#businessAddress li:nth-of-type(5)")(ppob.address.line5.get),
                elementText("#businessAddress li:nth-of-type(6)")(ppob.address.postCode.get),
                elementText("#businessAddress li:nth-of-type(7)")(
                  CountryCodes.getCountry(ppob.address.countryCode).get
                ),

                //Bank Details
                elementText("#bank-details li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
                elementText("#bank-details li:nth-of-type(4)")(bankDetails.sortCode.get),

                //VAT Return Dates
                elementText("#vat-return-dates")("January, April, July and October")
              )
            }
          }

          "a success response is received for the Customer Details with minimum details (Individual)" should {

            "Render the Customer Circumstances page with only the business name shown" in {

              mockAppConfig.features.agentAccess(true)
              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMin(individual))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),

                //Business Name
                isElementVisible("#business-name")(isVisible = false),

                //Business Address
                isElementVisible("#businessAddress")(isVisible = true),

                //Bank Details
                isElementVisible("#bank-details")(isVisible = false),

                //VAT Return Dates
                isElementVisible("#vat-return-dates")(isVisible = false)
              )
            }
          }

          "an error response is received for the Customer Details" should {

            "Render the Internal Server Error page" in {

              mockAppConfig.features.agentAccess(true)
              given.agent.isSignedUpToAgentServices

              And("A successful response with all details is returned for an Organisation")
              VatSubscriptionStub.getClientDetailsError(clientVRN)

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(INTERNAL_SERVER_ERROR),
                pageTitle(Messages("global.error.InternalServerError500.title"))
              )
            }

          }
        }

        "NO client VRN is held in the session cookie" should {

          "Redirect to the Select a Client page" in {

            mockAppConfig.features.agentAccess(true)
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

    "the user is a Principle Entity and not an Agent" when {

      def show(sessionVrn: Option[String] = None): WSResponse = get(s"$path/non-agent", formatSessionVrn(sessionVrn))

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
            isElementVisible("#registration-status-text")(isVisible = true)
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

      "a success response is received for the Customer Details with all details (Organisation)" should {

        "Render the Customer Circumstances page with correct details shown" in {

          given.user.isAuthenticated

          And("A successful response with all details is returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(organisation))

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(OK),

            //Business Name
            elementText("#business-name")(organisation.businessName.get),

            //Business Address
            elementText("#businessAddress li:nth-of-type(1)")(ppob.address.line1),
            elementText("#businessAddress li:nth-of-type(2)")(ppob.address.line2.get),
            elementText("#businessAddress li:nth-of-type(3)")(ppob.address.line3.get),
            elementText("#businessAddress li:nth-of-type(4)")(ppob.address.line4.get),
            elementText("#businessAddress li:nth-of-type(5)")(ppob.address.line5.get),
            elementText("#businessAddress li:nth-of-type(6)")(ppob.address.postCode.get),
            elementText("#businessAddress li:nth-of-type(7)")(
              CountryCodes.getCountry(ppob.address.countryCode).get
            ),

            //Bank Details
            elementText("#bank-details li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
            elementText("#bank-details li:nth-of-type(4)")(bankDetails.sortCode.get),

            //VAT Return Dates
            elementText("#vat-return-dates")("January, April, July and October")
          )
        }
      }

      "a success response is received for the Customer Details with minimum details (Organisation)" should {

        "Render the Customer Circumstances page with only business address shown" in {

          given.user.isAuthenticated

          And("A successful response with minimum details returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(OK),

            //Business Name
            isElementVisible("#business-name")(isVisible = false),

            //Business Address
            isElementVisible("#businessAddress")(isVisible = true),

            //Bank Details
            isElementVisible("#bank-details")(isVisible = false),

            //VAT Return Dates
            isElementVisible("#vat-return-dates")(isVisible = false)
          )
        }
      }

      "a success response is received for the Customer Details with a partyType (Organisation)" should {

        "Render the Customer Circumstances page with only business address shown" in {

          given.user.isAuthenticated

          And("A successful response with minimum details returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsWithPartyType(organisation))

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(OK),

            //Business Name
            elementText("#business-name")(organisation.businessName.get),

            //Business Address
            isElementVisible("#businessAddress")(isVisible = true),

            //Bank Details
            isElementVisible("#bank-details")(isVisible = false),

            //VAT Return Dates
            isElementVisible("#vat-return-dates")(isVisible = false)
          )
        }
      }

      "a success response is received for the Customer Details with all details (Individual)" should {

        "Render the Customer Circumstances page with correct details shown" in {

          given.user.isAuthenticated

          And("A successful response with all details is returned for an Individual")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(individual))

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(OK),

            //Business Name
            isElementVisible("#business-name")(isVisible = false),

            //Business Address
            elementText("#businessAddress li:nth-of-type(1)")(ppob.address.line1),
            elementText("#businessAddress li:nth-of-type(2)")(ppob.address.line2.get),
            elementText("#businessAddress li:nth-of-type(3)")(ppob.address.line3.get),
            elementText("#businessAddress li:nth-of-type(4)")(ppob.address.line4.get),
            elementText("#businessAddress li:nth-of-type(5)")(ppob.address.line5.get),
            elementText("#businessAddress li:nth-of-type(6)")(ppob.address.postCode.get),
            elementText("#businessAddress li:nth-of-type(7)")(
              CountryCodes.getCountry(ppob.address.countryCode).get
            ),

            //Bank Details
            elementText("#bank-details li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
            elementText("#bank-details li:nth-of-type(4)")(bankDetails.sortCode.get),

            //VAT Return Dates
            elementText("#vat-return-dates")("January, April, July and October")
          )
        }
      }

      "a success response is received for the Customer Details with minimum details (Individual)" should {

        "Render the Customer Circumstances page with only the business name shown" in {

          given.user.isAuthenticated

          And("A successful response with minimum details returned for an Individual")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(individual))

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(OK),

            //Business Name
            isElementVisible("#business-name")(isVisible = false),

            //Business Address
            isElementVisible("#businessAddress")(isVisible = true),

            //Bank Details
            isElementVisible("#bank-details")(isVisible = false),

            //VAT Return Dates
            isElementVisible("#vat-return-dates")(isVisible = false)
          )
        }
      }

      "an error response is received for the Customer Details" should {

        "Render the Internal Server Error page" in {

          given.user.isAuthenticated

          And("A successful response with all details is returned for an Organisation")
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

    getAuthenticationTests(s"$path/non-agent")
  }
}
