/*
 * Copyright 2021 HM Revenue & Customs
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

import play.api.data.{Field, FormError}
import play.twirl.api.Html
import testOnly.forms.test.TextInputForm
import views.html.templates.inputs.RadioGroup
import views.templates.TemplateBaseSpec

class RadioGroupTemplateSpec extends TemplateBaseSpec {

  val injectedView: RadioGroup = inject[RadioGroup]

  val fieldName = "fieldName"
  val labelText = "labelText"
  val hintText = "hintText"
  val errorMessage = "error message"
  val choices: Seq[(String, String)] = Seq(
    "value1" -> "display1",
    "value2" -> "display2",
    "value3" -> "display3",
    "value4" -> "display4",
    "value5" -> "display5"
  )

  def generateExpectedRadioMarkup(value: String, display: String, checked: Boolean = false): String =
    s"""
       |  <div class="multiple-choice">
       |    <input type="radio" id="$fieldName-$value" name="$fieldName" value="$value"${if (checked) " checked" else ""}>
       |    <label for="$fieldName-$value">$display</label>
       |  </div>
      """.stripMargin

  "Calling the radio helper with no choice pre-selected" should {

    "render the choices as radio buttons" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |<div id="yes_no" class="form-group">
           |  <fieldset>
           |    <legend class="heading-medium h2-margin">
           |      $labelText
           |    </legend>
           |
           |    <div>
           |      ${generateExpectedRadioMarkup("value1", "display1")}
           |      ${generateExpectedRadioMarkup("value2", "display2")}
           |      ${generateExpectedRadioMarkup("value3", "display3")}
           |      ${generateExpectedRadioMarkup("value4", "display4")}
           |      ${generateExpectedRadioMarkup("value5", "display5")}
           |    </div>
           |   </fieldset>
           |  </div>
        """.stripMargin
      )

      val markup = injectedView(field, choices, labelText, None)
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the radio group helper with a choice pre-selected" should {

    "render a list of radio options with one pre-checked" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("value2"))
      val expectedMarkup = Html(
        s"""
           |<div id="yes_no" class="form-group">
           |  <fieldset>
           |    <legend class="heading-medium h2-margin">
           |      $labelText
           |    </legend>
           |
           |    <div>
           |      ${generateExpectedRadioMarkup("value1", "display1")}
           |      ${generateExpectedRadioMarkup("value2", "display2", checked = true)}
           |      ${generateExpectedRadioMarkup("value3", "display3")}
           |      ${generateExpectedRadioMarkup("value4", "display4")}
           |      ${generateExpectedRadioMarkup("value5", "display5")}
           |    </div>
           |  </fieldset>
           |</div>
        """.stripMargin
      )

      val markup = injectedView(field, choices, labelText, None)
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the radio group helper with an error" should {

    "render an error" in {
      val errorMessage = "Error message"
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(FormError("text", errorMessage)), None)
      val expectedMarkup = Html(
        s"""
           |<div id="yes_no" class="form-group form-group-error panel-border-narrow">
           |  <fieldset aria-describedby="form-error">
           |    <legend class="heading-medium h2-margin">
           |      $labelText
           |    </legend>
           |
           |    <span id="form-error" class="error-message">
           |      <span class="visuallyhidden">Error:</span>
           |      $errorMessage
           |    </span>
           |
           |    <div>
           |      ${generateExpectedRadioMarkup("value1", "display1")}
           |      ${generateExpectedRadioMarkup("value2", "display2")}
           |      ${generateExpectedRadioMarkup("value3", "display3")}
           |      ${generateExpectedRadioMarkup("value4", "display4")}
           |      ${generateExpectedRadioMarkup("value5", "display5")}
           |    </div>
           |  </fieldset>
           |</div>
        """.stripMargin
      )

      val markup = injectedView(field, choices, labelText, None)
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the radio helper with additional content" should {

    "render the choices as radio buttons with additional content" in {
      val additionalContent = Html("<p>Additional text</p>")
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |<div id="yes_no" class="form-group">
           |  <fieldset>
           |    <legend class="heading-medium h2-margin">
           |      $labelText
           |    </legend>
           |
           |    $additionalContent
           |
           |    <div>
           |      ${generateExpectedRadioMarkup("value1", "display1")}
           |      ${generateExpectedRadioMarkup("value2", "display2")}
           |      ${generateExpectedRadioMarkup("value3", "display3")}
           |      ${generateExpectedRadioMarkup("value4", "display4")}
           |      ${generateExpectedRadioMarkup("value5", "display5")}
           |    </div>
           |  </fieldset>
           |</div>
        """.stripMargin
      )

      val markup = injectedView(field, choices, labelText, Some(additionalContent))
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }
}
