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

package views.missingTrader

import assets.PPOBAddressTestConstants.ppobAddressModelMax
import assets.messages.{ConfirmBusinessAddressMessages => viewMessages}
import forms.MissingTraderForm
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.missingTrader.ConfirmBusinessAddressView

class ConfirmBusinessAddressViewSpec extends ViewBaseSpec {

  object Selectors {
    val pageHeading = "h1"
    val yesOption = "div.govuk-radios__item:nth-child(1) > label:nth-child(2)"
    val noOption = "div.govuk-radios__item:nth-child(2) > label:nth-child(2)"
    val button = ".govuk-button"
    val error = ".govuk-error-message"
    val address = ".govuk-inset-text"
    val additionalInfo = ".vatvc-grey-paragraph-text"
    val form = "form"
    val formQuestion = "legend"
    val errorSummaryHeading = ".govuk-error-summary h2"
    val errorSummaryList = ".govuk-error-summary__list"
  }

  val injectedView: ConfirmBusinessAddressView = inject[ConfirmBusinessAddressView]

  "The ConfirmBusinessAddressView" should {

    lazy val view: Html = injectedView(ppobAddressModelMax, MissingTraderForm.missingTraderForm)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe viewMessages.title
    }

    "display the correct heading" in {
      elementText(Selectors.pageHeading) shouldBe viewMessages.heading
    }

    "has the correct address output" in {
      elementText(Selectors.address) shouldBe
        s"${ppobAddressModelMax.line1} ${ppobAddressModelMax.line2.get} ${ppobAddressModelMax.postCode.get}"
    }

    s"have the correct additional information" in {
      elementText(Selectors.additionalInfo) shouldBe viewMessages.additionalInformation
    }

    s"have the correct question" in {
      elementText(Selectors.formQuestion) shouldBe viewMessages.question
    }

    "have the correct radio buttons with yes/no answers" in {
      elementText(Selectors.yesOption) shouldBe viewMessages.yes
      elementText(Selectors.noOption) shouldBe viewMessages.no
    }

    "have a button" which {

      "has the correct text" in {
        elementText(Selectors.button) shouldBe viewMessages.continue
      }

      "has the prevent double click attribute" in {
        element(Selectors.button).hasAttr("data-prevent-double-click") shouldBe true
      }
    }

    "have the correct form action" in {
      element(Selectors.form).attr("action") shouldBe controllers.missingTrader.routes.ConfirmAddressController.submit.url
    }

    "not display an error" in {
      elementExtinct(Selectors.errorSummaryHeading)
      elementExtinct(Selectors.errorSummaryList)
      elementExtinct(Selectors.error)
    }
  }

  "The ConfirmBusinessAddress page with errors" should {

    lazy val view = injectedView(
      ppobAddressModelMax, MissingTraderForm.missingTraderForm.bind(Map("yes_no" -> "")))(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe s"${viewMessages.errorTitlePrefix} ${viewMessages.title}"
    }

    "display an error summary" which {

      "has the correct heading" in {
        elementText(Selectors.errorSummaryHeading) shouldBe viewMessages.errorHeading
      }

      "has the correct error message in the list" in {
        elementText(Selectors.errorSummaryList) shouldBe viewMessages.errorMessage
      }
    }

    "display the correct error message" in {
      elementText(Selectors.error) shouldBe s"${viewMessages.errorTitlePrefix} ${viewMessages.errorMessage}"
    }
  }
}
