/*
 * Copyright 2023 HM Revenue & Customs
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

import assets.messages.{BaseMessages, AgentUnauthorisedPageMessages => Messages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import views.html.errors.agent.UnauthorisedView

class UnauthorisedViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: UnauthorisedView = inject[UnauthorisedView]

  "Rendering the unauthorised page" should {

    object Selectors {
      val serviceName = ".hmrc-header__service-name"
      val pageHeading = "#content h1"
      val instructions = "#content p:eq(1)"
      val instructionsLink = "#content p:eq(1) > a"
      val button = "#content .govuk-button"
    }

    lazy val view = injectedView()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title" in {
      document.title shouldBe Messages.pageHeading + titleSuffixAgent
    }

    s"have the correct service name" in {
      elementText(Selectors.serviceName) shouldBe agentServiceName
    }

    s"have a the correct page heading" in {
      elementText(Selectors.pageHeading) shouldBe Messages.pageHeading
    }

    s"have the correct instructions on the page" in {
      elementText(Selectors.instructions) shouldBe Messages.instructions
    }

    s"have a link to GOV.UK guidance" in {
      element(Selectors.instructionsLink).attr("href") shouldBe "guidance/get-an-hmrc-agent-services-account"
    }

    s"have a Sign out button" in {
      elementText(Selectors.button) shouldBe signOut
    }

    s"have a link to sign out" in {
      element(Selectors.button).attr("href") shouldBe controllers.routes.SignOutController.signOut(authorised = false).url
    }
  }
}

