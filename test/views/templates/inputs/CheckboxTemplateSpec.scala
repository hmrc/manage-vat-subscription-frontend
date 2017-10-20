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
import views.templates.TemplateBaseSpec
import views.html.templates.inputs.checkbox

class CheckboxTemplateSpec extends TemplateBaseSpec {
  val fieldName: String = "fieldName"
  val question: String = "question"
  val choices: Seq[String] = Seq("choice1", "choice2", "choice3", "choice4")

  def generateExpectedCheckboxMarkup(choice: String, checked: Boolean = false): String =
    s"""
       |
       |<div class="multiple-choice">
       |  <input id="${fieldName}-${choices.indexOf(choice)}" name="${fieldName}[${choices.indexOf(choice)}]" type="checkbox" value="$choice" ${if (checked) s"""checked="true"""" else ""}>
       |  <label for="${fieldName}-${choices.indexOf(choice)}">$choice</label>
       |</div>
       |
     """.stripMargin

  "Calling the checkbox helper" should {

    "render a list of checkbox options" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset>
           |
           |    <legend>
           |      <h1 class="heading-medium">$question</h1>
           |    </legend>
           |
           |      ${generateExpectedCheckboxMarkup("choice1")}
           |      ${generateExpectedCheckboxMarkup("choice2")}
           |      ${generateExpectedCheckboxMarkup("choice3")}
           |      ${generateExpectedCheckboxMarkup("choice4")}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkbox(field, question, choices)

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the checkbox helper with a choice pre-selected" should {

    "render a list of checkbox options with one pre-selected" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("choice4"))
      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset>
           |
           |    <legend>
           |      <h1 class="heading-medium">$question</h1>
           |    </legend>
           |
           |      ${generateExpectedCheckboxMarkup("choice1")}
           |      ${generateExpectedCheckboxMarkup("choice2")}
           |      ${generateExpectedCheckboxMarkup("choice3")}
           |      ${generateExpectedCheckboxMarkup("choice4", checked = true)}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkbox(field, question, choices)

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the checkbox helper with all choices pre-selected" should {

    "render a list of checkbox options with all of them pre-selected" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset>
           |
           |    <legend>
           |      <h1 class="heading-medium">$question</h1>
           |    </legend>
           |
           |      ${generateExpectedCheckboxMarkup("choice1", checked = true)}
           |      ${generateExpectedCheckboxMarkup("choice2", checked = true)}
           |      ${generateExpectedCheckboxMarkup("choice3", checked = true)}
           |      ${generateExpectedCheckboxMarkup("choice4", checked = true)}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkbox(field, question, choices, preSelected = true)

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }
}
