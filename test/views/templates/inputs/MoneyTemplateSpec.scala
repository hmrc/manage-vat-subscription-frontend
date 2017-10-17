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

import controllers.ControllerBaseSpec
import forms.test.MoneyInputForm
import org.jsoup.Jsoup
import play.api.data.{Field, FormError}
import play.api.i18n.{Lang, Messages}
import play.twirl.api.Html

class MoneyTemplateSpec extends ControllerBaseSpec {

  lazy implicit val messages: Messages = Messages(Lang("en-GB"), messagesApi)

  def formatHtml(body: Html): String = Jsoup.parseBodyFragment(s"\n$body\n").toString.trim

  val fieldName = "fieldName"
  val labelText = "labelText"
  val hintText = "hintText"
  val errorMessage = "error message"

  "Rendering the money input" when {

    "the field contains valid data and hint text is supplied" should {

      val validData: String = "100.00"

      val field: Field = Field(MoneyInputForm.form, fieldName, Seq(), None, Seq(), Some(validData))

      val expectedMarkup = Html(
        s"""
           |<div for=$fieldName class=" form-group ">
           |
           |    <label for=$fieldName class="form-label visuallyhidden">
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
           |        class="input--no-spinner input--left-padding"
           |        name="$fieldName"
           |        id="$fieldName"
           |        value=$validData
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = views.html.templates.inputs.moneyInputWrapper(field, labelText, Some(hintText))

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains valid data and no hint text is supplied" should {

      val validData: String = "100.00"

      val field: Field = Field(MoneyInputForm.form, fieldName, Seq(), None, Seq(), Some(validData))

      val expectedMarkup = Html(
        s"""
           |<div for=$fieldName class=" form-group ">
           |
           |    <label for=$fieldName class="form-label visuallyhidden">
           |        $labelText
           |    </label>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding"
           |        name="$fieldName"
           |        id="$fieldName"
           |        value="$validData"
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = views.html.templates.inputs.moneyInputWrapper(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains no data and no hint text is supplied" should {

      val field: Field = Field(MoneyInputForm.form, fieldName, Seq(), None, Seq(), None)

      val expectedMarkup = Html(
        s"""
           |<div for=$fieldName class=" form-group ">
           |
           |    <label for=$fieldName class="form-label visuallyhidden">
           |        $labelText
           |    </label>
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding"
           |        name="$fieldName"
           |        id="$fieldName"
           |        value=""
           |        step="0.01"
           |    />
           |
           |</div>
         """.stripMargin)

      val markup = views.html.templates.inputs.moneyInputWrapper(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field contains invalid data" should {

      val field: Field = Field(MoneyInputForm.form, fieldName, Seq(), None, Seq(FormError(fieldName, errorMessage)), Some(""))

      val expectedMarkup = Html(
        s"""
           |<div for="$fieldName" class=" form-group form-field--error">
           |
           |
           |
           |            <span class="error-notification"
           |                role="tooltip">
           |                $errorMessage
           |            </span>
           |
           |
           |
           |    <label for="$fieldName" class="form-label visuallyhidden">
           |        labelText
           |    </label>
           |
           |
           |
           |    <span class="input-currency"></span>
           |
           |    <input
           |        type="number"
           |        class="input--no-spinner input--left-padding"
           |        name="$fieldName"
           |        id="$fieldName"
           |        value=""
           |        step="0.01"
           |    />
           |
           |</div>
           |
           |
         """.stripMargin)

      val markup = views.html.templates.inputs.moneyInputWrapper(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }
  }
}
