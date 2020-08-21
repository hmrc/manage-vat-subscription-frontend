/*
 * Copyright 2020 HM Revenue & Customs
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

import assets.BaseTestConstants.agentEmail
import assets.messages.{BaseMessages, ChangeAddressConfirmationPageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.businessAddress.ChangeAddressConfirmationView

class ChangeAddressConfirmationViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: ChangeAddressConfirmationView = inject[ChangeAddressConfirmationView]

  "the ChangeAddressConfirmationView for an individual" when {

    "contact preference is set to 'DIGITAL'" when {

      "the user has a verified email address" should {
        lazy val view: Html = injectedView(contactPref = Some("DIGITAL"), emailVerified = true)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct document title of '${viewMessages.title}'" in {
          document.title shouldBe viewMessages.title
        }

        s"have the correct page heading of '${viewMessages.heading}'" in {
          elementText("h1") shouldBe viewMessages.heading
        }

        s"have the correct p1 of '${viewMessages.digiPrefEmailVerified}'" in {
          paragraph(1) shouldBe viewMessages.digiPrefEmailVerified
        }

        s"have the correct p2 of '${viewMessages.contactDetails}'" in {
          paragraph(2) shouldBe viewMessages.contactDetails
        }

        s"have a button to finish" which {

          s"has the correct text of '$finish" in {
            elementText("#finish") shouldBe finish
          }

          s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
            element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
          }
        }
      }

      "the user has not verified their email address" should {
        lazy val view: Html = injectedView(contactPref = Some("DIGITAL"))(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct document title of '${viewMessages.title}'" in {
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

        s"have a button to finish" which {

          s"has the correct text of '$finish" in {
            elementText("#finish") shouldBe finish
          }

          s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
            element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
          }
        }
      }
    }

    "contact preference is set to 'PAPER'" should {

      lazy val view: Html = injectedView(contactPref = Some("PAPER"))(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
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

      s"have a button to finish" which {

        s"has the correct text of '$finish" in {
          elementText("#finish") shouldBe finish
        }

        s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url}'" in {
          element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url
        }
      }
    }

    "an error is returned from contact preferences" should {

      lazy val view: Html = injectedView()(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
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

      s"have a button to finish" which {

        s"has the correct text of '$finish" in {
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

      "there is a client name, the changeClient feature switch is on and the disableBulkPaper switch is off" should {
        lazy val view: Html = {
          mockConfig.features.changeClientFeature(true)
          mockConfig.features.disableBulkPaper(false)
          injectedView(
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

        s"have a button to finish" which {

          s"has the correct text of '$finishAgent" in {
            elementText("#finish") shouldBe finishAgent
          }

          s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("agent").url}'" in {
            element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("agent").url
          }
        }
      }

      "there is a client name, the changeClient feature switch is off and the disableBulkPaper switch is off" should {
        lazy val view: Html = {
          mockConfig.features.changeClientFeature(false)
          mockConfig.features.disableBulkPaper(false)
          injectedView(
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

        "have a button to finish" which {

          s"has the correct text of '$finishAgent" in {
            elementText("#finish") shouldBe finishAgent
          }

          s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show("agent").url}'" in {
            element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show("agent").url
          }
        }
      }

      "there is no client name and the disableBulkPaper switch is off" should {

        lazy val view: Html ={
          mockConfig.features.disableBulkPaper(false)
          injectedView(
            agentEmail = Some(agentEmail))(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct p2 of '${viewMessages.p2AgentNoClientName}'" in {
          paragraph(2) shouldBe viewMessages.p2AgentNoClientName
        }
      }
      "there is a client name, the changeClient switched on and the disableBulkPaper switch is on" should {
        lazy val view: Html = {
          mockConfig.features.changeClientFeature(true)
          injectedView(
            clientName = Some("MyCompany Ltd"), agentEmail = Some(agentEmail))(agentUser, messages, mockConfig)
        }
        lazy implicit  val document: Document = Jsoup.parse(view.body)

        s"have the correct p1 of '${viewMessages.confirmationAgentBulkPaper}'" in {
          paragraph(1) shouldBe viewMessages.confirmationAgentBulkPaper
        }

        s"have the correct p2 of '${viewMessages.p2Agent}" in {
          paragraph(2) shouldBe viewMessages.p2Agent
        }
      }
    }

    "they have selected to not receive email notifications" when {

      "there is a client name and the disableBulkPaper switch is off" should {

        lazy val view: Html = {
          mockConfig.features.disableBulkPaper(false)
          injectedView(
            clientName = Some("MyCompany Ltd"))(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct p1 of '${viewMessages.confirmationLetter}'" in {
          paragraph(1) shouldBe viewMessages.confirmationLetter
        }
      }

      "there is no client name and the disableBulkPaper switch is off" should {

        lazy val view: Html = {
          mockConfig.features.disableBulkPaper(false)
          injectedView()(agentUser, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        s"have the correct p1 of '${viewMessages.confirmationLetter}'" in {
          paragraph(1) shouldBe viewMessages.confirmationLetter
        }
      }
    }
  }
}
