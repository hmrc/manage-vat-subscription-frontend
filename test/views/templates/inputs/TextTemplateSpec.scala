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

import play.api.data.Field
import play.twirl.api.Html
import testOnly.forms.test.TextInputForm
import views.html.templates.inputs.Text
import views.templates.TemplateBaseSpec

class TextTemplateSpec extends TemplateBaseSpec {

  val injectedView: Text = inject[Text]

  "Rendering the text input" when {

    val fieldName = "fieldName"
    val labelText = "labelText"
    val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("text"))

    "the field is not populated and hint text is supplied" should {

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset aria-describedby="form-hint">
           |  <div class="form-field">
           |    <h1 id="page-heading"><label for="$fieldName" class="heading-large">$labelText</label></h1>
           |      <input class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="text">
           |    </div>
           |  </fieldset>
           |</div>
           |
        """.stripMargin
      )

      val markup = injectedView(field, labelText, None)

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
           |<div class="form-group">
           |  <fieldset aria-describedby="form-hint">
           |  <div class="form-field">
           |    <h1 id="page-heading"><label for="$fieldName" class="heading-large">$labelText</label></h1>
           |      <input class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="text value">
           |    </div>
           |  </fieldset>
           |</div>
           |
        """.stripMargin
      )

      val markup = injectedView(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "the field is populated with invalid data" should {

      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("text"))

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset aria-describedby="form-hint">
           |  <div class="form-field">
           |    <h1 id="page-heading"><label for="$fieldName" class="heading-large">$labelText</label></h1>
           |      <input class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="text">
           |    </div>
           |  </fieldset>
           |</div>
           |
        """.stripMargin
      )

      val markup = injectedView(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }

    "hint text is not supplied" should {

      val field: Field = Field(TextInputForm.form, fieldName, Seq(), None, Seq(), Some("text"))

      val expectedMarkup = Html(
        s"""
           |
           |<div class="form-group">
           |  <fieldset aria-describedby="form-hint">
           |  <div class="form-field">
           |    <h1 id="page-heading"><label for="$fieldName" class="heading-large">$labelText</label></h1>
           |      <input class="form-control input--no-spinner" name="$fieldName" id="$fieldName" value="text">
           |    </div>
           |  </fieldset>
           |</div>
           |
        """.stripMargin
      )

      val markup = injectedView(field, labelText, None)

      "generate the correct markup" in {
        formatHtml(markup) shouldBe formatHtml(expectedMarkup)
      }
    }
  }
}
