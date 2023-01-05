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

package controllers

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants.{customerInformationModelMaxOrganisationPending, customerInformationNoPendingIndividual}
import assets.PaymentsTestConstants._
import audit.models.BankAccountHandOffAuditModel
import mocks.services.MockPaymentsService
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers.{redirectLocation, _}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class PaymentsControllerSpec extends ControllerBaseSpec with MockPaymentsService {

  object TestPaymentController extends PaymentsController(
    mockAuthPredicate,
    serviceErrorHandler,
    mockPaymentsService,
    mockAuditingService,
    mockCustomerDetailsService,
    mockInFlightRepaymentBankAccountPredicate,
    mcc,
    mockConfig,
    ec
  )

  "Calling the sendToPayments method for an individual" when {

    "user has no in-flight repayment bank account change" when {

      "the PaymentsService returns a Right(PaymentRedirectModel)" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
          setupMockPaymentsService(Right(successPaymentsResponseModel))
          TestPaymentController.sendToPayments(request)
        }

        "return 303 (Redirect)" in {
          status(result) shouldBe Status.SEE_OTHER

          verify(mockAuditingService)
            .extendedAudit(
              ArgumentMatchers.eq(BankAccountHandOffAuditModel(user, successPaymentsResponse)),
              ArgumentMatchers.eq[Option[String]](Some(controllers.routes.PaymentsController.sendToPayments.url))
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

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
          setupMockPaymentsService(Left(errorModel))
          TestPaymentController.sendToPayments(request)
        }

        "return 500" in {
          status(result) shouldBe INTERNAL_SERVER_ERROR
        }

        "render the internal server error page" in {
          Jsoup.parse(contentAsString(result)).title shouldBe internalServerErrorTitleUser
        }
      }
    }

    "user has an in-flight repayment bank account change" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisationPending)
        TestPaymentController.sendToPayments(request)
      }

      "return SEE_OTHER (303)" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.show.url}" in {
        redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.show.url)
      }
    }

    "there was an error retrieving customer information in the second call, after the inflight predicate" should {

      lazy val result = {
        mockDoubleCustomerDetails(Right(customerInformationNoPendingIndividual), Left(errorModel))
        TestPaymentController.sendToPayments(request)
      }

      "return 500" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "render the internal server error page" in {
        Jsoup.parse(contentAsString(result)).title shouldBe internalServerErrorTitleUser
      }
    }

    insolvencyCheck(TestPaymentController.sendToPayments)
  }
}
