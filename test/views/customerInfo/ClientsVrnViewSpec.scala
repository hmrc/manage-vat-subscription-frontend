/*
 * Copyright 2018 HM Revenue & Customs
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

package views.customerInfo

import assets.messages.{BaseMessages, ClientVrnPageMessages => viewMessages}
import forms.VrnInputForm
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class ClientsVrnViewSpec extends ViewBaseSpec {

  "Rendering the Change Clients VRN page" should {

    lazy val view = views.html.customerInfo.clients_vrn(VrnInputForm.form)(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have the correct page heading of '${viewMessages.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.heading
    }

    s"have the correct p1 of '${viewMessages.p1}'" in {
      elementText("#p1") shouldBe viewMessages.p1
    }

    s"have the correct form hint of '${viewMessages.hint}'" in {
      elementText(".form-hint") shouldBe viewMessages.hint
    }

    s"have an input box for the VRN" in {
      element("#vrn-input").tagName() shouldBe "input"
    }

    "have a continue button" which {

      s"has the text '${BaseMessages.continue}'" in {
        elementText("#continue-button") shouldBe BaseMessages.continue
      }

      "has the correct URL" in {
        element("#continue-button").attr("href") shouldBe "#"
      }

    }

  }
}
