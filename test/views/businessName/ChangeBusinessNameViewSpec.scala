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

package views.businessName

import assets.CustomerDetailsTestConstants.orgName
import assets.messages.{BaseMessages,ChangeBusinessNamePageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class ChangeBusinessNameViewSpec extends ViewBaseSpec with BaseMessages {

  "Rendering the Change Business Name page" should {

    object Selectors {
      val wrapper = "#content"
      val pageHeading = s"$wrapper h1"
      val p1 = s"$wrapper article p:nth-of-type(1)"
      val p2 = s"$wrapper article p:nth-of-type(2)"
      val p3 = s"$wrapper article p:nth-of-type(3)"
      val bullet1 = s"$wrapper li:nth-of-type(1)"
      val bullet2 = s"$wrapper li:nth-of-type(2)"
      val p4 = s"$wrapper article p:nth-of-type(4)"
      val bullet3 = s"$wrapper ul:nth-child(8) > li:nth-child(1)"
      val formLink = s"$wrapper ul:nth-child(8) > li:nth-child(1) > a"
      val bullet4 = s"$wrapper ul:nth-child(8) > li:nth-child(2)"
      val bullet5 = s"$wrapper ul:nth-child(8) > li:nth-child(3)"
      val link = s"$wrapper #continue"
      val backLink = ".link-back"
    }

    lazy val view = views.html.businessName.change_business_name(orgName)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a the back link with correct text and url '${back}'" in {
      elementText(Selectors.backLink) shouldBe back
      element(Selectors.backLink).attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url
    }

    s"have a the correct page heading of '${viewMessages.heading}'" in {
      elementText(Selectors.pageHeading) shouldBe viewMessages.heading
    }

    s"have a the correct p1 of '${viewMessages.p1(orgName)}'" in {
      elementText(Selectors.p1) shouldBe viewMessages.p1(orgName)
    }

    s"have a the correct p2 of '${viewMessages.p2}'" in {
      elementText(Selectors.p2) shouldBe viewMessages.p2
    }

    s"have a the correct p3 of '${viewMessages.p3}'" in {
      elementText(Selectors.p3) shouldBe viewMessages.p3
    }

    s"have a the correct bullet1 of '${viewMessages.bullet1}'" in {
      elementText(Selectors.bullet1) shouldBe viewMessages.bullet1
    }

    s"have a the correct bullet2 of '${viewMessages.bullet2}'" in {
      elementText(Selectors.bullet2) shouldBe viewMessages.bullet2
    }

    s"have a the correct p4 of '${viewMessages.p4}'" in {
      elementText(Selectors.p4) shouldBe viewMessages.p4
    }

    "have a secondary bullet point section" which {

      "has the first bullet point" which {

        s"have a the correct text of '${viewMessages.bullet3}'" in {
          elementText(Selectors.bullet3) shouldBe viewMessages.bullet3
        }

        "has a link" which {

          s"has the correct URL to '${mockConfig.govUkVat484Form}'" in {
            element(Selectors.formLink).attr("href") shouldBe mockConfig.govUkVat484Form
          }

          "opens in a new tab" in {
            element(Selectors.formLink).attr("target") shouldBe "_blank"
          }
        }
      }

      s"has a the correct bullet of '${viewMessages.bullet4}'" in {
        elementText(Selectors.bullet4) shouldBe viewMessages.bullet4
      }

      s"has a the correct bullet of '${viewMessages.bullet5}'" in {
        elementText(Selectors.bullet5) shouldBe viewMessages.bullet5
      }
    }

    "have a continue link" which {

      s"has the text '${viewMessages.continueLink}'" in {
        elementText(Selectors.link) shouldBe viewMessages.continueLink
      }

      "has a URL to the Gov.UK guidance page for changing name via COHO" in {
        element(Selectors.link).attr("href") shouldBe controllers.routes.ChangeBusinessNameController.handOffToCOHO().url
      }
    }
  }
}
