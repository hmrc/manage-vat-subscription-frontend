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

import assets.CustomerDetailsTestConstants._
import assets.messages.{CustomerDetailsPageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class CustomerDetailsViewSpec extends ViewBaseSpec {

  "Rendering the Customer Details page" when {

    "Viewing for any user" should {

      lazy val view = views.html.customerInfo.customer_details(individual)(request, messages, mockConfig)
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

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#place-of-business-status") shouldBe viewMessages.change + " " + viewMessages.changeBusinessAddressHidden
          }

          s"has a link to ${controllers.routes.BusinessAddressController.initialiseJourney().url}" in {
            element("#place-of-business-status").attr("href") shouldBe controllers.routes.BusinessAddressController.initialiseJourney().url
          }
        }
      }

      "have a section for bank account details" which {

        "has the heading" in {
          elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
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

    "Viewing for an Self-Employed Individual" should {

      lazy val view = views.html.customerInfo.customer_details(individual)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#individual-name-text") shouldBe viewMessages.individualNameHeading
        }

        s"has the value '${individual.userName.get}'" in {
          elementText("#individual-name") shouldBe individual.userName.get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#individual-name-status") shouldBe viewMessages.change + " " + viewMessages.changeIndividualHidden
          }

          //TODO: Update this when URL developed and known
          "has a link to '#'" in {
            element("#individual-name-status").attr("href") shouldBe "#"
          }
        }
      }
    }

    "Viewing for an Organisation" should {

      lazy val view = views.html.customerInfo.customer_details(organisation)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
        }

        s"has the value '${organisation.organisationName.get}'" in {
          elementText("#business-name") shouldBe organisation.organisationName.get
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
  }
}
