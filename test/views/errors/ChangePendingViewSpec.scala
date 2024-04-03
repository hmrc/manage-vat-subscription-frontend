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

package views.errors

import assets.messages.{BaseMessages, ChangePendingMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import utils.TestUtil
import views.ViewBaseSpec
import views.html.errors.ChangePendingView


class ChangePendingViewSpec extends ViewBaseSpec with BaseMessages with TestUtil {

  val injectedView: ChangePendingView = inject[ChangePendingView]

  object Selectors {
    val heading = "h1"
    val paragraphOne = "#content p:nth-child(3)"
    val paragraphTwo = "#content p:nth-child(4)"
    val backLink = "#content a"
  }

  "The change pending view" should {

    lazy val view = injectedView()(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct title" in {
      document.title shouldBe ChangePendingMessages.title
    }

    "have the correct heading" in {
      elementText(Selectors.heading) shouldBe ChangePendingMessages.heading
    }

    "have the correct information in the first paragraph including email change" in {
      elementText(Selectors.paragraphOne) shouldBe ChangePendingMessages.para1
    }

    "have the correct information in the second paragraph" in {
      elementText(Selectors.paragraphTwo) shouldBe ChangePendingMessages.para2
    }

    "have the correct text for the back link" in {
      elementText(Selectors.backLink) shouldBe ChangePendingMessages.btaLink
    }

    "have the correct back link location" in {
      element(Selectors.backLink).attr("href") shouldBe mockConfig.btaAccountDetails
    }
  }
}
