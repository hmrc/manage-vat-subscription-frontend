/*
 * Copyright 2021 HM Revenue & Customs
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

package views.businessName

import assets.CustomerDetailsTestConstants.orgName
import assets.messages.{BaseMessages, ChangeBusinessNamePageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import views.html.businessName.AltChangeBusinessNameView

class AltChangeBusinessNameViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: AltChangeBusinessNameView = inject[AltChangeBusinessNameView]

  "Rendering the Alternative Change Business Name page" when {

    object Selectors {
      val wrapper = "#content"
      val pageHeading = s"$wrapper h1"
      val p1 = s"$wrapper article p:nth-of-type(1)"
      val p2 = s"$wrapper article p:nth-of-type(2)"
      val p3 = s"$wrapper article p:nth-of-type(3)"
      val bullet1 = s"$wrapper li:nth-of-type(1)"
      val bullet2 = s"$wrapper li:nth-of-type(2)"
      val p4 = s"$wrapper article p:nth-of-type(4)"
      val bullet3 = s"$wrapper ul:nth-child(8) > li:nth-child(1)"
      val formLink = s"$wrapper ul:nth-child(8) > li:nth-child(1) > a"
      val bullet4 = s"$wrapper ul:nth-child(8) > li:nth-child(2)"
      val bullet5 = s"$wrapper ul:nth-child(8) > li:nth-child(3)"
      val link = s"$wrapper #continue"
      val backLink = ".link-back"
    }

    "a regular user accesses the page" should {

      lazy val view = injectedView(orgName)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have a the back link with correct text and url '$back'" in {
        elementText(Selectors.backLink) shouldBe back
        element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url
      }

      s"have a the correct page heading of '${viewMessages.heading}'" in {
        elementText(Selectors.pageHeading) shouldBe viewMessages.heading
      }

      s"have a the correct p1 of '${viewMessages.altP1(orgName)}'" in {
        elementText(Selectors.p1) shouldBe viewMessages.altP1(orgName)
      }

      s"have a the correct p2 of '${viewMessages.altP2}'" in {
        elementText(Selectors.p2) shouldBe viewMessages.altP2
      }

      "have a continue link" which {

        s"has the text '${viewMessages.altContinueLinkText}'" in {
          elementText(Selectors.link) shouldBe viewMessages.altContinueLinkText
        }

        "has a URL to the Gov.UK guidance page for changing business details" in {
          element(Selectors.link).attr("href") shouldBe mockConfig.govUkChangeToBusinessDetails
        }
      }
    }

    "an agent accesses the page" should {

      lazy val view = injectedView(orgName)(agentUser, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.titleAgent}'" in {
        document.title shouldBe viewMessages.titleAgent
      }

      s"have a the back link with correct text and url '$back'" in {
        elementText(Selectors.backLink) shouldBe back
        element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(agentUser.redirectSuffix).url
      }

      s"have a the correct page heading of '${viewMessages.heading}'" in {
        elementText(Selectors.pageHeading) shouldBe viewMessages.heading
      }

      s"have a the correct p1 of '${viewMessages.altP1(orgName)}'" in {
        elementText(Selectors.p1) shouldBe viewMessages.altP1(orgName)
      }

      s"have a the correct p2 of '${viewMessages.altP2Agent}'" in {
        elementText(Selectors.p2) shouldBe viewMessages.altP2Agent
      }

      "have a continue link" which {

        s"has the text '${viewMessages.altContinueLinkText}'" in {
          elementText(Selectors.link) shouldBe viewMessages.altContinueLinkText
        }

        "has a URL to the Gov.UK guidance page for changing business details" in {
          element(Selectors.link).attr("href") shouldBe mockConfig.govUkChangeToBusinessDetails
        }
      }
    }
  }
}
