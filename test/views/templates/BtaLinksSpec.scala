/*
 * Copyright 2023 HM Revenue & Customs
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

package views.templates

import models.ListLinks
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.templates.BTALinks

class BtaLinksSpec extends ViewBaseSpec {

  val injectedView: BTALinks = inject[BTALinks]

  val messageslink: ListLinks = ListLinks("Messages", "/business-account/messages", Some("1"),Some(true))
  val manageAccountlink: ListLinks = ListLinks("Manage account", "/business-account/manage-account", Some("0"),Some(true))

  "BTALinks" should {

    object Selectors {
      val menuLink1 = ".hmrc-account-menu__main > li:nth-child(1) > a"
      val menuLink2 = ".hmrc-account-menu__main > li:nth-child(2) > a"
      val notificationBadge = ".hmrc-notification-badge"
    }

    "render a Messages link with a notification alert" which {

      val view: Html = injectedView(Seq(messageslink))
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "should have the text Messages and show the number of notifications" in {
        elementText(Selectors.menuLink1) shouldBe "Messages 1"
      }

      "should have the correct link to Messages" in {
        element(Selectors.menuLink1).attr("href") shouldBe "/business-account/messages"
      }

      "should display a notification badge" in {
        elementText(Selectors.notificationBadge) shouldBe "1"
      }
    }

      "render a Messages link without a notification alert" which {

        val noAltertlink: ListLinks = ListLinks("Messages", "/business-account/messages", Some("0"), Some(true))

        val view: Html = injectedView(Seq(noAltertlink))
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "should have the text Messages" in {
          elementText(Selectors.menuLink1) shouldBe "Messages"
        }

        "should not display a notification badge" in {
          elementExtinct(Selectors.notificationBadge)
        }

      }

      "render multiple links" which {

        val view: Html = injectedView(Seq(messageslink,manageAccountlink))
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "should have the correct link names" in {
          elementText(Selectors.menuLink1) shouldBe "Messages 1"
          elementText(Selectors.menuLink2) shouldBe "Manage account"
        }

        "should have the correct link URL's" in {
          element(Selectors.menuLink1).attr("href") shouldBe "/business-account/messages"
          element(Selectors.menuLink2).attr("href") shouldBe "/business-account/manage-account"
        }

      }

    "not render when required" which {

      val hideMenu: ListLinks = ListLinks("Messages", "/business-account/messages", Some("0"), Some(false))

      val view: Html = injectedView(Seq(hideMenu))
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "should not display the menu links" in {
        elementExtinct(Selectors.menuLink1)
      }

    }

  }
}
