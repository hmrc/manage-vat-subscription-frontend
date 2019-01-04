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
import assets.messages.ReturnFrequencyMessages
import assets.ReturnPeriodTestConstants.returnPeriodJan
import common.SessionKeys
import controllers.ControllerBaseSpec
import mocks.services.MockCustomerCircumstanceDetailsService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentType, _}

class ChooseDatesControllerSpec extends ControllerBaseSpec with MockCustomerCircumstanceDetailsService {

  object TestChooseDatesController extends ChooseDatesController(
    messagesApi, mockAuthPredicate, mockCustomerDetailsService, serviceErrorHandler, mockConfig)

  "ChooseDatesController 'show' method" when {

    "the user is authorised" when {

      "a return frequency is returned from the call to get circumstance info" when {

        "a value is not already held in session for the Return Frequency" should {

          lazy val result = TestChooseDatesController.show(request)

          "return OK (200)" in {
            mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          s"have the heading '${ReturnFrequencyMessages.ChoosePage.heading}'" in {
            Jsoup.parse(bodyOf(result)).title shouldBe ReturnFrequencyMessages.ChoosePage.heading
          }
        }

        "a value is already held in session for the Return Frequency = January" should {

          lazy val result = TestChooseDatesController.show(request.withSession(SessionKeys.NEW_RETURN_FREQUENCY -> returnPeriodJan))

          "return OK (200)" in {
            mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "have the January radio option selected" in {
            Jsoup.parse(bodyOf(result)).select("#period-option-january").attr("checked") shouldBe "checked"
          }
        }
      }

      "a return frequency is NOT returned from the call to get circumstance info" should {

        lazy val result = TestChooseDatesController.show(request)

        "return ISE (500)" in {
          mockCustomerDetailsSuccess(customerInformationModelMin)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }

    }

    "the user is authorised and an Error is returned from Customer Details" should {

      lazy val result = TestChooseDatesController.show(request)

      "return ISE (500)" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

  }

  "ChooseDatesController 'submit' method" when {

    "submitting with an option selected" should {

      lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", "January"))
      lazy val result = TestChooseDatesController.submit(request)

      "return 303" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.SEE_OTHER
      }
    }

    "submitting with no option selected" should {

      "when a return period is returned from get customer details" should {

        lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", ""))
        lazy val result = TestChooseDatesController.submit(request)

        "return Bad Request (400)" in {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
          status(result) shouldBe Status.BAD_REQUEST
        }
      }

      "when a return period is NOT returned from get customer details" should {

        lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", ""))
        lazy val result = TestChooseDatesController.submit(request)

        "return ISE (500)" in {
          mockCustomerDetailsSuccess(customerInformationModelMin)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }
  }
}
