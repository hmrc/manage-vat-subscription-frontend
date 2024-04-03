/*
 * Copyright 2024 HM Revenue & Customs
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

package views.missingTrader

import assets.messages.{BaseMessages, MissingTraderAddressConfirmationPageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.missingTrader.MissingTraderAddressConfirmationView

class MissingTraderAddressConfirmationViewSpec extends ViewBaseSpec with BaseMessages {

  val injectedView: MissingTraderAddressConfirmationView = inject[MissingTraderAddressConfirmationView]

  "Loading the Confirmation page as a User" when {

    lazy val view: Html = injectedView()(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct page heading of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have the correct h1 of '${viewMessages.h1}'" in {
      elementText("h1") shouldBe viewMessages.h1
    }

    s"have the correct p1 of '${viewMessages.p1}'" in {
      paragraph(1) shouldBe viewMessages.p1
    }

    s"have a button to finish" which {

      s"has the correct text of '$continue'" in {
        elementText(".govuk-button") shouldBe continue
      }

      "has the correct link" in {
        element(".govuk-button").attr("href") shouldBe mockConfig.vatSummaryUrl
      }
    }
  }

  "Loading the Confirmation page as an Agent" when {

    lazy val view: Html = injectedView()(agentUser, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct page heading of '${viewMessages.titleAgent}'" in {
      document.title shouldBe viewMessages.titleAgent
    }

    s"have the correct h1 of '${viewMessages.h1}'" in {
      elementText("h1") shouldBe viewMessages.h1
    }

    s"have the correct p1 of '${viewMessages.p1}'" in {
      paragraph(1) shouldBe viewMessages.p1
    }

    s"have a button to finish" which {

      s"has the correct text of '$continue'" in {
        elementText(".govuk-button") shouldBe continue
      }

      "has the correct link" in {
        element(".govuk-button").attr("href") shouldBe mockConfig.agentClientLookupAgentAction
      }
    }
  }
}
