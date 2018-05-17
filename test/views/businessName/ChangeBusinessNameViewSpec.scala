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

package views.businessName

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import assets.BaseTestConstants.organisationName
import assets.messages.{ChangeBusinessNamePageMessages => viewMessages}
import assets.messages.BaseMessages

class ChangeBusinessNameViewSpec extends ViewBaseSpec {

  "Rendering the Change Business Name page" should {

    lazy val view = views.html.businessName.change_business_name(organisationName)(request, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    s"have the correct document title of '${viewMessages.title}'" in {
      document.title shouldBe viewMessages.title
    }

    s"have a the correct page heading of '${viewMessages.h1}'" in {
      elementText("h1") shouldBe viewMessages.h1
    }

    s"have a the correct p1 of '${viewMessages.p1(organisationName)}'" in {
      elementText("#p1") shouldBe viewMessages.p1(organisationName)
    }

    s"have a the correct panel indent of '${viewMessages.tradingNameMessage}'" in {
      elementText("#tradingNameMessage") shouldBe viewMessages.tradingNameMessage
    }

    s"have a the correct p2 of '${viewMessages.p2}'" in {
      elementText("#p2") shouldBe viewMessages.p2
    }

    "have a continue link" which {

      s"has the text '${viewMessages.link}'" in {
        elementText("#continue") shouldBe viewMessages.link
      }

      "has a URL to the Gov.UK guidance page for changing name via COHO" in {
        element("#continue").attr("href") shouldBe mockConfig.govUkCohoNameChangeUrl
      }

    }

  }
}
