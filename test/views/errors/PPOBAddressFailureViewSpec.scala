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

class PPOBAddressFailureViewSpec extends ViewBaseSpec with BaseMessages with TestUtil {

  val injectedView: PPOBAddressFailureView = inject[PPOBAddressFailureView]

  val testId = "test-id"

  object Selectors {
    val heading = "h1"
    val paragraphOne = "#content > p"
    val backLink = "body > div > a"
    val buttonTryAgain = "#content > div > a:nth-child(1)"
    val buttonCancel = "#content > div > a.govuk-button.govuk-button--secondary"
  }

  "The PPOB addresss failure view" should {

    lazy val view = injectedView(testId)(user, messages, mockConfig)
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

    "a back link" which {
      "should have the correct text" in {
        elementText(Selectors.backLink) shouldBe PPOBAddressFailureMessages.back
      }
      "should the correct location" in {
        element(Selectors.backLink).attr("href") shouldBe mockConfig.addressLookUpConfirmUrl(testId)
      }
    }
    "a try again button" which {
      "should have the correct text" in {
        elementText(Selectors.buttonTryAgain) shouldBe PPOBAddressFailureMessages.button1
      }
      "should have the correct location" in {
        element(Selectors.buttonTryAgain).attr("href") shouldBe controllers.routes.BusinessAddressController.initialiseJourney.url
      }
    }
    "a cancel button" which {
      "should have the correct text" in {
        elementText(Selectors.buttonCancel) shouldBe PPOBAddressFailureMessages.button2
      }
      "should have the correct location" in {
        element(Selectors.buttonCancel).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url
      }
    }
  }
}
