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

package views.businessAddress

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.{businessAddress => views}
import assets.messages.{ChangeAddressConfirmationPageMessages => viewMessages}
import assets.messages.BaseMessages

class ChangeAddressConfirmationViewSpec extends ViewBaseSpec {

  lazy val view: Html = views.change_address_confirmation()(request, messages, mockConfig)
  lazy implicit val document: Document = Jsoup.parse(view.body)

  s"have the correct document title of '${viewMessages.title}'" in {
    document.title shouldBe viewMessages.title
  }

  s"have the correct page heading of '${viewMessages.title}'" in {
    elementText("h1") shouldBe viewMessages.title
  }

  s"have the correct p1 of '${viewMessages.p1}'" in {
    paragraph(1) shouldBe viewMessages.p1
  }

  s"have the correct p2 of '${viewMessages.p2}'" in {
    paragraph(2) shouldBe viewMessages.p2
  }

  s"have a button to finish" which {

    s"has the correct text of '${BaseMessages.finish}" in {
      elementText("#finish") shouldBe BaseMessages.finish
    }

    s"has the correct link to '${controllers.routes.CustomerCircumstanceDetailsController.show().url}'" in {
      element("#finish").attr("href") shouldBe controllers.routes.CustomerCircumstanceDetailsController.show().url
    }
  }
}
