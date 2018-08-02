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

package views.businessName

import assets.CustomerDetailsTestConstants.orgName
import assets.messages.{BaseMessages,ChangeBusinessNamePageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class ChangeBusinessNameViewSpec extends ViewBaseSpec {

  "Rendering the Change Business Name page" should {

    object Selectors {
      val wrapper = "#content"
      val pageHeading = s"$wrapper h1"
      val p1 = s"$wrapper p:nth-of-type(1)"
      val p2 = s"$wrapper p:nth-of-type(2)"
      val p3 = s"$wrapper p:nth-of-type(3)"
      val bullet1 = s"$wrapper li:nth-of-type(1)"
      val bullet2 = s"$wrapper li:nth-of-type(2)"
      val p4 = s"$wrapper p:nth-of-type(4)"
      val link = s"$wrapper #continue"
      val backLink = ".link-back"
    }

    lazy val view = views.html.businessName.change_business_name(orgName)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a the back link with correct text and url '${BaseMessages.back}'" in {
      elementText(Selectors.backLink) shouldBe BaseMessages.back
      element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.isAgent).url
    }

    s"have a the correct page heading of '${viewMessages.h1}'" in {
      elementText(Selectors.pageHeading) shouldBe viewMessages.h1
    }

    s"have a the correct p1 of '${viewMessages.p1(orgName)}'" in {
      elementText(Selectors.p1) shouldBe viewMessages.p1(orgName)
    }

    s"have a the correct p2 of '${viewMessages.p2}'" in {
      elementText(Selectors.p2) shouldBe viewMessages.p2
    }

    s"have a the correct p3 of '${viewMessages.p3}'" in {
      elementText(Selectors.p3) shouldBe viewMessages.p3
    }

    s"have a the correct bullet1 of '${viewMessages.bullet1}'" in {
      elementText(Selectors.bullet1) shouldBe viewMessages.bullet1
    }

    s"have a the correct bullet2 of '${viewMessages.bullet2}'" in {
      elementText(Selectors.bullet2) shouldBe viewMessages.bullet2
    }

    s"have a the correct p4 of '${viewMessages.p4}'" in {
      elementText(Selectors.p4) shouldBe viewMessages.p4
    }

    "have a continue link" which {

      s"has the text '${viewMessages.link}'" in {
        elementText(Selectors.link) shouldBe viewMessages.link
      }

      "has a URL to the Gov.UK guidance page for changing name via COHO" in {
        element(Selectors.link).attr("href") shouldBe controllers.routes.ChangeBusinessNameController.handOffToCOHO().url
      }
    }
  }
}
