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

package controllers

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants.customerInformationWithPartyType
import assets.PaymentsTestConstants._
import audit.mocks.MockAuditingService
import audit.models.BankAccountHandOffAuditModel
import mocks.services.{MockCustomerCircumstanceDetailsService, MockPaymentsService}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers.{redirectLocation, _}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext


class PaymentsControllerSpec extends ControllerBaseSpec with MockPaymentsService with MockAuditingService with MockCustomerCircumstanceDetailsService {

  object TestPaymentController extends PaymentsController(
    messagesApi,
    mockAuthPredicate,
    serviceErrorHandler,
    mockPaymentsService,
    mockAuditingService,
    mockInflightReturnPeriodPredicate,
    mockCustomerDetailsService,
    mockConfig
  )

  "Calling the sendToPayments method for an individual" when {

    def setup(customerDetailsResponse: CircumstanceDetailsResponse = Right(customerInformationWithPartyType(None)),
              paymentsResponse: PaymentsResponse): PaymentsController = {

      setupMockPaymentsService(paymentsResponse)
      setupMockCustomerDetails(vrn)(customerDetailsResponse)
      mockIndividualAuthorised()

      TestPaymentController
    }

    "the PaymentsService returns a Right(PaymentRedirectModel)" should {

      lazy val controller = setup(paymentsResponse = Right(successPaymentsResponseModel))
      lazy val result = controller.sendToPayments(request)

      "return 303 (Redirect)" in {
        status(result) shouldBe Status.SEE_OTHER

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(BankAccountHandOffAuditModel(user, successPaymentsResponse)),
            ArgumentMatchers.eq[Option[String]](Some(controllers.routes.PaymentsController.sendToPayments().url))
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
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

    "the CustomerCircumstanceDetailsService returns an error" should {

      lazy val controller = setup(customerDetailsResponse = Left(errorModel), paymentsResponse = Left(errorModel))
      lazy val result = controller.sendToPayments(request)

      "return 500 (ISE)" in {
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }
    }
  }
}
