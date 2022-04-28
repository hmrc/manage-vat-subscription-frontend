/*
 * Copyright 2022 HM Revenue & Customs
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

import org.jsoup.Jsoup
import views.ViewBaseSpec
import views.html.helpers.SignupBanner
import messages.SignupBannerMessages._

class SignupBannerSpec extends ViewBaseSpec {

  val injectedView = inject[SignupBanner]

  object Selectors {
    val banner = ".govuk-notification-banner"
    val title = "#govuk-notification-banner-title"
    val subheading = ".govuk-notification-banner__heading"
    val p1 = ".govuk-body > span"
    val link = "p > a"
  }

  "The SignupBanner helper" when {

    "the showBanner boolean is true" should {

      lazy val view = injectedView(showBanner = true)
      lazy implicit val document = Jsoup.parse(view.body)

      "display a banner" that {

        "has the correct title" in {
          elementText(Selectors.title) shouldBe important
        }

        "has the correct subheading" in {
          elementText(Selectors.subheading) shouldBe subheading
        }

        "has the correct text" in {
          elementText(Selectors.p1) shouldBe p1
        }

        "has a link" that {

          "has the correct text" in {
            elementText(Selectors.link) shouldBe link
          }

          "has the correct href" in {
            element(Selectors.link).attr("href") shouldBe linkHref
          }
        }
      }
    }

    "the showBanner boolean is false" should {

      lazy val view = injectedView(showBanner = false)
      lazy implicit val document = Jsoup.parse(view.body)

      "not display a banner" in {
        elementExtinct(Selectors.banner)
      }
    }
  }
}
