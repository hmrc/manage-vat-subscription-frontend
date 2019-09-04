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

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class GovUkWrapperSpec extends ViewBaseSpec {

  "Gov Uk Wrapper" when {

    "accessibilityReport feature switch is on" should {

      mockConfig.features.accessibilityReportFeature(true)

      lazy val view = views.html.govuk_wrapper(appConfig = mockConfig , title = "test") (request = request , messages = messages)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "contain a link to the Accessibility statement" which {
        val selector = ".platform-help-links > li:nth-child(2) > a"

        "contains the correct URL" in {
          element(selector).attr("href") shouldBe mockConfig.accessibilityReportUrl
        }
      }
    }
  }
}