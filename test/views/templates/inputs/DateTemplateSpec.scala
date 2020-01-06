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

package views.templates.inputs

import play.twirl.api.Html
import testOnly.forms.test.DateInputForm
import views.html.templates.inputs.Date
import views.templates.TemplateBaseSpec

class DateTemplateSpec extends TemplateBaseSpec {

  val injectedView: Date = inject[Date]

  "Rendering the date input" when {

    val fieldName = "date"
    val hintText = "hintText"
    val question = "Enter a date"

    "the fields are not populated and hint text is supplied" should {

      val form = DateInputForm.form

      val expectedMarkup = Html(
        s"""
           |
           |<fieldset id="$fieldName-fieldset" class="form-group form-date ">
           |
           |  <legend>
           |    <h1>$question</h1>
           |  </legend>
           |
           |  <span class="form-hint">
           |    $hintText
           |  </span>
           |
           |  <label for="${fieldName}Day" class="form-group form-group-day" >
           |    <span>Day</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Day" id="${fieldName}Day" value=""/>
           |  </label>
           |
           |  <label for="${fieldName}Month" class="form-group form-group-month" >
           |    <span>Month</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Month" id="${fieldName}Month" value=""/>
           |  </label>
           |
           |  <label for="${fieldName}Year" class="form-group form-group-year" >
           |    <span>Year</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Year" id="${fieldName}Year" value=""/>
           |  </label>
           |
           |</fieldset>
           |
        """.stripMargin
      )

      val markup = injectedView(form, question, fieldName, Some(hintText), renderQuestionAsHeading = true)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the fields are not populated and hint text is not supplied" should {

      val form = DateInputForm.form

      val expectedMarkup = Html(
        s"""
           |
           |<fieldset id="$fieldName-fieldset" class="form-group form-date ">
           |
           |  <legend>
           |    <h1>$question</h1>
           |  </legend>
           |
           |  <label for="${fieldName}Day" class="form-group form-group-day" >
           |    <span>Day</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Day" id="${fieldName}Day" value=""/>
           |  </label>
           |
           |  <label for="${fieldName}Month" class="form-group form-group-month" >
           |    <span>Month</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Month" id="${fieldName}Month" value=""/>
           |  </label>
           |
           |  <label for="${fieldName}Year" class="form-group form-group-year" >
           |    <span>Year</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Year" id="${fieldName}Year" value=""/>
           |  </label>
           |
           |</fieldset>
           |
        """.stripMargin
      )

      val markup = injectedView(form, question, fieldName, None, renderQuestionAsHeading = true)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "there are multiple data items on the page" should {

      val form = DateInputForm.form.bind(
        Map(
          "dateDay" -> "1",
          "dateMonth" -> "2",
          "dateYear" -> "3"
        )
      )

      val expectedMarkup = Html(
        s"""
           |
           |<fieldset id="$fieldName-fieldset" class="form-group form-date ">
           |
           |  <legend class="form-label">$question</legend>
           |
           |  <label for="${fieldName}Day" class="form-group form-group-day" >
           |    <span>Day</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Day" id="${fieldName}Day" value="1"/>
           |  </label>
           |
           |  <label for="${fieldName}Month" class="form-group form-group-month" >
           |    <span>Month</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Month" id="${fieldName}Month" value="2"/>
           |  </label>
           |
           |  <label for="${fieldName}Year" class="form-group form-group-year" >
           |    <span>Year</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Year" id="${fieldName}Year" value="3"/>
           |  </label>
           |
           |</fieldset>
           |
        """.stripMargin
      )

      val markup = injectedView(form, question, fieldName, None, renderQuestionAsHeading = false)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the fields are populated with valid data" should {

      val form = DateInputForm.form.bind(
        Map(
        "dateDay" -> "1",
        "dateMonth" -> "2",
        "dateYear" -> "3"
        )
      )

      val expectedMarkup = Html(
        s"""
           |
           |<fieldset id="$fieldName-fieldset" class="form-group form-date ">
           |
           |  <legend>
           |    <h1>$question</h1>
           |  </legend>
           |
           |  <label for="${fieldName}Day" class="form-group form-group-day" >
           |    <span>Day</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Day" id="${fieldName}Day" value="1"/>
           |  </label>
           |
           |  <label for="${fieldName}Month" class="form-group form-group-month" >
           |    <span>Month</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Month" id="${fieldName}Month" value="2"/>
           |  </label>
           |
           |  <label for="${fieldName}Year" class="form-group form-group-year" >
           |    <span>Year</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner " name="${fieldName}Year" id="${fieldName}Year" value="3"/>
           |  </label>
           |
           |</fieldset>
           |
        """.stripMargin
      )

      val markup = injectedView(form, question, fieldName, None, renderQuestionAsHeading = true)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "one field is invalid" should {

      val form = DateInputForm.form.bind(
        Map(
          "dateDay" -> "",
          "dateMonth" -> "2",
          "dateYear" -> "3"
        )
      )

      val dateError = "Enter a day"

      val expectedMarkup = Html(
        s"""
           |
           |<fieldset id="$fieldName-fieldset" class="form-group form-date form-field--error">
           |
           |  <legend>
           |    <h1>$question</h1>
           |  </legend>
           |
           |  <span class="error-notification" role="tooltip"> $dateError </span>
           |
           |  <label for="${fieldName}Day" class="form-group form-group-day" >
           |    <span>Day</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner error-field" name="${fieldName}Day" id="${fieldName}Day" value=""/>
           |  </label>
           |
           |  <label for="${fieldName}Month" class="form-group form-group-month" >
           |    <span>Month</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner error-field" name="${fieldName}Month" id="${fieldName}Month" value="2"/>
           |  </label>
           |
           |  <label for="${fieldName}Year" class="form-group form-group-year" >
           |    <span>Year</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner error-field" name="${fieldName}Year" id="${fieldName}Year" value="3"/>
           |  </label>
           |
           |</fieldset>
           |
        """.stripMargin
      )

      val markup = injectedView(form, question, fieldName, None, renderQuestionAsHeading = true)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "more than one fields are invalid" should {

      val form = DateInputForm.form.bind(
        Map(
          "dateDay" -> "1",
          "dateMonth" -> "",
          "dateYear" -> ""
        )
      )

      val dateError = "Enter a valid date"

      val expectedMarkup = Html(
        s"""
           |
           |<fieldset id="$fieldName-fieldset" class="form-group form-date form-field--error">
           |
           |  <legend>
           |    <h1>$question</h1>
           |  </legend>
           |
           |  <span class="error-notification" role="tooltip"> $dateError </span>
           |
           |  <label for="${fieldName}Day" class="form-group form-group-day" >
           |    <span>Day</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner error-field" name="${fieldName}Day" id="${fieldName}Day" value="1"/>
           |  </label>
           |
           |  <label for="${fieldName}Month" class="form-group form-group-month" >
           |    <span>Month</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner error-field" name="${fieldName}Month" id="${fieldName}Month" value=""/>
           |  </label>
           |
           |  <label for="${fieldName}Year" class="form-group form-group-year" >
           |    <span>Year</span>
           |    <input type="number" class="form-control input--xsmall input--no-spinner error-field" name="${fieldName}Year" id="${fieldName}Year" value=""/>
           |  </label>
           |
           |</fieldset>
           |
        """.stripMargin
      )

      val markup = injectedView(form, question, fieldName, None, renderQuestionAsHeading = true)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }
  }
}
