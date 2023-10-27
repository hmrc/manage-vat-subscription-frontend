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

package views.customerInfo

import assets.messages.BaseMessages
import mocks.services.MockServiceInfoService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import utils.TestUtil
import views.ViewBaseSpec
import views.html.customerInfo.changeTradingUnderNiProtocol

class ChangeTradingUnderNiProtocolSpec extends ViewBaseSpec with BaseMessages with MockServiceInfoService with TestUtil {

  "Rendering the change trading under NI Protocol page for an Individual" when {
    val injectedView: changeTradingUnderNiProtocol = inject[changeTradingUnderNiProtocol]
    lazy implicit val document: Document = Jsoup.parse(injectedView.apply()(user, messages, mockConfig).body)

    "have the correct document title" in {
      document.title should include("Change your Northern Ireland trading status")
    }

    "have the correct heading" in {
      document.select("#page-heading").text() shouldBe "Change your Northern Ireland trading status"
    }

    "have the correct paragraph" in {
      document.select("#change_trading_under_ni_protocol_p1").text() shouldBe "HMRC needs to know if you have changed your trading status under the terms of the Northern Ireland Protocol."
      document.select("#change_trading_under_ni_protocol_p2").text() shouldBe "You trade under the protocol if any of the following apply:"
    }

    "have the correct bullet points" in {
      document.select("#change_trading_under_ni_protocol_bp1").text() shouldBe "your goods are located in Northern Ireland at the time of sale"
      document.select("#change_trading_under_ni_protocol_bp2").text() shouldBe "you receive goods in Northern Ireland from VAT-registered EU businesses for business purposes"
      document.select("#change_trading_under_ni_protocol_bp3").text() shouldBe "you sell or move goods from Northern Ireland to an EU country"
    }

    "have the correct links" in {
      document.select("#change_trading_under_ni_protocol_link").text() shouldBe "Tell HMRC that your Northern Ireland trading status has changed"
      document.select("#change_trading_under_ni_protocol_link").attr("href") shouldBe "https://www.tax.service.gov.uk/submissions/new-form/withdraw-from-northern-ireland-protocol-vat"
    }

  }

  "Rendering the change trading under NI Protocol page for an agent" when {
    val injectedView: changeTradingUnderNiProtocol = inject[changeTradingUnderNiProtocol]
    lazy implicit val document: Document = Jsoup.parse(injectedView.apply()(agentUser, messages, mockConfig).body)

    "have the correct document title" in {
      document.title should include("Change your client’s Northern Ireland trading status")
    }

    "have the correct heading" in {
      document.select("#page-heading").text() shouldBe "Change your client’s Northern Ireland trading status"
    }

    "have the correct paragraph" in {
      document.select("#change_trading_under_ni_protocol_p1").text() shouldBe "HMRC needs to know if your client has changed their trading status under the terms of the NI Protocol."
      document.select("#change_trading_under_ni_protocol_p2").text() shouldBe "They trade under the protocol if any of the following apply:"
    }

    "have the correct bullet points" in {
      document.select("#change_trading_under_ni_protocol_bp1").text() shouldBe "their goods are located in Northern Ireland at the time of sale"
      document.select("#change_trading_under_ni_protocol_bp2").text() shouldBe "they receive goods in Northern Ireland from VAT-registered EU businesses for business purposes"
      document.select("#change_trading_under_ni_protocol_bp3").text() shouldBe "they sell or move goods from Northern Ireland to an EU country"
    }

    "have the correct links" in {
      document.select("#change_trading_under_ni_protocol_link").text() shouldBe "Tell HMRC that your client’s Northern Ireland trading status has changed"
      document.select("#change_trading_under_ni_protocol_link").attr("href") shouldBe "https://www.tax.service.gov.uk/submissions/new-form/withdraw-from-northern-ireland-protocol-vat"
    }

  }


}
