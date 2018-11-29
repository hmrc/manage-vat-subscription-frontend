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

package views.errors

import assets.messages.{BaseMessages, EmailChangePendingMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class EmailChangePendingViewSpec extends ViewBaseSpec {

  object Selectors {
    val heading = "h1"
    val paragraphOne = "article > p:nth-child(3)"
    val paragraphTwo = "article > p:nth-child(4)"
    val backLink = ".link-back"
  }

  "The email change pending view" should {

    lazy val view = views.html.errors.emailChangePending()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct title" in {
      document.title shouldBe EmailChangePendingMessages.title
    }

    "have the correct heading" in {
      elementText(Selectors.heading) shouldBe EmailChangePendingMessages.heading
    }

    "have the correct information in the first paragraph" in {
      elementText(Selectors.paragraphOne) shouldBe EmailChangePendingMessages.p1
    }

    "have the correct information in the second paragraph" in {
      elementText(Selectors.paragraphTwo) shouldBe EmailChangePendingMessages.p2
    }

    "have the correct text for the back link" in {
      elementText(Selectors.backLink) shouldBe BaseMessages.back
    }

    "have the correct back link location" in {
      element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.redirect().url
    }
  }
}
