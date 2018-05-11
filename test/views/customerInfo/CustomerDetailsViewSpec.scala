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

package views.customerInfo

import assets.CustomerDetailsTestConstants._
import assets.messages.{CustomerDetailsPageMessages => viewMessages}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class CustomerDetailsViewSpec extends ViewBaseSpec {

  "Rendering the Customer Details page" when {

    "Viewing for any user" should {

      lazy val view = views.html.customerInfo.customer_details(individual)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      s"have the correct document title '${viewMessages.title}'" in {
        document.title shouldBe viewMessages.title
      }

      s"have a the correct page heading '${viewMessages.h1}'" in {
        elementText("h1") shouldBe viewMessages.h1
      }

      s"have a the correct page subheading '${viewMessages.subheading}'" in {
        elementText("#sub-heading") shouldBe viewMessages.subheading
      }

      s"have a paragraph p1 with '${viewMessages.p1}'" in {
        elementText("#p1") shouldBe viewMessages.p1
      }

      s"have a bullet1 with '${viewMessages.bullet1}'" in {
        elementText("#bullet1") shouldBe viewMessages.bullet1
      }

      s"have a bullet2 with '${viewMessages.bullet2}'" in {
        elementText("#bullet2") shouldBe viewMessages.bullet2
      }

      s"have a section heading (h2) with '${viewMessages.h2}'" in {
        elementText("h2") shouldBe viewMessages.h2
      }
    }

    "Viewing for an Self-Employed Individual" should {

      lazy val view = views.html.customerInfo.customer_details(individual)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#individualNameHeading") shouldBe viewMessages.individualNameHeading
        }

        s"has the value '${individual.userName.get}'" in {
          elementText("#individualName") shouldBe individual.userName.get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#changeIndividualName") shouldBe viewMessages.change + " " + viewMessages.changeIndividualHidden
          }

          //TODO: Update this when URL developed and known
          "has a link to '#'" in {
            element("#changeIndividualName").attr("href") shouldBe "#"
          }
        }
      }
    }

    "Viewing for an Organisation" should {

      lazy val view = views.html.customerInfo.customer_details(organisation)(request, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a change details section for the Business Name" which {

        s"has the heading '${viewMessages.organisationNameHeading}'" in {
          elementText("#businessNameHeading") shouldBe viewMessages.organisationNameHeading
        }

        s"has the value '${organisation.organisationName.get}'" in {
          elementText("#businessName") shouldBe organisation.organisationName.get
        }

        "has a change link" which {

          s"has the wording '${viewMessages.change}'" in {
            elementText("#changeBusinessName") shouldBe viewMessages.change + " " + viewMessages.changeBusinessHidden
          }

          s"has a link to '${controllers.routes.ChangeBusinessNameController.show().url}'" in {
            element("#changeBusinessName").attr("href") shouldBe controllers.routes.ChangeBusinessNameController.show().url
          }
        }
      }
    }
  }
}
