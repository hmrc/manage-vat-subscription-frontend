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

package views.helpers

import models.circumstanceInfo._
import org.jsoup.Jsoup
import views.ViewBaseSpec
import views.html.helpers.RenderAddress

class RenderAddressSpec extends ViewBaseSpec {

  val injectedView: RenderAddress = inject[RenderAddress]

  "The RenderAddress helper" when {

    "all lines of an address if all are populated" should {
      lazy val address = PPOBAddress(
        "1",
        Some("2"),
        Some("3"),
        Some("4"),
        Some("5"),
        Some("6"),
        "GB"
      )

      lazy val view = injectedView(address)
      lazy val document = Jsoup.parse(view.body)

      "Render address lines 1 and 2" in {
        for (i <- 1 to 2) {
          document.select(s"li:nth-child($i)").text shouldBe s"$i"
        }
      }
    }

    "Render only lines of an address that are populated" should {
      val address = PPOBAddress("1", None, None, None, None, None, "GB")

      val view = injectedView(address)
      val document = Jsoup.parse(view.body)

      "Render the first address line" in {
        document.select(s"li:nth-child(1)").text shouldBe "1"
      }
      "Not render the 2nd address line" in {
        document.select(s"li:nth-child(2)").text shouldBe ""
      }
      "Not render the postcode" in {
        document.select(s"li:nth-child(3)").text shouldBe ""
      }
    }

    "Render only lines of an address that are populated and are required to display" should {
      val address = PPOBAddress("1", Some("2"), None, None, None, None, "GB")

      val view = injectedView(address)
      val document = Jsoup.parse(view.body)

      "Render the first address line" in {
        document.select(s"li:nth-child(1)").text shouldBe "1"
      }
      "Render the 2nd address line" in {
        document.select(s"li:nth-child(2)").text shouldBe "2"
      }
      "Not render the country code" in {
        document.select(s"li:nth-child(3)").text shouldBe ""

      }
    }

    "Render only lines of an address that are populated and are required to display with a full address" should {
      val address = PPOBAddress("1",
        Some("2"),
        Some("3"),
        Some("4"),
        Some("5"),
        Some("6"),
        "GB")

      val view = injectedView(address)
      val document = Jsoup.parse(view.body)

      "Render the first address line" in {
        document.select(s"li:nth-child(1)").text shouldBe "1"
      }
      "Render the 2nd address line" in {
        document.select(s"li:nth-child(2)").text shouldBe "2"
      }
      "Render the postcode as line 3" in {
        document.select(s"li:nth-child(3)").text shouldBe "6"
      }
      "Not render the 4th address line" in {
        document.select(s"li:nth-child(4)").text shouldBe ""
      }
      "Not render the 5th address line" in {
        document.select(s"li:nth-child(5)").text shouldBe ""
      }
      "Not render the 6th address line" in {
        document.select(s"li:nth-child(6)").text shouldBe ""
      }
    }
  }
}
