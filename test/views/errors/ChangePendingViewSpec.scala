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

package views.errors

import assets.messages.{BaseMessages, ChangePendingMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import views.html.errors.ChangePendingView

class ChangePendingViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: ChangePendingView = inject[ChangePendingView]

  object Selectors {
    val heading = "h1"
    val paragraphOne = "article > p:nth-child(3)"
    val paragraphTwo = "article > p:nth-child(4)"
    val backLink = ".link-back"
    val listItemOne = "article ul li:nth-child(1)"
    val listItemTwo = "article ul li:nth-child(2)"
    val listItemThree = "article ul li:nth-child(3)"
  }

  "The change pending view" should {

    lazy val view = injectedView("changePending.email")(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct title" in {
      document.title shouldBe ChangePendingMessages.title
    }

    "have the correct heading" in {
      elementText(Selectors.heading) shouldBe ChangePendingMessages.heading
    }

    "have the correct information in the first paragraph including email change" in {
      elementText(Selectors.paragraphOne) shouldBe ChangePendingMessages.p1 + ChangePendingMessages.emailChange
    }

    "have the correct information in the second paragraph" in {
      elementText(Selectors.paragraphTwo) shouldBe ChangePendingMessages.p2
    }

    "have the correct first list item" in {
      elementText(Selectors.listItemOne) shouldBe ChangePendingMessages.listItem1
    }

    "have the correct second list item" in {
      elementText(Selectors.listItemTwo) shouldBe ChangePendingMessages.listItem2
    }

    "have the correct third list item" in {
      elementText(Selectors.listItemThree) shouldBe ChangePendingMessages.listItem3
    }

    "have the correct text for the back link" in {
      elementText(Selectors.backLink) shouldBe back
    }

    "have the correct back link location" in {
      element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.redirect().url
    }
  }

  "The change pending view" should {

    lazy val view = injectedView("changePending.ppob")(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct information in the first paragraph including ppob change" in {
      elementText(Selectors.paragraphOne) shouldBe ChangePendingMessages.p1 + ChangePendingMessages.ppobChange
    }
  }

  "The change pending view" should {

    lazy val view = injectedView("changePending.landline")(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct information in the first paragraph including landine change" in {
      elementText(Selectors.paragraphOne) shouldBe ChangePendingMessages.p1 + ChangePendingMessages.landlineChange
    }
  }

  "The change pending view" should {

    lazy val view = injectedView("changePending.mobile")(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct information in the first paragraph including mobile change" in {
      elementText(Selectors.paragraphOne) shouldBe ChangePendingMessages.p1 + ChangePendingMessages.mobileChange
    }
  }

  "The change pending view" should {

    lazy val view = injectedView("changePending.website")(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct information in the first paragraph including website change" in {
      elementText(Selectors.paragraphOne) shouldBe ChangePendingMessages.p1 + ChangePendingMessages.websiteChange
    }
  }

}
