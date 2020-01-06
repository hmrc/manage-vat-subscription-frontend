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

package views.helpers

import assets.BaseTestConstants.vrn
import assets.CircumstanceDetailsTestConstants._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec
import views.html.helpers.MtdSectionHelper

class MakingTaxDigitalSectionSpec extends ViewBaseSpec {

  val injectedView: MtdSectionHelper = inject[MtdSectionHelper]

  "The Making Tax Digital section" when {

    "the user's client has an opt out request pending" should {

      lazy val view = injectedView(customerInformationPendingOptOut, vrn)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct row title" in {
        elementText("#opt-in-text") shouldBe "Status"
      }

      "have the correct value" in {
        elementText("#opt-in") shouldBe "Opted out"
      }

      "has the correct status text" in {
        elementText("#opt-in-status") shouldBe "Pending"
      }
    }

    "the user's client has an opt in request pending" should {

      lazy val view = injectedView(customerInformationModelMaxOrganisationPending, vrn)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct row title" in {
        elementText("#opt-in-text") shouldBe "Status"
      }

      "have the correct value" in {
        elementText("#opt-in") shouldBe "Opted in"
      }

      "has the correct status text" in {
        elementText("#opt-in-status") shouldBe "Pending"
      }
    }

    "the user's client is opted out of MTD" should {

      lazy val view = injectedView(customerInformationNonMtd, vrn)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct row title" in {
        elementText("#opt-in-text") shouldBe "Status"
      }

      "have the correct value" in {
        elementText("#opt-in") shouldBe "Opted out"
      }

      "have a link" which {

        "has the correct link text" in {
          elementText("#opt-in-status") shouldBe "Sign up"
        }

        "has the correct link location" in {
          element("#opt-in-status").attr("href") shouldBe mockConfig.mtdSignUpUrl(vrn)
        }
      }
    }

    "the user's client has a NonDigital mandation status" should {

      lazy val view = injectedView(customerInformationNonDigital, vrn)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct row title" in {
        elementText("#opt-in-text") shouldBe "Status"
      }

      "have the correct value" in {
        elementText("#opt-in") shouldBe "Opted out"
      }

      "have a link" which {

        "has the correct link text" in {
          elementText("#opt-in-status") shouldBe "Sign up"
        }

        "has the correct link location" in {
          element("#opt-in-status").attr("href") shouldBe mockConfig.mtdSignUpUrl(vrn)
        }
      }
    }

    "the user's client is opted in to MTD" should {

      lazy val view = injectedView(customerInformationModelMin, vrn)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct row title" in {
        elementText("#opt-in-text") shouldBe "Status"
      }

      "have the correct value" in {
        elementText("#opt-in") shouldBe "Opted in"
      }

      "have a link" which {

        "has the correct link text" in {
          elementText("#opt-in-status") shouldBe "Opt out"
        }

        "has the correct link location" in {
          element("#opt-in-status").attr("href") shouldBe mockConfig.vatOptOutUrl
        }
      }
    }
  }
}
