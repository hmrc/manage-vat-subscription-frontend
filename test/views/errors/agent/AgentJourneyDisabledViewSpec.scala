/*
 * Copyright 2019 HM Revenue & Customs
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

/*
 * Copyright 2019 HM Revenue & Customs
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

import assets.messages.{BaseMessages, AgentJourneyDisabledPageMessages => Messages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class AgentJourneyDisabledViewSpec extends ViewBaseSpec with BaseMessages {

  "Rendering the unauthorised page" should {

    object Selectors {
      val serviceName = ".header__menu__proposition-name"
      val pageHeading = "#content h1"
      val p1 = "#content article p:nth-of-type(1)"
      val p2 = "#content article p:nth-of-type(2)"
      val button = "#content .button"
    }

    lazy val view = views.html.errors.agent.agent_journey_disabled()(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title" in {
      document.title shouldBe Messages.title
    }

    s"have the correct service name" in {
      elementText(Selectors.serviceName) shouldBe agentServiceName
    }

    s"have a the correct page heading" in {
      elementText(Selectors.pageHeading) shouldBe Messages.heading
    }

    s"have the correct info in the first paragraph on the page" in {
      elementText(Selectors.p1) shouldBe Messages.p1
    }

    s"have the correct info in the second paragraph on the page" in {
      elementText(Selectors.p2) shouldBe Messages.p2
    }

    s"have a Sign out button" in {
      elementText(Selectors.button) shouldBe signOut
    }

    s"have a link to sign out" in {
      element(Selectors.button).attr("href") shouldBe controllers.routes.SignOutController.signOut(authorised = true).url
    }
  }
}

