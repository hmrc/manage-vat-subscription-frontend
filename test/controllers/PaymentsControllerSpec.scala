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

import mocks.services.MockPaymentsService
import assets.BaseTestConstants._
import assets.PaymentsTestConstants._
import models.payments.PaymentStartModel
import play.api.http.Status
import play.api.test.Helpers.redirectLocation
import play.api.test.Helpers._


class PaymentsControllerSpec extends ControllerBaseSpec with MockPaymentsService {

  object TestPaymentController extends PaymentsController(
    messagesApi,
    mockAuthPredicate,
    serviceErrorHandler,
    mockPaymentsService,
    frontendAppConfig
  )

  "The 'paymentDetails' method" when {

    "passed '(999999999, true)' should return a PaymentStartModel with defined 'convenienceUrl'" in {

      TestPaymentController.paymentDetails("someVrn", isAgent = true) shouldBe
        PaymentStartModel(
          "someVrn",
          isAgent = true,
          frontendAppConfig.signInContinueBaseUrl + "/vat-through-software/account/change-business-details",
          frontendAppConfig.signInContinueBaseUrl + "/vat-through-software/account/change-business-details",
          frontendAppConfig.signInContinueBaseUrl + "/vat-through-software/account/client-vat-number"
        )
    }

    "passed '(999999999, false)' should return a PaymentStartModel with undefined 'convenienceUrl'" in {

      TestPaymentController.paymentDetails("someVrn", isAgent = false) shouldBe
        PaymentStartModel(
          "someVrn",
          isAgent = false,
          frontendAppConfig.signInContinueBaseUrl + "/vat-through-software/account/change-business-details",
          frontendAppConfig.signInContinueBaseUrl + "/vat-through-software/account/change-business-details",
          frontendAppConfig.signInContinueBaseUrl + "/vat-through-software/account/change-business-details"
        )
    }

  }

  "Calling the sendToPayments method for an individual" when {

    def setup(paymentsResponse: PaymentsResponse): PaymentsController = {

      setupMockPaymentsService(paymentsResponse)
      mockIndividualAuthorised()

      TestPaymentController
    }

    "the PaymentsService returns a Right(PaymentRedirectModel)" should {

      lazy val controller = setup(paymentsResponse = Right(successPaymentsResponseModel))
      lazy val result = controller.sendToPayments(request)

      "return 303 (Redirect)" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the correct url" in {
        redirectLocation(result) shouldBe Some(successPaymentsResponse)
      }
    }

    "the PaymentsService returns an error" should {

      lazy val controller = setup(paymentsResponse = Left(errorModel))
      lazy val result = controller.sendToPayments(request)

      "return 500 (ISE)" in {
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }

    }

  }

}