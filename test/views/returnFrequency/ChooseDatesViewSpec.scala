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
import forms.chooseDatesForm
import models.returnFrequency._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.Form
import views.ViewBaseSpec

class ChooseDatesViewSpec extends ViewBaseSpec {

  "Rendering the Choose dates page with no errors" should {

    val form: Form[ReturnDatesModel] = chooseDatesForm.datesForm

    lazy val view = views.html.returnFrequency.chooseDates(form,Jan)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.ChoosePage.heading}'" in {
      document.title shouldBe viewMessages.ChoosePage.heading
    }

    s"have a the correct page heading of '${viewMessages.ChoosePage.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.ChoosePage.heading
    }

    s"should not display an error" in {
      document.select("#error-summary-display").isEmpty shouldBe true
    }

    s"have a the correct current return dates of '${viewMessages.ChoosePage.question} ${viewMessages.option1Jan}'" in {
      elementText("#currently-set-text") shouldBe viewMessages.ChoosePage.question
      elementText("#currently-set-period") shouldBe viewMessages.option1Jan
    }

    s"have a the correct options return dates of" in {
      elementText("fieldset > div:nth-of-type(1) > label") shouldBe viewMessages.option2Feb
      elementText("fieldset > div:nth-of-type(2) > label") shouldBe viewMessages.option3Mar
      elementText("fieldset > div:nth-of-type(3) > label") shouldBe viewMessages.option4Monthly
    }


    s"have a continue button has the text '${BaseMessages.continue}'" in {
      elementText("#continue") shouldBe BaseMessages.continue
    }
    s"have a the back link with correct text and url '${BaseMessages.back}'" in {
      elementText(".link-back") shouldBe BaseMessages.back
      element(".link-back").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url
    }
  }


  "Rendering the Choose dates page with errors" should {

    val form: Form[ReturnDatesModel] = chooseDatesForm.datesForm.bind(Map("period-option" -> ""))

    lazy val view = views.html.returnFrequency.chooseDates(form,Monthly)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.ChoosePage.heading}'" in {
      document.title shouldBe viewMessages.ChoosePage.heading
    }

    s"have a the correct page heading of '${viewMessages.ChoosePage.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.ChoosePage.heading
    }

    s"should display an error" in {
      elementText("#error-summary-display") shouldBe s"${BaseMessages.errorHeading} ${viewMessages.ChoosePage.error}"
    }

    s"have a the correct current return dates of '${viewMessages.ChoosePage.question} ${viewMessages.option4Monthly}'" in {
      elementText("#currently-set-text") shouldBe viewMessages.ChoosePage.question
      elementText("#currently-set-period") shouldBe viewMessages.option4Monthly
    }

    s"have a the correct options return dates of" in {
      elementText("fieldset > div:nth-of-type(1) > label") shouldBe viewMessages.option1Jan
      elementText("fieldset > div:nth-of-type(2) > label") shouldBe viewMessages.option2Feb
      elementText("fieldset > div:nth-of-type(3) > label") shouldBe viewMessages.option3Mar
    }
  }
}
