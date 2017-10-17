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
import forms.test.DateInputForm
import org.jsoup.Jsoup
import play.api.data.Field
import play.api.i18n.{Lang, Messages}
import play.twirl.api.Html

class DateSpec extends ControllerBaseSpec {

  "Rendering the date input" when {

    lazy implicit val messages: Messages = Messages(Lang("en-GB"), messagesApi)

    def formatHtml(body: Html): String = Jsoup.parseBodyFragment(s"\n$body\n").toString.trim

    val fieldName = "fieldName"
    val labelText = "labelText"
    val hintText = "hintText"
    val errorMessage = "errorMessage"

    "the field is not populated and hint text is supplied" should {

      val field: Field = Field(DateInputForm.form, fieldName, Seq(), None, Seq(), None)

      "generate the correct markup" in {

      }
    }
  }
}
