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
import assets.{CustomerDetailsTestConstants, PPOBAddressTestConstants}
import assets.messages.{BaseMessages, ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import models.customerAddress.CountryCodes
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class CustomerCircumstanceDetailsViewSpec extends ViewBaseSpec {

  "Rendering the Customer Details page" when {

    "Viewing for any user (in this case Individual) without any pending changes" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPending)(user, messages, mockConfig)
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

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
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
            elementText("#business-name-status") shouldBe viewMessages.change
          }

          s"has the correct aria label text '${viewMessages.changeBusinessHidden}'" in {
            element("#business-name-status").attr("aria-label") shouldBe viewMessages.changeBusinessHidden(CustomerDetailsTestConstants.orgName)
          }

          s"has a link to '${controllers.routes.ChangeBusinessNameController.show().url}'" in {
            element("#business-name-status").attr("href") shouldBe controllers.routes.ChangeBusinessNameController.show().url
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
  }
}
