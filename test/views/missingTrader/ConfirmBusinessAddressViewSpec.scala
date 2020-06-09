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

package views.missingTrader

import assets.PPOBAddressTestConstants.ppobAddressModelMin
import assets.messages.{ConfirmBusinessAddressMessages => viewMessages}
import forms.MissingTraderForm
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.missingTrader.ConfirmBusinessAddressView

class ConfirmBusinessAddressViewSpec extends ViewBaseSpec {

  object Selectors {
    val pageHeading = "#page-heading"
    val yesOption = "div.multiple-choice:nth-child(1) > label"
    val noOption = "div.multiple-choice:nth-child(2) > label"
    val button = ".button"
    val errorHeading = "#error-summary-display"
    val error = ".error-message"
  }

  val injectedView: ConfirmBusinessAddressView = inject[ConfirmBusinessAddressView]

  "The ConfirmBusinessAddressView" should {

    lazy val view: Html = injectedView(ppobAddressModelMin, MissingTraderForm.missingTraderForm)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe viewMessages.title
    }

    "display the correct heading" in {
      elementText(Selectors.pageHeading) shouldBe viewMessages.heading
    }

    s"have the correct question" in {
      elementText("h2") shouldBe viewMessages.question
    }

    "have the correct radio buttons with yes/no answers" in {
      elementText(Selectors.yesOption) shouldBe viewMessages.yes
      elementText(Selectors.noOption) shouldBe viewMessages.no
    }

    "have the correct continue button text" in {
      elementText(Selectors.button) shouldBe viewMessages.continue
    }

    "not display an error" in {
      document.select(Selectors.error).isEmpty shouldBe true
    }
  }

  "The ConfirmBusinessAddress page with errors" should {

    lazy val view = injectedView(ppobAddressModelMin, MissingTraderForm.missingTraderForm.bind(Map("yes_no" -> "")))(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe s"${viewMessages.errorTitlePrefix} ${viewMessages.title}"
    }

    "have the correct page heading" in {
      elementText(Selectors.pageHeading) shouldBe viewMessages.heading
    }

    "display the correct error heading" in {
      elementText(Selectors.errorHeading) shouldBe s"${viewMessages.errorHeading} ${viewMessages.errorMessage}"
    }

    "have the correct radio buttons with yes/no answers" in {
      elementText(Selectors.yesOption) shouldBe viewMessages.yes
      elementText(Selectors.noOption) shouldBe viewMessages.no
    }

    "have the correct continue button text" in {
      elementText(Selectors.button) shouldBe viewMessages.continue
    }

    "display the correct error messages" in {
      elementText(Selectors.error) shouldBe viewMessages.errorMessage
    }
  }

}
