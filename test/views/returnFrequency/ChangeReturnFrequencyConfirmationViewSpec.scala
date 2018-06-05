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

package views.returnFrequency

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import assets.messages.{BaseMessages, ReturnFrequencyMessages => viewMessages}

class ChangeReturnFrequencyConfirmationViewSpec extends ViewBaseSpec {

  "Rendering the Dates Received page" should {

    lazy val view = views.html.returnFrequency.change_return_frequency_confirmation()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a correct page heading of '${viewMessages.ReceivedPage.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.ReceivedPage.heading
    }

    s"have the correct h2 '${viewMessages.ReceivedPage.h2}'" in {
      elementText("h2") shouldBe viewMessages.ReceivedPage.h2
    }

    s"have the correct p1 of '${viewMessages.ReceivedPage.p1}'" in {
      paragraph(1) shouldBe viewMessages.ReceivedPage.p1
    }

    s"have the correct p2 of '${viewMessages.ReceivedPage.p2}'" in {
      paragraph(2) shouldBe viewMessages.ReceivedPage.p2
    }

    s"have the correct bullet1 of '${viewMessages.ReceivedPage.bullet1}'" in {
      bullet(1) shouldBe viewMessages.ReceivedPage.bullet1
    }

    s"have the correct p2 of '${viewMessages.ReceivedPage.bullet2}'" in {
      bullet(2) shouldBe viewMessages.ReceivedPage.bullet2
    }

    s"have the correct finish button" which {

      s"has the text '${BaseMessages.finish}'" in {
        elementText("#finish") shouldBe BaseMessages.finish
      }

      s"has link back to customer details page" in {
        element("#finish").attr("href") shouldBe controllers.routes.CustomerDetailsController.show().url
      }
    }
  }
}
