/*
 * Copyright 2024 HM Revenue & Customs
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
import views.html.businessName.ChangeBusinessNameView

class ChangeBusinessNameViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: ChangeBusinessNameView = inject[ChangeBusinessNameView]

  "Rendering the Change Business Name page" should {

    object Selectors {
      val wrapper = "#content"
      val pageHeading = s"$wrapper h1"
      val noCrn = s"$wrapper details summary"
      val contactHMRC = s"$wrapper details div"
      val p1 = s"$wrapper p:nth-of-type(1)"
      val p2 = s"$wrapper p:nth-of-type(2)"
      val p3 = s"$wrapper p:nth-of-type(3)"
      val p4 = s"$wrapper p:nth-of-type(4)"
      val p5 = s"$wrapper p:nth-of-type(5)"
      val link = ".govuk-body > a"

      val detailsLink = s"$wrapper details div a"
      val backLink = ".govuk-back-link"
    }

    lazy val view = injectedView(orgName)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a the back link with correct text and url '$back'" in {
      elementText(Selectors.backLink) shouldBe back
      element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show.url
    }

    s"have a the correct page heading of '${viewMessages.heading}'" in {
      elementText(Selectors.pageHeading) shouldBe viewMessages.heading
    }

    s"have a the correct p1 of '${viewMessages.p1}'" in {
      elementText(Selectors.p1) shouldBe viewMessages.p1
    }

    s"have a the correct details summary of '${viewMessages.noCRN}'" in {
      elementText(Selectors.noCrn) shouldBe viewMessages.noCRN
    }

    s"have a the correct details text of '${viewMessages.toChangeName} ${viewMessages.contactHMRC}'" in {
      elementText(Selectors.contactHMRC) shouldBe viewMessages.toChangeName + viewMessages.contactHMRC
    }

    s"have a the correct details url of '${viewMessages.toChangeName} ${viewMessages.contactHMRC}'" in {

      element(Selectors.detailsLink).attr("href") shouldBe "https://www.gov.uk/government/organisations/hm-revenue-customs/contact/vat-enquiries"
    }

    s"have a the correct p2 of '${viewMessages.p2}'" in {
      elementText(Selectors.p2) shouldBe viewMessages.p2
    }

    s"have a the correct p3 of '${viewMessages.your}${viewMessages.bold1}${viewMessages.p3}'" in {
      elementText(Selectors.p3) shouldBe viewMessages.your + viewMessages.bold1 + viewMessages.p3
    }





    s"have a the correct p4 of '${viewMessages.p4}'" in {
      elementText(Selectors.p4) shouldBe viewMessages.p4
    }

    "have a link to information on changing business link" which {

      s"has the text '${viewMessages.p5}'" in {
        elementText(Selectors.link) shouldBe viewMessages.p5
      }

      "has a URL to the Gov.UK guidance page for changing name" in {
        element(Selectors.link).attr("href") shouldBe "https://www.gov.uk/government/publications/change-a-company-name-nm01"
      }
    }
  }
}
