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

package pages

import assets.PaymentsIntegrationTestConstants._
import helpers.BaseIntegrationSpec
import models.payments.PaymentRedirectModel
import play.api.http.Status.{INTERNAL_SERVER_ERROR, SEE_OTHER}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import stubs.PaymentStub

class PaymentsControllerISpec extends BaseIntegrationSpec {

  "Calling PaymentsController.sendToPayments" when {

    "A valid PaymentStartModel is posted" should {

      "return status 303 and redirect to the returned url " in {
        given.user.isAuthenticated
        PaymentStub.postPaymentSuccess(PaymentRedirectModel("change-business-details"))
        val res: WSResponse = get("/initialise-payment-journey")

        res should have(
          httpStatus(SEE_OTHER)
        )

        redirectLocation(res) shouldBe Some("change-business-details")
      }

    }

    "An invalid model is posted" should {

      "return an ISE (500)" in {
        given.user.isAuthenticated
        PaymentStub.postPaymentError()
        val res = get("/initialise-payment-journey")

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }

    }

  }

}