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

package controllers.returnFrequency

import assets.CircumstanceDetailsTestConstants._
import assets.messages.{ReturnFrequencyMessages => messages}
import common.SessionKeys
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.services.{MockCustomerDetailsService, MockReturnFrequencyService}
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._

class ConfirmVatDatesControllerSpec extends ControllerBaseSpec
  with MockCustomerDetailsService
  with MockReturnFrequencyService {

  object TestConfirmVatDatesController extends ConfirmVatDatesController(
    messagesApi,
    mockAuthPredicate,
    app.injector.instanceOf[ServiceErrorHandler],
    mockCustomerDetailsService,
    mockReturnFrequencyService,
    mockAppConfig
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      val session = SessionKeys.RETURN_FREQUENCY -> "January"
      lazy val result = TestConfirmVatDatesController.show(fakeRequest.withSession(session))
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the CustomerDetails Page" in {
        document.title shouldBe messages.title
      }
    }

    "the user is authorised and an Error is returned" should {

      val session = SessionKeys.RETURN_FREQUENCY -> "unknown"
      lazy val result = TestConfirmVatDatesController.show(fakeRequest.withSession(session))

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

      val session = SessionKeys.RETURN_FREQUENCY -> "Monthly"
      lazy val result = TestConfirmVatDatesController.submit(fakeRequest.withSession(session))

      "return 303" in {
        setupMockReturnFrequencyServiceWithSuccess()
        status(result) shouldBe Status.SEE_OTHER
      }

      "return a location to the received dates view" in {
        setupMockReturnFrequencyServiceWithSuccess()
        redirectLocation(result) shouldBe Some("/vat-through-software/account/confirmation-vat-return-dates")
      }
    }

    "the user is authorised but submitting the changes to the backend fails" should {

      val session = SessionKeys.RETURN_FREQUENCY -> "Monthly"
      lazy val result = TestConfirmVatDatesController.submit(fakeRequest.withSession(session))

      "return 500" in {
        setupMockReturnFrequencyServiceWithFailure()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user is authorised and a the session contains invalid data" should {

      val session = SessionKeys.RETURN_FREQUENCY -> "unknown"
      lazy val result = TestConfirmVatDatesController.show(fakeRequest.withSession(session))

      "return 500" in {
        setupMockReturnFrequencyServiceWithSuccess()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
