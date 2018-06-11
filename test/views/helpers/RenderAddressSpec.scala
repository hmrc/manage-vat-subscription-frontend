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

package views.helpers

import models.circumstanceInfo._
import org.jsoup.Jsoup
import views.ViewBaseSpec

class RenderAddressSpec extends ViewBaseSpec {

  "The RenderAddress helper" when {

    "all lines of an address if all are populated" should {
      lazy val address = PPOBAddress(
        Some("1"),
        Some("2"),
        Some("3"),
        Some("4"),
        Some("5"),
        Some("6"),
        Some("GB")
      )

      lazy val view = views.html.helpers.render_address(address)(messages, frontendAppConfig)
      lazy val document = Jsoup.parse(view.body)


      "Render address lines 1 to 6" in {
        for (i <- 1 to 6) {
          document.select(s"li:nth-child($i)").text shouldBe s"$i"
        }
      }

      "Render the correct Country for the Postcode" in {
        document.select(s"li:nth-child(7)").text shouldBe "United Kingdom"
      }
    }

    "Render no lines of an address if none are populated" in {
      val address = PPOBAddress(None, None, None, None, None, None, None)

      val view = views.html.helpers.render_address(address)(messages, frontendAppConfig)
      val document = Jsoup.parse(view.body)

      for (i <- 1 to 7) {
        document.select(s"li:nth-child($i)").text shouldBe ""
      }
    }
  }
}
