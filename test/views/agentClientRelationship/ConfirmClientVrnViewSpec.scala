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

package views.agentClientRelationship

import assets.{BaseTestConstants, CustomerDetailsTestConstants}
import assets.messages.{BaseMessages, ConfirmClientVrnPageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class ConfirmClientVrnViewSpec extends ViewBaseSpec {

  "The Confirm Change Client VRN page" when {

    "given an individial" should {

      lazy val view = views.html.agentClientRelationship.confirm_client_vrn(BaseTestConstants.vrn, CustomerDetailsTestConstants.individual)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title of '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have the correct page heading of '${viewMessages.heading}'" in {
        elementText("h1") shouldBe viewMessages.heading
      }

      s"have the correct heading and text for the business name section" in {
        elementText("h2") shouldBe viewMessages.name
        elementText("article > p:nth-of-type(1)") shouldBe CustomerDetailsTestConstants.individual.userName.get
      }

      s"have the correct heading and text for the VAT number section" in {
        elementText("h3") shouldBe viewMessages.vrn
        elementText("article > p:nth-of-type(2)") shouldBe BaseTestConstants.vrn
      }

      s"have a confirm button" which {

        s"has the text '${BaseMessages.confirm}'" in {
          elementText("a.button") shouldBe BaseMessages.confirmAndContinue
        }

        s"has a link to '${controllers.routes.CustomerCircumstanceDetailsController.show().url}'" in {
          element("a.button").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show().url
        }
      }

      "have an edit different client link" which {

        s"has the text '${viewMessages.edit}'" in {
          elementText("article > p > a") shouldBe viewMessages.edit
        }

        "has the correct URL" in {
          element("article > p > a").attr("href") shouldBe controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
        }
      }
    }

  }
}
