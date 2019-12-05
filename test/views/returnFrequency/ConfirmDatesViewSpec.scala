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

package views.returnFrequency

import assets.messages.{BaseMessages, ReturnFrequencyMessages => viewMessages}
import models.returnFrequency._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import views.html.returnFrequency.ConfirmDatesView
class ConfirmDatesViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: ConfirmDatesView = inject[ConfirmDatesView]

  "Rendering the Confirm Dates page" should {

    lazy val view = injectedView(Jan)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.ConfirmPage.title}'" in {
      document.title shouldBe viewMessages.ConfirmPage.title
    }
    s"have a the back link with correct text and url '$back'" in {
      elementText(".link-back") shouldBe back
    }

    s"have a the correct page heading of '${viewMessages.ConfirmPage.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.ConfirmPage.heading
    }

    s"have a the display the correct dates of" when {

      s"the current date is '${viewMessages.option1Jan}'" in {
        lazy val view = injectedView(Jan)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe s"${viewMessages.ConfirmPage.newDates} ${viewMessages.option1Jan}"
      }

      s"the current date is '${viewMessages.option2Feb}'" in {
        lazy val view = injectedView(Feb)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe s"${viewMessages.ConfirmPage.newDates} ${viewMessages.option2Feb}"
      }

      s"the current date is '${viewMessages.option3Mar}'" in {
        lazy val view = injectedView(Mar)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe s"${viewMessages.ConfirmPage.newDates} ${viewMessages.option3Mar}"
      }

      s"the current date is '${viewMessages.option4Monthly}'" in {
        lazy val view = injectedView(Monthly)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)
        elementText("#p1") shouldBe s"${viewMessages.ConfirmPage.newDates} ${viewMessages.option4Monthly}"
      }
    }

    "have a link back to the change dates page" which {

      s"has the text '${viewMessages.ConfirmPage.changeLink}'" in {
        elementText("#change-vat-link") shouldBe viewMessages.ConfirmPage.changeLink
      }

    }

    "have a confirm button" which {

      s"has the text '$confirmAndContinue'" in {
        elementText("#continue-button") shouldBe confirmAndContinue
      }


    }
  }
}
