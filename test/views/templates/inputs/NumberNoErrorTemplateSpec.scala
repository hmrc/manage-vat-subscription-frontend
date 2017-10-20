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

import forms.test.DateInputForm
import play.api.data.Field
import play.twirl.api.Html
import views.templates.TemplateBaseSpec

class NumberNoErrorTemplateSpec extends TemplateBaseSpec {

  "Rendering the numberNoError input" should {

    val fieldName = "dateDay"
    val label = "Day"
    val formClass = "day"
    val inputClass = "error-field"
    val value = "12"
    val field = Field(DateInputForm.form, fieldName, Seq(), None, Seq(), Some(value))

    val expectedMarkup = Html(
      s"""
         |  <label for="$fieldName" class="form-group form-group-$formClass" >
         |    <span>$label</span>
         |    <input type="number" class="form-control input--xsmall input--no-spinner $inputClass" name="$fieldName" id="$fieldName" value="$value"/>
         |  </label>
         |
        """.stripMargin
    )

    val markup = views.html.templates.inputs.numberNoError(field, label, formClass, inputClass)

    "generate the correct markup" in {
      formatHtml(markup) shouldBe formatHtml(expectedMarkup)
    }

  }
}
