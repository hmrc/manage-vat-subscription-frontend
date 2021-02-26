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
  val exampleAddress: PPOBAddress = PPOBAddress(
    "Line 1",
    Some("Line 2"),
    Some("Line 3"),
    Some("Line 4"),
    Some("Line 5"),
    Some("P05T C0D3"),
    "GB"
  )

  "The RenderAddress helper" when {

    "all fields are populated" should {
      lazy val view = injectedView(exampleAddress)
      lazy val document = Jsoup.parse(view.body)

      "render address lines 1, 2 and the postcode with line breaks in between" in {
        document.select("p").html() shouldBe "Line 1 <br> Line 2 <br> P05T C0D3"
      }
    }

    "address line 2 and postcode are not present" should {
      lazy val view = injectedView(exampleAddress.copy(line2 = None, postCode = None))
      lazy val document = Jsoup.parse(view.body)

      "render the first address line, with no line break" in {
        document.select("p").html() shouldBe "Line 1"
      }
    }

    "address line 2 is not present" should {
      lazy val view = injectedView(exampleAddress.copy(line2 = None))
      lazy val document = Jsoup.parse(view.body)

      "render the first address line and postcode, with a line break in between" in {
        document.select("p").html() shouldBe "Line 1 <br> P05T C0D3"
      }
    }

    "postcode is not present" should {
      lazy val view = injectedView(exampleAddress.copy(postCode = None))
      lazy val document = Jsoup.parse(view.body)

      "render the first and second address lines, with a line break in between" in {
        document.select("p").html() shouldBe "Line 1 <br> Line 2"
      }
    }
  }
}
