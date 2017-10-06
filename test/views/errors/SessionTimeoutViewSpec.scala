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

package views.errors

import controllers.ControllerBaseSpec
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages}
import play.api.test.FakeRequest
import common.MessageLookup.SessionTimeout._
import mocks.MockAppConfig

class SessionTimeoutViewSpec extends ControllerBaseSpec {

  "Rendering the sessionTimeout view" should {

    lazy val mockConfig: MockAppConfig = new MockAppConfig {
      override val ggServiceUrl: String = "sign-in"
    }

    lazy val messages: Messages = Messages(Lang("en-GB"), messagesApi)
    lazy val view = views.html.errors.sessionTimeout()(FakeRequest(), messages, mockConfig)
    lazy val document: Document = Jsoup.parse(view.body)

    "have the correct title" in {
      document.title shouldBe title
    }

    "have the correct page heading" in {
      document.select("#content h1").text() shouldBe title
    }

    "have the correct instructions on the page" in {
      document.select("#content p").text() shouldBe instructions
    }

    "contain a link to sign in" in {
      document.select("#content p a").attr("href") shouldBe "sign-in"
    }
  }
}
