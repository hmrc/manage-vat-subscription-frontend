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
import play.api.data.{Field, FormError}
import play.twirl.api.Html
import views.templates.TemplateBaseSpec
import views.html.templates.inputs.checkboxList

class CheckboxListTemplateSpec extends TemplateBaseSpec {
  val fieldName: String = "fieldName"
  val question: String = "question"
  val choices: Seq[(String, String)] = Seq(("choice1", "value1"), ("choice2", "value2"), ("choice3", "value3"), ("choice4", "value4"))

  def generateExpectedCheckboxMarkup(choice: (String, String), checked: Boolean = false): String =
    s"""
       |
       |<div class="multiple-choice">
       |  <input id="${fieldName}-${choices.indexOf((choice._1, choice._2))}" name="${fieldName}[${choices.indexOf((choice._1, choice._2))}]" type="checkbox" value="${choice._2}" ${if (checked) "checked" else ""}>
       |  <label for="${fieldName}-${choices.indexOf((choice._1, choice._2))}">${choice._1}</label>
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
           |      <span class="form-label-bold">$question</span>
           |    </legend>
           |
           |      ${generateExpectedCheckboxMarkup(("choice1", "value1"))}
           |      ${generateExpectedCheckboxMarkup(("choice2", "value2"))}
           |      ${generateExpectedCheckboxMarkup(("choice3", "value3"))}
           |      ${generateExpectedCheckboxMarkup(("choice4", "value4"))}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkboxList(field, question, choices)

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the checkbox helper with a few options checked" should {

    "render a list of checkbox options with a few options checked" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("choice4"))
      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset>
           |
           |    <legend>
           |      <span class="form-label-bold">$question</span>
           |    </legend>
           |
           |      ${generateExpectedCheckboxMarkup(("choice1", "value1"))}
           |      ${generateExpectedCheckboxMarkup(("choice2", "value2"), checked = true)}
           |      ${generateExpectedCheckboxMarkup(("choice3", "value3"))}
           |      ${generateExpectedCheckboxMarkup(("choice4", "value4"), checked = true)}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkboxList(field, question, choices, Seq("value2", "value4"))

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the checkbox helper with all options checked" should {

    "render a list of checkbox options with all of them checked" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)
      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset>
           |
           |    <legend>
           |      <span class="form-label-bold">$question</span>
           |    </legend>
           |
           |      ${generateExpectedCheckboxMarkup(("choice1", "value1"), checked = true)}
           |      ${generateExpectedCheckboxMarkup(("choice2", "value2"), checked = true)}
           |      ${generateExpectedCheckboxMarkup(("choice3", "value3"), checked = true)}
           |      ${generateExpectedCheckboxMarkup(("choice4", "value4"), checked = true)}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkboxList(field, question, choices, Seq("value1", "value2", "value3", "value4"))

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }

  "Calling the checkbox helper with an error" should {

    "render an error message" in {
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None,
        Seq(FormError("example", Seq("Please select at least one option"))), None)
      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group form-field--error">
           |  <fieldset>
           |
           |    <legend>
           |      <span class="form-label-bold">$question</span>
           |    </legend>
           |
           |    <span class="error-notification" role="tooltip">${field.errors.head.message}</span>
           |
           |    ${generateExpectedCheckboxMarkup(("choice1", "value1"))}
           |    ${generateExpectedCheckboxMarkup(("choice2", "value2"))}
           |    ${generateExpectedCheckboxMarkup(("choice3", "value3"))}
           |    ${generateExpectedCheckboxMarkup(("choice4", "value4"))}
           |
           |  </fieldset>
           |</div>
           |
         """.stripMargin
      )

      val markup = checkboxList(field, question, choices)

      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }
  }
}
