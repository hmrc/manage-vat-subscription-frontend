/*
 * Copyright 2024 HM Revenue & Customs
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

package controllers

import mocks.services.MockServiceInfoService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._
import views.html.customerInfo.changeTradingUnderNiProtocol

class NiTeaderControllerSpec extends ControllerBaseSpec with MockServiceInfoService {
  object TestCustomerCircumstanceDetailsController extends NiTraderController(
    mockAuthPredicate,
    inject[changeTradingUnderNiProtocol],
    mcc
  )

  "Callint the changeNiTradingStatus action" should {
    "successfully render the Change your Northern Ireland trading status page" in {
      lazy val result = TestCustomerCircumstanceDetailsController.changeNiTradingStatus(request)
      lazy val document = Jsoup.parse(contentAsString(result))
      status(result) shouldBe Status.OK
      contentType(result) shouldBe Some("text/html")
      charset(result) shouldBe Some("utf-8")
      document.select("h1").text() shouldBe "Change your Northern Ireland trading status"
    }
  }

}
