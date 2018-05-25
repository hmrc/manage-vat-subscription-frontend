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

package views.returnFrequency

import assets.messages.{BaseMessages, ReturnFrequencyMessages => viewMessages}
import models.returnFrequency._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class ConfirmDatesViewSpec extends ViewBaseSpec {

  "Rendering the Confirm Dates page" should {

    lazy val view = views.html.returnFrequency.confirm_dates(Jan)(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a the correct page heading of '${viewMessages.ConfirmPage.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.ConfirmPage.heading
    }

    s"have a the display the correct dates of" when {

      s"the current date is '${viewMessages.option1}'" in {
        lazy val view = views.html.returnFrequency.confirm_dates(Jan)(request, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe viewMessages.option1
      }

      s"the current date is '${viewMessages.option2}'" in {
        lazy val view = views.html.returnFrequency.confirm_dates(Feb)(request, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe viewMessages.option2
      }

      s"the current date is '${viewMessages.option3}'" in {
        lazy val view = views.html.returnFrequency.confirm_dates(Mar)(request, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe viewMessages.option3
      }

      s"the current date is '${viewMessages.option4}'" in {
        lazy val view = views.html.returnFrequency.confirm_dates(Monthly)(request, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe viewMessages.option4
      }
    }

    "have a link back to the change dates page" which {

      s"has the text '${viewMessages.ConfirmPage.changeLink}'" in {
        elementText("#change-vat-link") shouldBe viewMessages.ConfirmPage.changeLink
      }

      "has a URL back to the change dates page" in {
        element("#change-vat-link").attr("href") shouldBe "/vat-through-software/account/frequency"
      }

    }

    s"have a the correct p2 of '${viewMessages.ConfirmPage.p2}'" in {
      elementText("#p2") shouldBe viewMessages.ConfirmPage.p2
    }

    "have a confirm button" which {

      s"has the text '${BaseMessages.confirm}'" in {
        elementText("#continue-button") shouldBe BaseMessages.confirm
      }

      "posts data to the server" in {
        element("form").attr("method") shouldBe "POST"
      }

      "posts data to the correct endpoint" in {
        element("form").attr("action") shouldBe "/vat-through-software/account/confirm-dates"
      }

    }
  }
}