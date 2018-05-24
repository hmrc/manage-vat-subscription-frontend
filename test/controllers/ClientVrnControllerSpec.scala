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

package controllers

import assets.CustomerDetailsTestConstants.customerDetailsMax
import assets.messages.{ClientVrnPageMessages => messages}
import config.ServiceErrorHandler
import mocks.services.MockCustomerDetailsService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ClientVrnControllerSpec extends ControllerBaseSpec with MockCustomerDetailsService {

  object TestClientVrnControllerSpec extends ClientVrnController(
    messagesApi,
    mockAgentOnlyAuthPredicate,
    mockCustomerDetailsService,
    app.injector.instanceOf[ServiceErrorHandler],
    mockAppConfig
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      lazy val result = TestClientVrnControllerSpec.show(fakeRequest)
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockCustomerDetailsSuccess(customerDetailsMax)
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the Client Vrn Page" in {
        document.select("h1").text shouldBe messages.heading
      }
    }

    "the user is authorised and an Error is returned" should {

      lazy val result = TestClientVrnControllerSpec.show(fakeRequest)

      "return 500" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    unauthenticatedCheck(TestClientVrnControllerSpec.show)
  }

  "the .submit method" when {

    "submitting with a valid VRN" should {

      lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("vrn", "123456789"))
      lazy val result = TestClientVrnControllerSpec.submit(request)

      "return 303" in {
        status(result) shouldBe Status.SEE_OTHER
      }
    }
  }

}
