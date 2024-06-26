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

package views.helpers

import assets.messages.ReturnFrequencyMessages
import models.returnFrequency._
import views.ViewBaseSpec
import views.html.helpers.RenderReturnFrequency

class RenderReturnFrequencySpec extends ViewBaseSpec {

  val injectedView: RenderReturnFrequency = inject[RenderReturnFrequency]

  "The RenderReturnFrequency helper" should {

    "Render the correct text for a Jan quarter" in {
      val view = injectedView(Jan)(messages)
      view.body.trim shouldBe ReturnFrequencyMessages.option1Jan
    }

    "Render the correct text for a Feb quarter" in {
      val view = injectedView(Feb)(messages)
      view.body.trim shouldBe ReturnFrequencyMessages.option2Feb
    }

    "Render the correct text for a March quarter" in {
      val view = injectedView(Mar)(messages)
      view.body.trim shouldBe ReturnFrequencyMessages.option3Mar
    }

    "Render the correct text for a Monthly quarter" in {
      val view = injectedView(Monthly)(messages)
      view.body.trim shouldBe ReturnFrequencyMessages.option4Monthly
    }

    "Render the correct text for Annual" in {
      val view = injectedView(Annual)(messages)
      view.body.trim shouldBe ReturnFrequencyMessages.option5Annually
    }
  }
}
