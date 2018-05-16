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

import assets.messages.{BaseMessages, DatesReceivedPageMessages => viewMessages}
import models.returnFrequency._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class DatesReceivedViewSpec extends ViewBaseSpec {

  "Rendering the Dates Received page" should {

    lazy val view = views.html.returnFrequency.dates_received()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a correct page heading of '${viewMessages.heading}'" in {
      elementText("#page-heading") shouldBe viewMessages.heading
    }

    s"have the correct sub heading of '${viewMessages.subheading}'" in {
      elementText("#sub-heading") shouldBe viewMessages.subheading
    }

    "have a link back to the change dates page" which {

      s"has the text '${viewMessages.changeLink}'" in {
        elementText("#change-vat-link") shouldBe viewMessages.changeLink
      }

      "has a URL back to the change dates page" in {
        element("#change-vat-link").attr("href") shouldBe "#"
      }

    }

    s"have a the correct p2 of '${viewMessages.p2}'" in {
      elementText("#p2") shouldBe viewMessages.p2
    }

    "have a confirm button" which {

      s"has the text '${BaseMessages.confirm}'" in {
        elementText("#continue-button") shouldBe BaseMessages.confirm
      }

      "has a URL to the change dates results page" in {
        element("#continue-button").attr("href") shouldBe "#"
      }

    }
  }
}
