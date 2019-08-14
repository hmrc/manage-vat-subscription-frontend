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

package views.businessAddress

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.{businessAddress => views}
import assets.messages.{ChangeAddressConfirmationPageMessages => viewMessages}
import assets.messages.BaseMessages
import assets.BaseTestConstants.agentEmail

class ChangeAddressConfirmationViewSpec extends ViewBaseSpec with BaseMessages {

  "the ChangeAddressConfirmationView for an individual" when {

    "'useContactPreferences' is disabled" should {

      lazy val view: Html = views.change_address_confirmation()(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
        mockConfig.features.useContactPreferences(false)
        document.title shouldBe viewMessages.title
      }

      s"have the correct page heading of '${viewMessages.heading}'" in {
        elementText("h1") shouldBe viewMessages.heading
      }

      s"have the correct p1 of '${viewMessages.p1}'" in {
        paragraph(1) shouldBe viewMessages.p1
      }

      s"have the correct p2 of '${viewMessages.p2}'" in {
        paragraph(2) shouldBe viewMessages.p2
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }

      s"have a button to finish" which {

        s"has the correct text of '${finish}" in {
          elementText("#finish") shouldBe finish
        }

        s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
          element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
        }
      }
    }

    "'useContactPreferences' is enabled and set to 'DIGITAL'" should {

      lazy val view: Html = views.change_address_confirmation(contactPref = Some("DIGITAL"))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
        mockConfig.features.useContactPreferences(true)
        document.title shouldBe viewMessages.title
      }

      s"have the correct page heading of '${viewMessages.heading}'" in {
        elementText("h1") shouldBe viewMessages.heading
      }

      s"have the correct p1 of '${viewMessages.digitalPref}'" in {
        paragraph(1) shouldBe viewMessages.digitalPref
      }

      s"have the correct p2 of '${viewMessages.contactDetails}'" in {
        paragraph(2) shouldBe viewMessages.contactDetails
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }

      s"have a button to finish" which {

        s"has the correct text of '${finish}" in {
          elementText("#finish") shouldBe finish
        }

        s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
          element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
        }
      }
    }

    "'useContactPreferences' is enabled and set to 'PAPER'" should {

      lazy val view: Html = views.change_address_confirmation(contactPref = Some("PAPER"))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
        mockConfig.features.useContactPreferences(true)
        document.title shouldBe viewMessages.title
      }

      s"have the correct page heading of '${viewMessages.heading}'" in {
        elementText("h1") shouldBe viewMessages.heading
      }

      s"have the correct p1 of '${viewMessages.paperPref}'" in {
        paragraph(1) shouldBe viewMessages.paperPref
      }

      s"have the correct p2 of '${viewMessages.contactDetails}'" in {
        paragraph(2) shouldBe viewMessages.contactDetails
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }

      s"have a button to finish" which {

        s"has the correct text of '${finish}" in {
          elementText("#finish") shouldBe finish
        }

        s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
          element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
        }
      }
    }

    "'useContactPreferences' is enabled, but an error is returned" should {

      lazy val view: Html = views.change_address_confirmation()(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
        mockConfig.features.useContactPreferences(true)
        document.title shouldBe viewMessages.title
      }

      s"have the correct page heading of '${viewMessages.heading}'" in {
        elementText("h1") shouldBe viewMessages.heading
      }

      s"have the correct p1 of '${viewMessages.contactPrefError}'" in {
        paragraph(1) shouldBe viewMessages.contactPrefError
      }

      s"have the correct p2 of '${viewMessages.contactDetails}'" in {
        paragraph(2) shouldBe viewMessages.contactDetails
      }

      "not display the 'change another clients details' link" in {
        elementExtinct("#change-client-text")
      }

      s"have a button to finish" which {

        s"has the correct text of '${finish}" in {
          elementText("#finish") shouldBe finish
        }

        s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
          element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
        }
      }
    }
  }

  "the ChangeAddressConfirmationView for an agent" when {

    "they have selected to receive email notifications" when {

      "there is a client name and the changeClient feature switch is on" should {
        lazy val view: Html = {
          mockConfig.features.changeClientFeature(true)
          views.change_address_confirmation(
            clientName = Some("MyCompany Ltd"), agentEmail = Some(agentEmail))(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct document title of '${viewMessages.titleAgent}'" in {
          document.title shouldBe viewMessages.titleAgent
        }

        s"have the correct page heading of '${viewMessages.heading}'" in {
          elementText("h1") shouldBe viewMessages.heading
        }

        s"have the correct p1 of '${viewMessages.p1Agent}'" in {
          paragraph(1) shouldBe viewMessages.p1Agent
        }

        s"have the correct p2 of '${viewMessages.p2Agent}'" in {
          paragraph(2) shouldBe viewMessages.p2Agent
        }

        "display the 'change another clients details' link" in {
          elementText("#change-client-text") shouldBe viewMessages.newChangeClientDetails
          element("#change-client-link").attr("href") shouldBe
            controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
        }

        s"have a button to finish" which {

          s"has the correct text of '${finish}" in {
            elementText("#finish") shouldBe finish
          }

          s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("agent").url}'" in {
            element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("agent").url
          }
        }
      }

      "there is a client name and the changeClient feature switch is off" should {
        lazy val view: Html = {
          mockConfig.features.changeClientFeature(false)
          views.change_address_confirmation(
            clientName = Some("MyCompany Ltd"), agentEmail = Some(agentEmail))(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct document title of '${viewMessages.titleAgent}'" in {
          document.title shouldBe viewMessages.titleAgent
        }

        s"have the correct page heading of '${viewMessages.heading}'" in {
          elementText("h1") shouldBe viewMessages.heading
        }

        s"have the correct p1 of '${viewMessages.p1Agent}'" in {
          paragraph(1) shouldBe viewMessages.p1Agent
        }

        s"have the correct p2 of '${viewMessages.p2Agent}'" in {
          paragraph(2) shouldBe viewMessages.p2Agent
        }

        "display the 'change another clients details' link" in {
          elementText("#change-client-text") shouldBe viewMessages.oldChangeClientDetails
          element("#change-client-link").attr("href") shouldBe
            controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
        }

        "have a button to finish" which {

          s"has the correct text of '${finish}" in {
            elementText("#finish") shouldBe finish
          }

          s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("agent").url}'" in {
            element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("agent").url
          }
        }
      }

      "there is no client name" should {

        lazy val view: Html = views.change_address_confirmation(
          agentEmail = Some(agentEmail))(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct p2 of '${viewMessages.p2AgentNoClientName}'" in {
          paragraph(2) shouldBe viewMessages.p2AgentNoClientName
        }
      }
    }

    "they have selected to not receive email notifications" when {

      "there is a client name" should {

        lazy val view: Html = views.change_address_confirmation(
          clientName = Some("MyCompany Ltd"))(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct p1 of '${viewMessages.confirmationLetter}'" in {
          paragraph(1) shouldBe viewMessages.confirmationLetter
        }

        s"have the correct p2 of '${viewMessages.p2Agent}'" in {
          paragraph(2) shouldBe viewMessages.p2Agent
        }
      }

      "there is no client name" should {

        lazy val view: Html = views.change_address_confirmation()(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct p1 of '${viewMessages.confirmationLetter}'" in {
          paragraph(1) shouldBe viewMessages.confirmationLetter
        }

        s"have the correct p2 of '${viewMessages.p2AgentNoClientName}'" in {
          paragraph(2) shouldBe viewMessages.p2AgentNoClientName
        }
      }
    }
  }
}
