/*
 * Copyright 2017 HM Revenue & Customs
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

package views.templates.inputs

import forms.test.MoneyInputForm
import play.twirl.api.Html
import views.templates.TemplateBaseSpec
import views.html.templates.inputs.money

class MoneyTemplateSpec extends TemplateBaseSpec {

  private val validForm = MoneyInputForm.form.bind(Map("moneyInput" -> "100.00"))
  private val invalidForm = MoneyInputForm.form.bind(Map("moneyInput" -> "invalid data"))
  private val blankForm = MoneyInputForm.form
  
  "Rendering the money input" when {

    val fieldName = "moneyInput"
    val labelText = "labelText"
    val hintText = "hintText"

    "the field contains valid data and hint text is supplied" should {

      val validData: String = "100.00"

      val expectedMarkup = Html(
        s"""
           |<div class="form-group ">
           |
           |    <h1>
           |        <label for="$fieldName" class="heading-large">
           |          $labelText
           |        </label>
           |    </h1>
           |
           |    <span class="form-hint">
           |        $hintText
           |    </span>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding "
           |        name="$fieldName"
           |        id="$fieldName"
           |        value=$validData
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = money(validForm(fieldName), labelText, decimalPlace = true, Some(hintText))

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains valid data and no hint text is supplied" should {

      val validData: String = "100.00"

      val expectedMarkup = Html(
        s"""
           |<div class="form-group ">
           |
           |    <h1>
           |        <label for="$fieldName" class="heading-large">
           |          $labelText
           |        </label>
           |    </h1>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding "
           |        name="$fieldName"
           |        id="$fieldName"
           |        value="$validData"
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = money(validForm(fieldName), labelText, decimalPlace = true, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains valid data, no hint text is supplied and there no decimal places" should {

      val validData: String = "100.00"

      val expectedMarkup = Html(
        s"""
           |<div class="form-group ">
           |
           |    <h1>
           |        <label for="$fieldName" class="heading-large">
           |          $labelText
           |        </label>
           |    </h1>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding "
           |        name="$fieldName"
           |        id="$fieldName"
           |        value="$validData"
           |        step="1"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = money(validForm(fieldName), labelText, decimalPlace = false, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains no data and no hint text is supplied" should {

      val expectedMarkup = Html(
        s"""
           |<div class="form-group ">
           |
           |    <h1>
           |        <label for="$fieldName" class="heading-large">
           |          $labelText
           |        </label>
           |    </h1>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding "
           |        name="$fieldName"
           |        id="$fieldName"
           |        value=""
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = money(blankForm(fieldName), labelText, decimalPlace = true, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "there is more than one item on the page" should {
      val validData: String = "100.00"

      val expectedMarkup = Html(
        s"""
           |<div class="form-group ">
           |
           |    <label for="$fieldName" class="form-label">
           |        $labelText
           |    </label>
           |
           |    <span class="form-hint">
           |        $hintText
           |    </span>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding "
           |        name="$fieldName"
           |        id="$fieldName"
           |        value=$validData
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = money(validForm(fieldName), labelText, decimalPlace = true, Some(hintText), renderLabelAsHeading = false)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains invalid data" should {

      val errorMessage = "Not a bigDecimal"

      val expectedMarkup = Html(
        s"""
           |<div class="form-group form-field--error">
           |
           |    <h1>
           |        <label for="$fieldName" class="heading-large">
           |          $labelText
           |        </label>
           |    </h1>
           |
           |    <span class="error-notification"
           |        role="tooltip">
           |        $errorMessage
           |    </span>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding error-field"
           |        name="$fieldName"
           |        id="$fieldName"
           |        value="invalid data"
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = money(invalidForm(fieldName), labelText, decimalPlace = true, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }
  }
}
