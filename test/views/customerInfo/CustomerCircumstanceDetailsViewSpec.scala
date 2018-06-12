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
import assets.messages.{ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import models.customerAddress.CountryCodes
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class CustomerCircumstanceDetailsViewSpec extends ViewBaseSpec {

  "Rendering the Customer Details page" when {

    "Viewing for any user (in this case Individual)" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual, false)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have a the correct page heading '${viewMessages.h1}'" in {
        elementText("h1") shouldBe viewMessages.h1
      }

      s"have a the correct page subheading '${viewMessages.subheading}'" in {
        elementText("#sub-heading") shouldBe viewMessages.subheading
      }

      s"have a section heading (h2) with '${viewMessages.h2}'" in {
        elementText("h2") shouldBe viewMessages.h2
      }

      "have a section for business address" which {

        "has the heading" in {
          elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        }

        "has the correct address output" in {
          elementText("#businessAddress li:nth-child(1)") shouldBe customerInformationModelMaxIndividual.ppob.get.address.get.line1.get
          elementText("#businessAddress li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.ppob.get.address.get.line2.get
          elementText("#businessAddress li:nth-child(3)") shouldBe customerInformationModelMaxIndividual.ppob.get.address.get.line3.get
          elementText("#businessAddress li:nth-child(4)") shouldBe customerInformationModelMaxIndividual.ppob.get.address.get.line4.get
          elementText("#businessAddress li:nth-child(5)") shouldBe customerInformationModelMaxIndividual.ppob.get.address.get.line5.get
          elementText("#businessAddress li:nth-child(6)") shouldBe customerInformationModelMaxIndividual.ppob.get.address.get.postCode.get
          elementText("#businessAddress li:nth-child(7)") shouldBe
            CountryCodes.getCountry(customerInformationModelMaxIndividual.ppob.get.address.get.countryCode.get)(frontendAppConfig).get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#place-of-business-status") shouldBe viewMessages.change + " " + viewMessages.changeBusinessAddressHidden
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
            elementText("#bank-details-status") shouldBe viewMessages.change + " " + viewMessages.changeBankDetailsHidden
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
            elementText("#vat-return-dates-status") shouldBe viewMessages.change + " " + viewMessages.changeReturnFrequencyHidden
          }

          s"has a link to ${controllers.routes.BusinessAddressController.initialiseJourney().url}" in {
            element("#vat-return-dates-status").attr("href") shouldBe controllers.returnFrequency.routes.ChooseDatesController.show().url
          }
        }
      }
    }

    "Viewing for an Organisation" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxOrganisation, false)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
        }

        s"has the value '${customerInformationModelMaxOrganisation.customerDetails.organisationName.get}'" in {
          elementText("#business-name") shouldBe customerInformationModelMaxOrganisation.customerDetails.organisationName.get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#business-name-status") shouldBe viewMessages.change + " " + viewMessages.changeBusinessHidden
          }

          s"has a link to '${controllers.routes.ChangeBusinessNameController.show().url}'" in {
            element("#business-name-status").attr("href") shouldBe controllers.routes.ChangeBusinessNameController.show().url
          }
        }
      }
    }

    "Viewing a client's details as an agent" should {

      lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual, true)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have a the correct page heading '${viewMessages.h1}'" in {
        elementText("h1") shouldBe viewMessages.h1
      }

      s"have a the correct page subheading '${viewMessages.agentSubheading}'" in {
        elementText("#sub-heading") shouldBe viewMessages.agentSubheading
      }

    }
  }
}
