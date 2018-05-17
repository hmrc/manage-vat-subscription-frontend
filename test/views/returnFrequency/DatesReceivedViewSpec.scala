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

import assets.messages.{DatesReceivedPageMessages => viewMessages}
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

    s"have the correct p1 of '${viewMessages.p1}'" in {
      elementText("#p1") shouldBe viewMessages.p1
    }

    s"have the correct p2 of '${viewMessages.p2}'" in {
      elementText("#p2") shouldBe viewMessages.p2
    }

    s"have the correct p3" which {

      s"has the text '${viewMessages.p3}'" in {
        elementText("#p3") shouldBe viewMessages.p3
      }

      s"has link back to customer details page" in {
        element("#view-change-link").attr("href") shouldBe controllers.routes.CustomerDetailsController.show().url
      }

    }
  }
}
