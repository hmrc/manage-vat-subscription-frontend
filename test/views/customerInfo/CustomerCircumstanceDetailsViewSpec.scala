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

package views.customerInfo

import assets.CircumstanceDetailsTestConstants._
import assets.CustomerDetailsTestConstants.individual
import assets.DeregistrationTestConstants._
import assets.PPOBAddressTestConstants
import assets.PPOBAddressTestConstants.ppobModelMax
import assets.messages.{BaseMessages, ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import models.circumstanceInfo.{CircumstanceDetails, MTDfBMandated}
import models.customerAddress.CountryCodes
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import utils.ImplicitDateFormatter._
import views.ViewBaseSpec

class CustomerCircumstanceDetailsViewSpec extends ViewBaseSpec {

  "Rendering the Customer Details page" when {

    mockConfig.features.registrationStatus(true)
    mockConfig.features.contactDetailsSection(true)
    mockConfig.features.allowAgentBankAccountChange(false)
    mockConfig.features.makingTaxDigitalSection(true)
    mockConfig.features.useLanguageSelector(true)

    "Viewing for an Individual without any pending changes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      "have the correct service name" in {
        elementText(".header__menu__proposition-name") shouldBe BaseMessages.clientServiceName
      }

      s"have a the correct page heading '${viewMessages.h1}'" in {
        elementText("h1") shouldBe viewMessages.h1
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
          elementText("#registration-section > h2") shouldBe viewMessages.registrationStatusHeading
        }

        "has a registration status header" in {
          elementText("#registration-status-text") shouldBe viewMessages.statusText
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

          s"has a link to ${controllers.routes.BusinessAddressController.show().url}" in {
            element("#place-of-business-status").attr("href") shouldBe controllers.routes.BusinessAddressController.show().url
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

      "have a section for contact details" which {

        "has a contact details header" in {
          elementText("#contact-details-section > h2") shouldBe viewMessages.contactDetailsHeading
        }

        "has the heading" in {
          elementText("#vat-email-address-text") shouldBe viewMessages.emailAddressHeading
        }

        "has the correct value for the email address" in {
          elementText("#vat-email-address") shouldBe customerInformationModelMaxIndividual.ppob.contactDetails.get.emailAddress.get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#vat-email-address-status") shouldBe viewMessages.change
          }

          s"has the correct aria label text '${viewMessages.changeEmailAddressHidden(PPOBAddressTestConstants.email)}'" in {
            element("#vat-email-address-status").attr("aria-label") shouldBe
              viewMessages.changeEmailAddressHidden(PPOBAddressTestConstants.email)
          }

          s"has a link to ${mockConfig.vatCorrespondenceChangeEmailUrl}" in {
            element("#vat-email-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeEmailUrl
          }
        }
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }

      "display a progressive disclosure" which {

        "contains help text" which {

          lazy val progressiveDisclosure = element("details")

          "contains the correct text" in {
            progressiveDisclosure.select("summary").text() shouldEqual viewMessages.changeNotListed
          }

          "contains content" which {

            lazy val helpContent = progressiveDisclosure.select("div > p")

            "displays the correct text" in {
              helpContent.text() shouldEqual viewMessages.helpText
            }

            s"has a link to ${mockConfig.govUkChangeVatRegistrationDetails}" in {
              helpContent.select("a").attr("href") shouldEqual mockConfig.govUkChangeVatRegistrationDetails
            }
          }
        }
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
    }

    "viewing for an individual with no email address" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMin)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "display the 'Not provided' text in place of the email address" in {
        elementText("#vat-email-address") shouldBe "Not provided"
      }

      "display an 'Add' link" which {

        "has the correct text" in {
          elementText("#vat-email-address-status") shouldBe "Add"
        }

        "links to the correspondence details service" in {
          element("#vat-email-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeEmailUrl
        }
      }
    }

    "Viewing for an Organisation with pending changes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelOrganisationPending)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "display a breadcrumb trail" in {
        elementText(".breadcrumbs li:nth-of-type(1)") shouldBe BaseMessages.breadcrumbBta
        elementText(".breadcrumbs li:nth-of-type(2)") shouldBe BaseMessages.breadcrumbVat
        elementText(".breadcrumbs li:nth-of-type(3)") shouldBe BaseMessages.breadcrumbBizDeets
      }

      "have a section for business address" which {

        "has the heading" in {
          elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        }

        "has the correct address output" in {
          elementText("#businessAddress li:nth-child(1)") shouldBe customerInformationModelOrganisationPending.pendingChanges.get.ppob.get.address.line1
          elementText("#businessAddress li:nth-child(2)") shouldBe customerInformationModelOrganisationPending.pendingChanges.get.ppob.get.address.line2.get
          elementText("#businessAddress li:nth-child(3)") shouldBe customerInformationModelOrganisationPending.pendingChanges.get.ppob.get.address.line3.get
          elementText("#businessAddress li:nth-child(4)") shouldBe customerInformationModelOrganisationPending.pendingChanges.get.ppob.get.address.line4.get
          elementText("#businessAddress li:nth-child(5)") shouldBe customerInformationModelOrganisationPending.pendingChanges.get.ppob.get.address.line5.get
          elementText("#businessAddress li:nth-child(6)") shouldBe customerInformationModelOrganisationPending.pendingChanges.get.ppob.get.address.postCode.get
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

        "has the correct Sort Code" which {

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

        "has Pending instead of a change link" which {

          s"has the wording '${viewMessages.pending}'" in {
            elementText("#vat-return-dates-status") shouldBe viewMessages.pending
          }

          s"has the correct aria label text '${viewMessages.pendingReturnFrequencyHidden}'" in {
            element("#vat-return-dates-status").attr("aria-label") shouldBe viewMessages.pendingReturnFrequencyHidden
          }

          s"has no link" in {
            element("#vat-return-dates-status").attr("href").isEmpty shouldBe true
          }
        }
      }

      "have a section for contact details" which {

        "has a contact details header" in {
          elementText("#contact-details-section > h2") shouldBe viewMessages.contactDetailsHeading
        }

        "has the heading" in {
          elementText("#vat-email-address-text") shouldBe viewMessages.emailAddressHeading
        }

        "has the correct value for the email address" in {
          elementText("#vat-email-address") shouldBe customerInformationModelOrganisationPending.pendingChanges.
            get.ppob.get.contactDetails.get.emailAddress.get
        }

        s"has the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
          element("#vat-email-address-status").attr("aria-label") shouldBe viewMessages.pendingEmailAddressHidden
        }
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }
    }

    "Viewing for an Organisation with one of the valid partyTypes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(Some("2")))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
        }
      }
    }

    "Viewing for a user with one of the valid partyTypes but no organisation name" should {

      val model: CircumstanceDetails = CircumstanceDetails(
        mandationStatus = MTDfBMandated,
        customerDetails = individual,
        flatRateScheme = None,
        ppob = ppobModelMax,
        bankDetails = None,
        returnPeriod = None,
        deregistration = None,
        changeIndicators = None,
        pendingChanges = None,
        partyType = Some(mockConfig.partyTypes.head)
      )

      lazy val view = views.html.customerInfo.customer_circumstance_details(model)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not have a change details section for the Business Name" in {
        document.select("#business-name-text").isEmpty shouldBe true
      }
    }

    "Viewing for an Organisation with a different valid partyType" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(Some("4")))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
        }
      }
    }

    "Viewing for an Organisation without one of the valid partyTypes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(Some("other")))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not have a change details section for the Business Name" in {
        document.select("#business-name-text").isEmpty shouldBe true
      }
    }

    "Viewing for an Organisation without any partyType" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(None))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have no change details section for the Business Name" in {
        document.select("#business-name-text").isEmpty shouldBe true
      }
    }

    "Viewing a client's details as an agent with changeClient feature switch on" when {

      "the allowAgentBankAccountChange feature is set to false" should {

        lazy val view = {
          mockConfig.features.changeClientFeature(true)
          views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not display a breadcrumb trail" in {
          elementExtinct(".breadcrumbs li:nth-of-type(1)")
          elementExtinct(".breadcrumbs li:nth-of-type(2)")
          elementExtinct(".breadcrumbs li:nth-of-type(3)")
        }

        s"have the correct document title '${viewMessages.title}'" in {
          document.title shouldBe viewMessages.agentTitle
        }

        "have the correct service name" in {
          elementText(".header__menu__proposition-name") shouldBe BaseMessages.agentServiceName
        }

        s"have a the correct page heading '${viewMessages.agentTitle}'" in {
          elementText("h1") shouldBe viewMessages.agentTitle
        }

        "display the 'Change client' link" in {
          elementText("#change-client-text") shouldBe viewMessages.newChangeClientDetails
          element("#change-client-link").attr("href") shouldBe
            controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
        }

        "display the Finish button" in {
          val finishSelector = "#finish-button"
          elementText(finishSelector) shouldBe viewMessages.finish
          element("#content > article > a").attr("href") shouldBe "/agent-action"
        }

        "not display the Change Bank Account details row" which {

          "is not found in the document" in {
            document.select("#bank-details-text") shouldBe empty
          }
        }

        "have a blank field where the 'Change' link would be for email address" which {

          "displays no text" in {
            elementText("#vat-email-address-status") shouldBe ""
          }

          s"has the correct aria-label text '${viewMessages.changeEmailAddressAgentHidden}'" in {
            element("#vat-email-address-status").attr("aria-label") shouldBe viewMessages.changeEmailAddressAgentHidden
          }
        }
      }

      "the allowAgentBankAccountChange feature is set to false" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "display the Change Bank Account details row" in {
          mockConfig.features.allowAgentBankAccountChange(true)
          elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
        }
      }
    }

    "Viewing a client's details as an agent with changeClient feature switch off" when {

      "the allowAgentBankAccountChange feature is set to false" should {

        lazy val view = {
          mockConfig.features.changeClientFeature(false)
          mockConfig.features.allowAgentBankAccountChange(false)
          views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not display a breadcrumb trail" in {
          elementExtinct(".breadcrumbs li:nth-of-type(1)")
          elementExtinct(".breadcrumbs li:nth-of-type(2)")
          elementExtinct(".breadcrumbs li:nth-of-type(3)")
        }

        s"have the correct document title '${viewMessages.agentTitle}'" in {
          document.title shouldBe viewMessages.agentTitle
        }

        "have the correct service name" in {
          elementText(".header__menu__proposition-name") shouldBe BaseMessages.agentServiceName
        }

        s"have a the correct page heading '${viewMessages.agentTitle}'" in {
          elementText("h1") shouldBe viewMessages.agentTitle
        }

        "display the 'change another clients details' link" in {
          elementText("#change-client-text") shouldBe viewMessages.oldChangeClientDetails
          element("#change-client-link").attr("href") shouldBe
            controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
        }

        "not display the Finish button" in {
          val finishSelector = "#finish-button"
          elementExtinct(finishSelector)
        }

        "not display the Change Bank Account details row" which {

          "is not found in the document" in {
            document.select("#bank-details-text") shouldBe empty
          }
        }

        "have a blank field where the 'Change' link would be for email address" which {

          "displays no text" in {
            elementText("#vat-email-address-status") shouldBe ""
          }

          s"has the correct aria-label text '${viewMessages.changeEmailAddressAgentHidden}'" in {
            element("#vat-email-address-status").attr("aria-label") shouldBe viewMessages.changeEmailAddressAgentHidden
          }
        }
      }

      "the allowAgentBankAccountChange feature is set to false" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "display the Change Bank Account details row" in {
          mockConfig.features.allowAgentBankAccountChange(true)
          elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
        }
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

    "the registration status is false deregistration set to a future date" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelFutureDereg)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not have a registration section" in {
        mockConfig.features.registrationStatus(true)
        elementText(".panel > p:nth-child(1)") shouldBe viewMessages.futureDeregDateText(futureDate.toLongDate)

      }
    }

    "the registration status is false - deregistration set to a past date" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "registration section displays the following" in {
        mockConfig.features.registrationStatus(true)
        elementText(".panel > p:nth-child(1)") shouldBe viewMessages.pastDeregDateText(pastDate.toLongDate)
        elementText("#registration-status")
        elementText("#registration-status-link") shouldBe viewMessages.howToRegister
        elementText("#registration-status-text") shouldBe viewMessages.statusText
        elementText("div.form-group:nth-child(5) > h2:nth-child(1)") shouldBe viewMessages.registrationStatusHeading
      }
    }

    "the registration status is false deregistration set to a pending" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelDeregPending)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "registration section displays the following" in {
        mockConfig.features.registrationStatus(true)
        elementText("#registration-status-link") shouldBe viewMessages.pending
        elementText("#registration-status") shouldBe viewMessages.deregPending
      }
    }

    "the registration status is true" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(Some("2")))(user, messages, mockConfig)
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
        elementExtinct("#contact-details-section")
      }
    }

    "the making tax digital feature switch is true" when {

      "the user is an agent" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "have a section for making tax digital" which {

          "has the correct section header" in {
            elementText("#mtd-section > h2") shouldBe viewMessages.mtdSectionHeading
          }

          "has the correct row heading" in {
            elementText("#opt-in-text") shouldBe viewMessages.statusText
          }

          "has the correct description" in {
            elementText("#opt-in") shouldBe viewMessages.optedIn
          }

          "has a change link" which {

            s"has the wording '${viewMessages.optOut}'" in {
              elementText("#opt-in-status") shouldBe viewMessages.optOut
            }

            s"has the correct aria label text '${viewMessages.changeMtdStatusHidden}'" in {
              element("#opt-in-status").attr("aria-label") shouldBe viewMessages.changeMtdStatusHidden
            }

            s"has a link to ${mockConfig.vatOptOutUrl}" in {
              element("#opt-in-status").attr("href") shouldBe mockConfig.vatOptOutUrl
            }
          }
        }
      }

      "the user is not an agent" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not display the making tax digital section" in {
          elementExtinct("#mtd-section")
        }
      }
    }

    "the making tax digital feature switch is false" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "hide the making tax digital section" in {
        mockConfig.features.makingTaxDigitalSection(false)
        elementExtinct("#mtd-section")
      }
    }

    "the user has a non-MTD mandation status" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNonMtd)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "hide the making tax digital section" in {
        mockConfig.features.makingTaxDigitalSection(true)
        elementExtinct("#mtd-section")
      }
    }

    "the user is an overseas business" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(overseasCompany)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not display business name" in {
        elementExtinct("#business-name-text")
        elementExtinct("#business-name")
        elementExtinct("#business-name-status")
      }

      "display PPOB" in {
        elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        elementText("#businessAddress > ol > li:nth-child(1)" ) shouldBe "Add Line 1"
      }

      "not give the option to change PPOB" in {
        elementExtinct("#place-of-business-status")
      }

    }
  }
}
