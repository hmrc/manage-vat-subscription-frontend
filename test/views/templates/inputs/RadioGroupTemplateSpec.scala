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

import forms.test.TextInputForm
import play.api.data.Field
import play.twirl.api.Html
import views.html.templates.inputs.radioGroup
import views.templates.TemplateBaseSpec

class RadioGroupTemplateSpec extends TemplateBaseSpec {

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
       |    <label for="$fieldName-$value"> $display </label>
       |  </div>
      """.stripMargin

  "Calling the radio group helper with an choice pre-selected" should {

    "render a list of radio options with one pre-checked" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("value2"))
      val expectedMarkup = Html(
        s"""
           |  <div class="form-group">
           |    <div class="">
           |      <fieldset class="inline">
           |        <legend>
           |          <h1 class="heading-medium">
           |            $labelText
           |          </h1>
           |        </legend>
           |        ${generateExpectedRadioMarkup("value1", "display1")}
           |        ${generateExpectedRadioMarkup("value2", "display2", true)}
           |        ${generateExpectedRadioMarkup("value3", "display3")}
           |        ${generateExpectedRadioMarkup("value4", "display4")}
           |        ${generateExpectedRadioMarkup("value5", "display5")}
           |      </fieldset>
           |    </div>
           |  </div>
        """.stripMargin
      )

      val markup = radioGroup(field, choices, labelText)
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the radio helper with a list of available choices" should {

    "render the choices as radio buttons" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |  <div class="form-group">
           |    <div class="">
           |     <fieldset class="inline">
           |
           |       <legend>
           |          <h1 class="heading-medium">
           |            $labelText
           |          </h1>
           |        </legend>
           |
           |        ${generateExpectedRadioMarkup("value1", "display1")}
           |        ${generateExpectedRadioMarkup("value2", "display2")}
           |        ${generateExpectedRadioMarkup("value3", "display3")}
           |        ${generateExpectedRadioMarkup("value4", "display4")}
           |        ${generateExpectedRadioMarkup("value5", "display5")}
           |
           |      </fieldset>
           |    </div>
           |  </div>
        """.stripMargin
      )

      val markup = radioGroup(field, choices, labelText)
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the radio helper with the stacked option enabled" should {

    "render the choices as radio buttons in a stacked layout" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |  <div class="form-group">
           |    <div class="">
           |      <fieldset class="">
           |
           |        <legend>
           |          <h1 class="heading-medium">
           |            $labelText
           |          </h1>
           |        </legend>
           |
           |        ${generateExpectedRadioMarkup("value1", "display1")}
           |        ${generateExpectedRadioMarkup("value2", "display2")}
           |        ${generateExpectedRadioMarkup("value3", "display3")}
           |        ${generateExpectedRadioMarkup("value4", "display4")}
           |        ${generateExpectedRadioMarkup("value5", "display5")}
           |
           |      </fieldset>
           |    </div>
           |  </div>
        """.stripMargin
      )

      val markup = radioGroup(field, choices, labelText, stacked = true)
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

}
