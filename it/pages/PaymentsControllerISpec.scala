/*
 * Copyright 2021 HM Revenue & Customs
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

import helpers.BaseIntegrationSpec
import models.payments.PaymentRedirectModel
import play.api.http.Status.{INTERNAL_SERVER_ERROR, SEE_OTHER}
import play.api.libs.ws.WSResponse
import stubs.{PaymentStub, VatSubscriptionStub}
import helpers.IntegrationTestConstants.{VRN, customerCircumstancesDetailsMin, individual}

class PaymentsControllerISpec extends BaseIntegrationSpec {

  "Calling PaymentsController.sendToPayments" when {

    "A valid PaymentStartModel is posted" should {

      "return status 303 and redirect to the returned url " in {

        given.user.isAuthenticated

        And("I stub a successful response from the Subscription service")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(individual))

        And("I stub a successful response from the Payments service")
        PaymentStub.postPaymentSuccess(PaymentRedirectModel("change-business-details"))

        When("I initiate a payment journey")
        val res: WSResponse = get("/initialise-payment-journey")

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI("change-business-details")
        )
      }
    }

    "An invalid model is posted" should {

      "Render the Internal Server Error page" in {

        given.user.isAuthenticated

        And("I stub a successful response from the Subscription service")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMin(individual))

        And("I stub an error response from the Payments service")
        PaymentStub.postPaymentError()

        When("I initiate a payment journey")
        val res = get("/initialise-payment-journey")

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR),
          pageTitle(titleThereIsAProblem + titleSuffixUser)
        )
      }
    }
  }
}
