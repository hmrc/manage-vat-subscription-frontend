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

package views.templates.inputs

import testOnly.forms.test.TextInputForm
import play.api.data.{Field, FormError}
import play.twirl.api.Html
import views.templates.TemplateBaseSpec

class TextTemplateSpec extends TemplateBaseSpec {

  "Rendering the text input" when {

    val fieldName = "fieldName"
    val labelText = "labelText"
    val hintText = "hintText"
    val errorMessage = "error message"

    "the field is not populated and hint text is supplied" should {

      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-field">
           |  <label for="$fieldName" class="  ">
           |      <span class="form-label visuallyhidden">
           |          $labelText
           |      </span>
           |      <span class="form-hint">
           |          $hintText
           |      </span>
           |      <input type="text" class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="">
           |  </label>
           |</div>
           |
        """.stripMargin
      )

      val markup = views.html.templates.inputs.text(field, labelText, Some(hintText))

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field is populated with valid data" should {

      val value = "text value"
      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some(value))

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-field">
           |  <label for="$fieldName" class="  ">
           |      <span class="form-label visuallyhidden">
           |          $labelText
           |      </span>
           |      <span class="form-hint">
           |          $hintText
           |      </span>
           |      <input type="text" class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="$value">
           |  </label>
           |</div>
           |
        """.stripMargin
      )

      val markup = views.html.templates.inputs.text(field, labelText, Some(hintText))

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field is populated with invalid data" should {

      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(FormError(fieldName, errorMessage)), Some(""))

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-field">
           |  <label for="$fieldName" class="  form-field--error">
           |      <span class="form-label visuallyhidden">
           |          $labelText
           |      </span>
           |      <span class="form-hint">
           |          $hintText
           |      </span>
           |      <span class="error-notification" role="tooltip" data-journey="search-page:error:$fieldName">
           |          $errorMessage
           |      </span>
           |      <input type="text" class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="">
           |  </label>
           |</div>
           |
        """.stripMargin
      )

      val markup = views.html.templates.inputs.text(field, labelText, Some(hintText))

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "hint text is not supplied" should {

      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), None)

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-field">
           |  <label for="$fieldName" class="  ">
           |      <span class="form-label visuallyhidden">
           |          $labelText
           |      </span>
           |      <input type="text" class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="">
           |  </label>
           |</div>
           |
        """.stripMargin
      )

      val markup = views.html.templates.inputs.text(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }
  }
}
