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

package views.customerInfo

import assets.CircumstanceDetailsTestConstants._
import assets.DeregistrationTestConstants._
import assets.PPOBAddressTestConstants
import assets.messages.{BaseMessages, ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import models.customerAddress.CountryCodes
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import utils.ImplicitDateFormatter._
import views.ViewBaseSpec

class CustomerCircumstanceDetailsViewSpec extends ViewBaseSpec {

  "Rendering the Customer Details page" when {

        mockConfig.features.registrationStatus(true)
        mockConfig.features.contactDetailsSection(true)

    "Viewing for any user (in this case Individual) without any pending changes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have the correct service name" in {
        elementText(".header__menu__proposition-name") shouldBe BaseMessages.clientServiceName
      }

      s"have a the correct page heading '${viewMessages.h1}'" in {
        elementText("h1") shouldBe viewMessages.h1
      }

      s"have a the correct page subheading '${viewMessages.subheading}'" in {
        elementText("#sub-heading") shouldBe viewMessages.subheading
      }

      "display a breadcrumb trail which" in {
        elementText(".breadcrumbs li:nth-of-type(1)") shouldBe BaseMessages.breadcrumbBta
        elementText(".breadcrumbs li:nth-of-type(2)") shouldBe BaseMessages.breadcrumbVat
        elementText(".breadcrumbs li:nth-of-type(3)") shouldBe BaseMessages.breadcrumbBizDeets

        element("#breadcrumb-bta").attr("href") shouldBe "ye olde bta url"
        element("#breadcrumb-vat").attr("href") shouldBe "ye olde vat summary url"
      }

      "have a section for registration status" which {

        "has a registration header" in {
          elementText("div.form-group:nth-child(5) > h2:nth-child(1)") shouldBe viewMessages.registrationStatusHeading
        }

        "has a registration status header" in {
          elementText("#registration-status-text") shouldBe viewMessages.registrationStatusText
        }

        "displays the correct registration status" in {
          elementText("#registration-status") shouldBe viewMessages.deregStatus(toLongDate(pastDate))
        }

        "has the 'how to register' link" in {
          elementText("#registration-status-link") shouldBe viewMessages.howToRegister
          element("#registration-status-link").attr("href") shouldBe "https://www.gov.uk/vat-registration/how-to-register"
        }
      }

      "has an about header" in {
        elementText("#content > article > div:nth-child(3) > h2") shouldBe viewMessages.aboutHeading
      }

      "have a section for business address" which {

        "has the heading" in {
          elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        }

        "has the correct address output" in {
          elementText("#businessAddress li:nth-child(1)") shouldBe customerInformationModelMaxIndividual.ppob.address.line1
          elementText("#businessAddress li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.ppob.address.line2.get
          elementText("#businessAddress li:nth-child(3)") shouldBe customerInformationModelMaxIndividual.ppob.address.line3.get
          elementText("#businessAddress li:nth-child(4)") shouldBe customerInformationModelMaxIndividual.ppob.address.line4.get
          elementText("#businessAddress li:nth-child(5)") shouldBe customerInformationModelMaxIndividual.ppob.address.line5.get
          elementText("#businessAddress li:nth-child(6)") shouldBe customerInformationModelMaxIndividual.ppob.address.postCode.get
          elementText("#businessAddress li:nth-child(7)") shouldBe
            CountryCodes.getCountry(customerInformationModelMaxIndividual.ppob.address.countryCode)(mockConfig).get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#place-of-business-status") shouldBe viewMessages.change
          }

          s"has the correct aria label text '${viewMessages.changeBusinessAddressHidden(PPOBAddressTestConstants.addLine1)}'" in {
            element("#place-of-business-status").attr("aria-label") shouldBe viewMessages.changeBusinessAddressHidden(PPOBAddressTestConstants.addLine1)
          }

          s"has a link to ${controllers.routes.BusinessAddressController.initialiseJourney().url}" in {
            element("#place-of-business-status").attr("href") shouldBe controllers.routes.BusinessAddressController.initialiseJourney().url
          }
        }
      }

      "have a section for repayment Bank Account details" which {

        "has the heading" in {
          elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
        }

        "has a the correct Account Number" which {

          "has the correct heading for the Account Number" in {
            elementText("#bank-details li:nth-child(1)") shouldBe viewMessages.accountNumberHeading
          }

          "has the correct value for the account number" in {
            elementText("#bank-details li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.bankDetails.get.bankAccountNumber.get
          }
        }

        "has a the correct Sort Code" which {

          "has the correct heading for the Sort Code" in {
            elementText("#bank-details li:nth-child(3)") shouldBe viewMessages.sortcodeHeading
          }

          "has the correct value for the account number" in {
            elementText("#bank-details li:nth-child(4)") shouldBe customerInformationModelMaxIndividual.bankDetails.get.sortCode.get
          }
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#bank-details-status") shouldBe viewMessages.change
          }

          s"has the correct aria label text '${viewMessages.changeBankDetailsHidden}'" in {
            element("#bank-details-status").attr("aria-label") shouldBe viewMessages.changeBankDetailsHidden
          }

          s"has a link to ${controllers.routes.PaymentsController.sendToPayments().url}" in {
            element("#bank-details-status").attr("href") shouldBe controllers.routes.PaymentsController.sendToPayments().url
          }
        }
      }

      "have a section for contact details if there is an email address" which {

        "has a contact details header" in {
          elementText("#content > article > div:nth-child(4) > h2") shouldBe viewMessages.contactDetailsHeading
        }

        "has the heading" in {
          elementText("#vat-email-address-text") shouldBe viewMessages.emailAddressHeading
        }

        "has the correct value for the email address" in {
          elementText("#vat-email-address") shouldBe customerInformationModelMaxIndividual.ppob.contactDetails.get.emailAddress.get
        }
      }

      "not have a section for contact details if there is not an email address" which {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMin)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "does not have a contact details header" in {
          elementExtinct("#content > article > div:nth-child(4) > h2")
        }

        "does not have the heading" in {
          elementExtinct("#vat-email-address-text")
        }

        "does not have a value for email address" in {
          elementExtinct("#vat-email-address")
        }
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }
    }

    "for a user with a future deregistration date" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelFutureDereg)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a section for registration status" which {

        "displays the correct registration status" in {
          elementText("#registration-status") shouldBe viewMessages.futureDereg(toLongDate(futureDate))
        }

        "has the 'how to register' link" in {
          elementText("#registration-status-link") shouldBe viewMessages.howToRegister
          element("#registration-status-link").attr("href") shouldBe "https://www.gov.uk/vat-registration/how-to-register"
        }
      }
    }

    "for a user with a pending deregistration" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelDeregPending)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a section for registration status" which {

        "displays the correct registration status" in {
          elementText("#registration-status") shouldBe viewMessages.deregPending
        }

        "states that the decision is 'pending'" in {
          elementText("#registration-status-link") shouldBe viewMessages.pending
        }
      }
    }

    "Viewing for an Organisation with pending changes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxOrganisation)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "display a breadcrumb trail" in {
        elementText(".breadcrumbs li:nth-of-type(1)") shouldBe BaseMessages.breadcrumbBta
        elementText(".breadcrumbs li:nth-of-type(2)") shouldBe BaseMessages.breadcrumbVat
        elementText(".breadcrumbs li:nth-of-type(3)") shouldBe BaseMessages.breadcrumbBizDeets
      }

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
        }

        s"has the value '${customerInformationModelMaxOrganisation.customerDetails.organisationName.get}'" in {
          elementText("#business-name") shouldBe customerInformationModelMaxOrganisation.customerDetails.organisationName.get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            document.select("#business-name-status").isEmpty shouldBe true
          }
        }
      }

      "have a section for business address" which {

        "has the heading" in {
          elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        }

        "has the correct address output" in {
          elementText("#businessAddress li:nth-child(1)") shouldBe customerInformationModelMaxIndividual.ppob.address.line1
          elementText("#businessAddress li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.ppob.address.line2.get
          elementText("#businessAddress li:nth-child(3)") shouldBe customerInformationModelMaxIndividual.ppob.address.line3.get
          elementText("#businessAddress li:nth-child(4)") shouldBe customerInformationModelMaxIndividual.ppob.address.line4.get
          elementText("#businessAddress li:nth-child(5)") shouldBe customerInformationModelMaxIndividual.ppob.address.line5.get
          elementText("#businessAddress li:nth-child(6)") shouldBe customerInformationModelMaxIndividual.ppob.address.postCode.get
          elementText("#businessAddress li:nth-child(7)") shouldBe
            CountryCodes.getCountry(customerInformationModelMaxIndividual.ppob.address.countryCode)(mockConfig).get
        }

        "has Pending instead of a change link" which {

          s"has the wording '${viewMessages.pending}'" in {
            elementText("#place-of-business-status") shouldBe viewMessages.pending
          }

          s"has the correct aria label text '${viewMessages.pendingBusinessAddressHidden}'" in {
            element("#place-of-business-status").attr("aria-label") shouldBe viewMessages.pendingBusinessAddressHidden
          }

          s"has no link" in {
            element("#place-of-business-status").attr("href").isEmpty shouldBe true
          }
        }
      }

      "have a section for repayment Bank Account details" which {

        "has the heading" in {
          elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
        }

        "has a the correct Account Number" which {

          "has the correct heading for the Account Number" in {
            elementText("#bank-details li:nth-child(1)") shouldBe viewMessages.accountNumberHeading
          }

          "has the correct value for the account number" in {
            elementText("#bank-details li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.bankDetails.get.bankAccountNumber.get
          }
        }

        "has a the correct Sort Code" which {

          "has the correct heading for the Sort Code" in {
            elementText("#bank-details li:nth-child(3)") shouldBe viewMessages.sortcodeHeading
          }

          "has the correct value for the account number" in {
            elementText("#bank-details li:nth-child(4)") shouldBe customerInformationModelMaxIndividual.bankDetails.get.sortCode.get
          }
        }

        "has Pending instead of a change link" which {

          s"has the wording '${viewMessages.pending}'" in {
            elementText("#bank-details-status") shouldBe viewMessages.pending
          }

          s"has the correct aria label text '${viewMessages.pendingBankDetailsHidden}'" in {
            element("#bank-details-status").attr("aria-label") shouldBe viewMessages.pendingBankDetailsHidden
          }

          s"has no link" in {
            element("#bank-details-status").attr("href").isEmpty shouldBe true
          }
        }
      }


      "have a section for return frequency" which {

        "has the heading" in {
          elementText("#vat-return-dates-text") shouldBe viewMessages.returnFrequencyHeading
        }

        "has the correct value output for the current frequency" in {
          elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
        }

        "has no change link or pending status" in {
          document.select("#vat-return-dates-status").isEmpty shouldBe true
        }
      }

      "have a section for contact details if there is an email address" which {

        "has a contact details header" in {
          elementText("#content > article > div:nth-child(3) > h2") shouldBe viewMessages.contactDetailsHeading
        }

        "has the heading" in {
          elementText("#vat-email-address-text") shouldBe viewMessages.emailAddressHeading
        }

        "has the correct value for the email address" in {
          elementText("#vat-email-address") shouldBe customerInformationModelMaxIndividual.ppob.contactDetails.get.emailAddress.get
        }
      }

      "not have a section for contact details if there is not an email address" which {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMin)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "does not have a contact details header" in {
          elementExtinct("#content > article > div:nth-child(4) > h2")
        }

        "does not have the heading" in {
          elementExtinct("#vat-email-address-text")
        }

        "does not have a value for email address" in {
          elementExtinct("#vat-email-address")
        }
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }
    }

    "Viewing a client's details as an agent" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not display a breadcrumb trail" in {
        elementExtinct(".breadcrumbs li:nth-of-type(1)")
        elementExtinct(".breadcrumbs li:nth-of-type(2)")
        elementExtinct(".breadcrumbs li:nth-of-type(3)")
      }

      s"have the correct document title '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have the correct service name" in {
        elementText(".header__menu__proposition-name") shouldBe BaseMessages.agentServiceName
      }

      s"have a the correct page heading '${viewMessages.h1}'" in {
        elementText("h1") shouldBe viewMessages.h1
      }

      s"have a the correct page subheading '${viewMessages.agentSubheading}'" in {
        elementText("#sub-heading") shouldBe viewMessages.agentSubheading
      }

      "display the 'change another clients details' link" in {
        elementText("#change-client-text") shouldBe viewMessages.changeClientDetails
        element("#change-client-link").attr("href") shouldBe
          controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
      }
    }

    "the registration feature switch is disabled" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not have a registration section" in {
        mockConfig.features.registrationStatus(false)
        elementText("div.form-group:nth-child(3) > h2:nth-child(1)") shouldBe viewMessages.aboutHeading
        document.select("#registration-status-text").isEmpty shouldBe true
        document.select("#registration-status").isEmpty shouldBe true
        document.select("#registration-status-link").isEmpty shouldBe true
      }
    }


    "the registration status is true" should {
      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationRegisteredIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a section for return frequency" which {

        "has the heading" in {
          elementText("#vat-return-dates-text") shouldBe viewMessages.returnFrequencyHeading
        }

        "has the correct value output for the current frequency" in {
          elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#vat-return-dates-status") shouldBe viewMessages.change
          }

          s"has the correct aria label text '${viewMessages.changeReturnFrequencyHidden(ReturnFrequencyMessages.option3Mar)}'" in {
            element("#vat-return-dates-status").attr("aria-label") shouldBe viewMessages.changeReturnFrequencyHidden(ReturnFrequencyMessages.option3Mar)
          }

          s"has a link to ${controllers.routes.BusinessAddressController.initialiseJourney().url}" in {
            element("#vat-return-dates-status").attr("href") shouldBe controllers.returnFrequency.routes.ChooseDatesController.show().url
          }
        }
      }
    }

    "the registration status is false deregistration set to a future date " should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelFutureDereg)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not have a registration section" in {
        mockConfig.features.registrationStatus(true)
        elementText(".panel > p:nth-child(1)") shouldBe viewMessages.futureDeregDateText(futureDate.toLongDate)

      }
    }

    "the registration status is false - deregistration set to a past date " should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "registration section displays the following" in {
        mockConfig.features.registrationStatus(true)
        elementText(".panel > p:nth-child(1)") shouldBe viewMessages.pastDeregDateText(pastDate.toLongDate)
        elementText("#registration-status")
        elementText("#registration-status-link") shouldBe viewMessages.howToRegister
        elementText("#registration-status-text") shouldBe viewMessages.registrationStatusText
        elementText("div.form-group:nth-child(5) > h2:nth-child(1)") shouldBe viewMessages.registrationStatusHeading
      }
    }


    "the registration status is false deregistration set to a pending " should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelDeregPending)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "registration section displays the following" in {
        mockConfig.features.registrationStatus(true)
        elementText("#registration-status-link") shouldBe viewMessages.pending
        elementText("#registration-status") shouldBe viewMessages.deregPending
      }
    }


    "the registration status is true" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingChangeOfCert)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "registration section displays the following" in {
        mockConfig.features.registrationStatus(true)
        elementText("#registration-status-link") shouldBe viewMessages.deregister
        elementText("#business-name-status") shouldBe viewMessages.change
        elementText("#place-of-business-status") shouldBe viewMessages.change
        elementText("#bank-details-status") shouldBe viewMessages.change
        elementText("#vat-return-dates-status") shouldBe viewMessages.change
      }
    }

    "the contact details feature switch is false" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "contact details section is hidden" in {
        mockConfig.features.contactDetailsSection(false)
        elementExtinct("#content > article > div:nth-child(4) > h2")
      }
    }


  }
}
