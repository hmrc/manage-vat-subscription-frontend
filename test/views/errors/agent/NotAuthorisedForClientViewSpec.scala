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

package views.errors.agent

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class NotAuthorisedForClientViewSpec extends ViewBaseSpec {

  "Rendering the unauthorised page" should {

    object Selectors {
      val pageHeading = "#content h1"
      val instructions = "#content p:nth-of-type(1)"
      val instructionsLink = "#content p:nth-of-type(1) > a"
      val tryAgain = "#content p:nth-of-type(2)"
      val tryAgainLink = "#content p:nth-of-type(2) > a"
      val button = "#content .button"
    }

    lazy val view = views.html.errors.agent.notAuthorisedForClient()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title" in {
      document.title shouldBe "You’re not authorised for this client"
    }

    s"have a the correct page heading" in {
      elementText(Selectors.pageHeading) shouldBe "You’re not authorised for this client"
    }

    s"have the correct instructions on the page" in {
      elementText(Selectors.instructions) shouldBe "To use this service, your client needs to authorise you as their agent."
    }

    s"have a link to GOV.UK guidance" in {
      element(Selectors.instructionsLink).attr("href") shouldBe "appoint-tax-agent"
    }

    s"have the correct content for trying again" in {
      elementText(Selectors.tryAgain) shouldBe "If you think you have entered the wrong details you can try again."
    }

    s"have a link to '${controllers.agentClientRelationship.routes.SelectClientVrnController.show().url}'" in {
      element(Selectors.tryAgainLink).attr("href") shouldBe controllers.agentClientRelationship.routes.SelectClientVrnController.show().url
    }

    s"have a Sign out button" in {
      elementText(Selectors.button) shouldBe "Sign out"
    }

    s"have a link to sign out" in {
      element(Selectors.button).attr("href") shouldBe controllers.routes.SignOutController.signOut(authorised = false).url
    }
  }
}
