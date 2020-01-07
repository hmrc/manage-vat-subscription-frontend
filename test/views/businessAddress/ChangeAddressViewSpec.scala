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

import assets.messages.{BaseMessages, ChangeAddressPageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.businessAddress.ChangeAddressView

class ChangeAddressViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: ChangeAddressView = inject[ChangeAddressView]

  "the ChangeAddressConfirmationView for an individual" should {

    lazy val view: Html = injectedView()(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have the correct page heading of '${viewMessages.heading}'" in {
      elementText("h1") shouldBe viewMessages.heading
    }

    s"have a the back link with correct text and url '$back'" in {
      elementText("#content > article > a.link-back") shouldBe back
      element("#content > article > a.link-back").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url
    }

    s"have the correct p1 of '${viewMessages.p1}'" in {
      paragraph(1) shouldBe viewMessages.p1
    }

    s"have the correct p2 of '${viewMessages.p2}'" in {
      paragraph(2) shouldBe viewMessages.p2
    }

    "have the correct text to start the bullet list" in {
      elementText("#content > article > div > p") shouldBe viewMessages.bulletHeader
    }

    "have the correct text for the 3 bullet points" in {
      bullet(1) shouldBe viewMessages.bullet1
      bullet(2) shouldBe viewMessages.bullet2
      bullet(3) shouldBe viewMessages.bullet3
    }

    s"have a button to continue" which {

      s"has the correct text of '$continue" in {
        elementText("#content > article > a.button") shouldBe continue
      }

      s"has the correct link to '${controllers.routes.BusinessAddressController.initialiseJourney().url}'" in {
        element("#content > article > a.button").attr("href") shouldBe controllers.routes.BusinessAddressController.initialiseJourney().url
      }
    }
  }
}
