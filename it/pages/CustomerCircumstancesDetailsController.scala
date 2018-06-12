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

import helpers.IntegrationTestConstants._
import models.customerAddress.CountryCodes
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._
import stubs.VatSubscriptionStub

class CustomerCircumstancesDetailsController extends BasePageISpec {

  val path = "/change-business-details"

  "Calling the .show action" when {

    def show(sessionVrn: Option[String] = None): WSResponse = get(path, formatSessionVrn(sessionVrn))

    "the user is an Agent" when {

      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {

        "a clients VRN is held in session cookie" when {

          "a success response is received for the Customer Details with all details (Organisation)" should {

            "Render the Customer Circumstances page with correct details shown" in {

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
                elementText("#businessAddress li:nth-of-type(1)")(ppob.address.get.line1.get),
                elementText("#businessAddress li:nth-of-type(2)")(ppob.address.get.line2.get),
                elementText("#businessAddress li:nth-of-type(3)")(ppob.address.get.line3.get),
                elementText("#businessAddress li:nth-of-type(4)")(ppob.address.get.line4.get),
                elementText("#businessAddress li:nth-of-type(5)")(ppob.address.get.line5.get),
                elementText("#businessAddress li:nth-of-type(6)")(ppob.address.get.postCode.get),
                elementText("#businessAddress li:nth-of-type(7)")(
                  CountryCodes.getCountry(ppob.address.get.countryCode.get).get
                ),

                //Bank Details
                elementText("#bankAccount li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
                elementText("#bankAccount li:nth-of-type(4)")(bankDetails.sortCode.get),

                //VAT Return Dates
                elementText("#vat-return-dates")("January, April, July and October")
              )
            }
          }

          "a success response is received for the Customer Details with minimum details (Organisation)" should {

            "Render the Customer Circumstances page with only the business name shown" in {

              given.agent.isSignedUpToAgentServices

              And("A successful response with minimum details returned for an Organisation")
              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMin(organisation))

              When("I call to show the Customer Circumstances page")
              val res = show(Some(clientVRN))

              res should have(
                httpStatus(OK),

                //Business Name
                elementText("#business-name")(organisation.businessName.get),

                //Business Address
                isElementVisible("#businessAddress")(isVisible = false),

                //Bank Details
                isElementVisible("#bankAccount")(isVisible = false),

                //VAT Return Dates
                isElementVisible("#vat-return-dates")(isVisible = false)
              )
            }
          }

          "a success response is received for the Customer Details with all details (Individual)" should {

            "Render the Customer Circumstances page with correct details shown" in {

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
                elementText("#businessAddress li:nth-of-type(1)")(ppob.address.get.line1.get),
                elementText("#businessAddress li:nth-of-type(2)")(ppob.address.get.line2.get),
                elementText("#businessAddress li:nth-of-type(3)")(ppob.address.get.line3.get),
                elementText("#businessAddress li:nth-of-type(4)")(ppob.address.get.line4.get),
                elementText("#businessAddress li:nth-of-type(5)")(ppob.address.get.line5.get),
                elementText("#businessAddress li:nth-of-type(6)")(ppob.address.get.postCode.get),
                elementText("#businessAddress li:nth-of-type(7)")(
                  CountryCodes.getCountry(ppob.address.get.countryCode.get).get
                ),

                //Bank Details
                elementText("#bankAccount li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
                elementText("#bankAccount li:nth-of-type(4)")(bankDetails.sortCode.get),

                //VAT Return Dates
                elementText("#vat-return-dates")("January, April, July and October")
              )
            }
          }

          "a success response is received for the Customer Details with minimum details (Individual)" should {

            "Render the Customer Circumstances page with only the business name shown" in {

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
                isElementVisible("#businessAddress")(isVisible = false),

                //Bank Details
                isElementVisible("#bankAccount")(isVisible = false),

                //VAT Return Dates
                isElementVisible("#vat-return-dates")(isVisible = false)
              )
            }
          }

          "an error response is received for the Customer Details" should {

            "Render the Internal Server Error page" in {

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
            elementText("#businessAddress li:nth-of-type(1)")(ppob.address.get.line1.get),
            elementText("#businessAddress li:nth-of-type(2)")(ppob.address.get.line2.get),
            elementText("#businessAddress li:nth-of-type(3)")(ppob.address.get.line3.get),
            elementText("#businessAddress li:nth-of-type(4)")(ppob.address.get.line4.get),
            elementText("#businessAddress li:nth-of-type(5)")(ppob.address.get.line5.get),
            elementText("#businessAddress li:nth-of-type(6)")(ppob.address.get.postCode.get),
            elementText("#businessAddress li:nth-of-type(7)")(
              CountryCodes.getCountry(ppob.address.get.countryCode.get).get
            ),

            //Bank Details
            elementText("#bankAccount li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
            elementText("#bankAccount li:nth-of-type(4)")(bankDetails.sortCode.get),

            //VAT Return Dates
            elementText("#vat-return-dates")("January, April, July and October")
          )
        }
      }

      "a success response is received for the Customer Details with minimum details (Organisation)" should {

        "Render the Customer Circumstances page with only the business name shown" in {

          given.user.isAuthenticated

          And("A successful response with minimum details returned for an Organisation")
          VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(organisation))

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(OK),

            //Business Name
            elementText("#business-name")(organisation.businessName.get),

            //Business Address
            isElementVisible("#businessAddress")(isVisible = false),

            //Bank Details
            isElementVisible("#bankAccount")(isVisible = false),

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
            elementText("#businessAddress li:nth-of-type(1)")(ppob.address.get.line1.get),
            elementText("#businessAddress li:nth-of-type(2)")(ppob.address.get.line2.get),
            elementText("#businessAddress li:nth-of-type(3)")(ppob.address.get.line3.get),
            elementText("#businessAddress li:nth-of-type(4)")(ppob.address.get.line4.get),
            elementText("#businessAddress li:nth-of-type(5)")(ppob.address.get.line5.get),
            elementText("#businessAddress li:nth-of-type(6)")(ppob.address.get.postCode.get),
            elementText("#businessAddress li:nth-of-type(7)")(
              CountryCodes.getCountry(ppob.address.get.countryCode.get).get
            ),

            //Bank Details
            elementText("#bankAccount li:nth-of-type(2)")(bankDetails.bankAccountNumber.get),
            elementText("#bankAccount li:nth-of-type(4)")(bankDetails.sortCode.get),

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
            isElementVisible("#businessAddress")(isVisible = false),

            //Bank Details
            isElementVisible("#bankAccount")(isVisible = false),

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

    getAuthenticationTests(path)
  }
}
