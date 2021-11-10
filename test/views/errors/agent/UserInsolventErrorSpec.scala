/*
 * Copyright 2021 HM Revenue & Customs
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

package views.errors.agent


import messages.UserInsolventMessages
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import views.html.errors.agent.UserInsolventError

class UserInsolventErrorSpec extends ViewBaseSpec {

  val userInsolvent: UserInsolventError = inject[UserInsolventError]

  "Rendering the unauthorised page" should {

    object Selectors {
      val pageHeading = "#insolvent-without-access-heading"
      val message = "#insolvent-without-access-body"
      val button = ".govuk-button"
    }

    lazy val view = userInsolvent()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe UserInsolventMessages.title
    }

    "have a the correct page heading" in {
      elementText(Selectors.pageHeading) shouldBe UserInsolventMessages.heading
    }

    "have the correct body" in {
      elementText(Selectors.message) shouldBe UserInsolventMessages.message
    }

    "have the correct button text" in {
      elementText(Selectors.button) shouldBe UserInsolventMessages.buttonText
    }

    "have the correct button link" in {
      element(Selectors.button).attr("href") shouldBe "bta-url"
    }

  }

}