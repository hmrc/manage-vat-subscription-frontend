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

package controllers.returnFrequency

import assets.CircumstanceDetailsTestConstants._
import assets.BaseTestConstants._
import assets.messages.{ReturnFrequencyMessages => Messages}
import audit.mocks.MockAuditingService
import audit.models.UpdateReturnFrequencyAuditModel
import common.SessionKeys
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.services.{MockCustomerCircumstanceDetailsService, MockReturnFrequencyService}
import models.returnFrequency.{Jan, Monthly}
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class ConfirmVatDatesControllerSpec extends ControllerBaseSpec
  with MockCustomerCircumstanceDetailsService
  with MockReturnFrequencyService
  with MockAuditingService {

  object TestConfirmVatDatesController extends ConfirmVatDatesController(
    mockAuthPredicate,
    app.injector.instanceOf[ServiceErrorHandler],
    mockReturnFrequencyService,
    mockCustomerDetailsService,
    mockAuditingService,
    mockConfig,
    messagesApi
  )

  "Calling the .show action" when {

    "the user is authorised and a Return Frequency is in session" should {

      val session = SessionKeys.NEW_RETURN_FREQUENCY -> "January"
      lazy val result = TestConfirmVatDatesController.show(request.withSession(session))
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the Confirm Dates Page" in {
        document.title shouldBe Messages.ConfirmPage.heading
      }
    }

    "the user is authorised and an Error is returned" should {

      val session = SessionKeys.NEW_RETURN_FREQUENCY -> "unknown"
      lazy val result = TestConfirmVatDatesController.show(request.withSession(session))

      "return 500" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    unauthenticatedCheck(TestConfirmVatDatesController.show)
  }

  "Calling the .submit action" when {

    "the user is authorised and a the session contains valid data" should {

      lazy val result = TestConfirmVatDatesController.submit(request.withSession(
        SessionKeys.NEW_RETURN_FREQUENCY -> "January",
        SessionKeys.CURRENT_RETURN_FREQUENCY -> "Monthly"
      ))

      "return 303" in {
        setupMockReturnFrequencyServiceWithSuccess()
        setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxOrganisation))
        status(result) shouldBe Status.SEE_OTHER

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(UpdateReturnFrequencyAuditModel(user, Monthly, Jan, Some(partyType))),
            ArgumentMatchers.eq[Option[String]](Some(controllers.returnFrequency.routes.ConfirmVatDatesController.submit().url))
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
      }

      "return a location to the received dates view" in {
        val test = s"/vat-through-software/account/confirmation-vat-return-dates/non-agent"
        redirectLocation(result) shouldBe Some(test)
      }
    }

    "the user is an Agent and authorised and a the session contains valid data" should {

      lazy val result = TestConfirmVatDatesController.submit(fakeRequestWithClientsVRN.withSession(
        SessionKeys.NEW_RETURN_FREQUENCY -> "January",
        SessionKeys.CURRENT_RETURN_FREQUENCY -> "Monthly"
      ))

      "return 303" in {
        mockAgentAuthorised()
        setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxOrganisation))
        setupMockReturnFrequencyServiceWithSuccess()
        status(result) shouldBe Status.SEE_OTHER

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(UpdateReturnFrequencyAuditModel(agentUser, Monthly, Jan, Some(partyType))),
            ArgumentMatchers.eq[Option[String]](Some(controllers.returnFrequency.routes.ConfirmVatDatesController.submit().url))
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
      }

      "return a location to the received dates view" in {
        val test = s"/vat-through-software/account/confirmation-vat-return-dates/agent"
        redirectLocation(result) shouldBe Some(test)
      }
    }

    "the user is authorised but submitting the changes to the backend fails" should {

      lazy val result = TestConfirmVatDatesController.submit(request.withSession(
        SessionKeys.NEW_RETURN_FREQUENCY -> "Monthly",
        SessionKeys.CURRENT_RETURN_FREQUENCY -> "January"
      ))

      "return 500" in {
        setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxOrganisation))
        setupMockReturnFrequencyServiceWithFailure()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user is authorised and a the session contains invalid data" should {

      lazy val result = TestConfirmVatDatesController.submit(request.withSession(
        SessionKeys.NEW_RETURN_FREQUENCY -> "unknown",
        SessionKeys.CURRENT_RETURN_FREQUENCY -> "January"
      ))

      "return 500" in {
        setupMockReturnFrequencyServiceWithSuccess()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
