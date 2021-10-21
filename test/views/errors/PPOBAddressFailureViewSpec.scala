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

package views.errors

import assets.messages.{BaseMessages, PPOBAddressFailureMessages}
import utils.TestUtil
import views.ViewBaseSpec
import views.html.errors.PPOBAddressFailureView
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PPOBAddressFailureViewSpec extends ViewBaseSpec with BaseMessages with TestUtil{

  val injectedView: PPOBAddressFailureView = inject[PPOBAddressFailureView]

  object Selectors {
    val heading = "h1"
    val paragraphOne = "#content > p"
    val backLink = "body > div > a"
    val buttonTryAgain = "#content > div > a:nth-child(1)"
    val buttonCancel = "#content > div > a.govuk-button.govuk-button--secondary"
  }

  "The PPOB addresss failure view" should {

    lazy val view = injectedView()(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct title" in {
      document.title shouldBe PPOBAddressFailureMessages.title
    }

    "have the correct heading" in {
      elementText(Selectors.heading) shouldBe PPOBAddressFailureMessages.heading
    }

    "have the correct text in the paragraph" in {
      elementText(Selectors.paragraphOne) shouldBe PPOBAddressFailureMessages.paragraph
    }

    "have the correct text for the back link" in {
      elementText(Selectors.backLink) shouldBe PPOBAddressFailureMessages.back
    }
    "have the correct href for the back link" in {
      element(Selectors.backLink).attr("href") shouldBe controllers.routes.BusinessAddressController.confirmation(user.redirectSuffix).url
    }
    "have the correct button text for Try Again" in {
      elementText(Selectors.buttonTryAgain) shouldBe PPOBAddressFailureMessages.button1
    }
    "have the correct href link for Try Again" in {
      element(Selectors.buttonTryAgain).attr("href") shouldBe controllers.routes.BusinessAddressController.initialiseJourney.url
    }
    "have the correct button text for Cancel" in {
      elementText(Selectors.buttonCancel) shouldBe PPOBAddressFailureMessages.button2
    }
    "have the correct href link for Cancel" in {
      element(Selectors.buttonCancel).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url
    }
  }
}
