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

package controllers.agentClientRelationship

import assets.messages.{ConfirmClientVrnPageMessages => messages}
import assets.CustomerDetailsTestConstants.customerDetailsMax
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.MockAuth
import mocks.services.MockCustomerDetailsService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ConfirmClientVrnControllerSpec extends ControllerBaseSpec with MockAuth with MockCustomerDetailsService {

  object TestConfirmClientVrnControllerSpec extends ConfirmClientVrnController(
    messagesApi,
    mockAgentOnlyAuthPredicate,
    mockCustomerDetailsService,
    app.injector.instanceOf[ServiceErrorHandler],
    mockAppConfig
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      lazy val result = TestConfirmClientVrnControllerSpec.show(fakeRequest)
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockCustomerDetailsSuccess(customerDetailsMax)
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the Confirm Client Vrn Page" in {
        document.select("h1").text shouldBe messages.heading
      }
    }

    unauthenticatedCheck(TestConfirmClientVrnControllerSpec.show)
  }

  "Calling the .submit action" when {

    "valid data is posted" should {

      lazy val request = FakeRequest("POST", "/")
      lazy val result = TestConfirmClientVrnControllerSpec.submit(request)

      "return 303" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "contain the correct location header" in {
        redirectLocation(result) shouldBe Some(controllers.routes.CustomerDetailsController.show().url)
      }
    }
  }

}
