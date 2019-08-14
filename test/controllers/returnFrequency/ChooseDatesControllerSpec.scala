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
import assets.ReturnPeriodTestConstants._
import common.SessionKeys
import controllers.ControllerBaseSpec
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentType, _}
import assets.BaseTestConstants._
import assets.messages.ReturnFrequencyMessages

class ChooseDatesControllerSpec extends ControllerBaseSpec{

  object TestChooseDatesController extends ChooseDatesController(
    messagesApi, mockAuthPredicate,mockInFlightReturnPeriodPredicate, mockCustomerDetailsService, serviceErrorHandler, mockConfig)

  "ChooseDatesController 'show' method" when {

    "the user is authorised" when {

      "user has an in-flight return frequency change" should {

        lazy val result = TestChooseDatesController.show(request)

        "return SEE_OTHER (303)" in {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisationPending)
          status(result) shouldBe Status.SEE_OTHER
        }

        s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.redirect().url}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
        }
      }

      "user does not have an in-flight change" when {

        "a value is not held in session for the current Return Frequency" should {

          lazy val result = TestChooseDatesController.show(request)

          "return SEE_OTHER (303)" in {
            mockCustomerDetailsSuccess(customerInformationModelDeregPending)
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
            redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
          }

          "add the current return frequency to the session" in {
            session(result).get(SessionKeys.CURRENT_RETURN_FREQUENCY) shouldBe Some(returnPeriodMar)
          }
        }

        "a value is already held in session for the current Return Frequency" when {

          "a value for new return frequency is not in session" should {

            lazy val result = TestChooseDatesController.show(request.withSession(
              SessionKeys.CURRENT_RETURN_FREQUENCY -> returnPeriodJan
            ))

            "return OK (200)" in {
              mockCustomerDetailsSuccess(customerInformationModelDeregPending)
              status(result) shouldBe Status.OK
            }

            "return HTML" in {
              contentType(result) shouldBe Some("text/html")
              charset(result) shouldBe Some("utf-8")
            }

            s"have the title 'Choose the new VAT Return dates'" in {
              Jsoup.parse(bodyOf(result)).select("title").text shouldBe ReturnFrequencyMessages.ChoosePage.title
            }
          }

          "a value for new return frequency is in session" should {

            lazy val result = TestChooseDatesController.show(request.withSession(
              SessionKeys.CURRENT_RETURN_FREQUENCY -> returnPeriodJan,
              SessionKeys.NEW_RETURN_FREQUENCY -> returnPeriodMar)
            )

            "return OK (200)" in {
              mockCustomerDetailsSuccess(customerInformationModelDeregPending)
              status(result) shouldBe Status.OK
            }

            "return HTML" in {
              contentType(result) shouldBe Some("text/html")
              charset(result) shouldBe Some("utf-8")
            }

            "have the January radio option selected" in {
              Jsoup.parse(bodyOf(result)).select("#period-option-march").attr("checked") shouldBe "checked"
            }

            s"have the title 'Choose the new VAT Return dates'" in {
              Jsoup.parse(bodyOf(result)).select("title").text shouldBe ReturnFrequencyMessages.ChoosePage.title
            }
          }
        }

        "a return frequency is NOT returned from the call to get circumstance info" should {

          lazy val result = TestChooseDatesController.show(request)

          "return 303" in {
            mockCustomerDetailsSuccess(customerInformationModelMin)
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.redirect().url}" in {
            redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
          }
        }

        "an error is returned from Customer Details" should {

          lazy val result = TestChooseDatesController.show(request)

          "return ISE (500)" in {
            mockCustomerDetailsSuccess(customerInformationModelDeregPending)
            mockCustomerDetailsError()
            status(result) shouldBe Status.INTERNAL_SERVER_ERROR
            messages(Jsoup.parse(bodyOf(result)).title) shouldBe internalServerErrorTitle
          }
        }
      }
    }
  }

  "ChooseDatesController 'submit' method" when {

    "user is authorised" when {

      "user has an in-flight return frequency change" should {

        lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", "January"))
        lazy val result = TestChooseDatesController.submit(request)

        "return SEE_OTHER (303)" in {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisationPending)
          status(result) shouldBe Status.SEE_OTHER
        }

        s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.redirect().url}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
        }
      }

      "user does not have an in-flight change" when {

        "a value is not held in session for the current Return Frequency" should {

          lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", "January"))
          lazy val result = TestChooseDatesController.submit(request)

          "return SEE_OTHER (303)" in {
            mockCustomerDetailsSuccess(customerInformationModelDeregPending)
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
            redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
          }

          "add the current return frequency to the session" in {
            session(result).get(SessionKeys.CURRENT_RETURN_FREQUENCY) shouldBe Some(returnPeriodMar)
          }
        }

        "submitting with an option selected" should {

          lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", "January"))
          lazy val result = TestChooseDatesController.submit(request.withSession(SessionKeys.CURRENT_RETURN_FREQUENCY -> returnPeriodJan))

          "return 303" in {
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.returnFrequency.routes.ConfirmVatDatesController.show().url}" in {
            redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ConfirmVatDatesController.show().url)
          }

          "add the new return frequency to the session" in {
            session(result).get(SessionKeys.NEW_RETURN_FREQUENCY) shouldBe Some(returnPeriodJan)
          }
        }

        "submitting with no option selected" should {

          "current return period in session is not valid" should {

            lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", ""))
            lazy val result = TestChooseDatesController.submit(request.withSession(SessionKeys.CURRENT_RETURN_FREQUENCY -> "invalid"))

            "return Internal Server Error (500)" in {
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
              messages(Jsoup.parse(bodyOf(result)).title) shouldBe internalServerErrorTitle
            }
          }

          "current return period in session is valid" should {

            lazy val request = FakeRequest("POST", "/").withFormUrlEncodedBody(("period-option", ""))
            lazy val result = TestChooseDatesController.submit(request.withSession(SessionKeys.CURRENT_RETURN_FREQUENCY -> returnPeriodJan))

            "return Bad Request (400)" in {
              status(result) shouldBe Status.BAD_REQUEST
            }

            s"have the title 'Choose the new VAT Return dates'" in {
              Jsoup.parse(bodyOf(result)).select("title").text shouldBe ReturnFrequencyMessages.ChoosePage.title
            }
          }
        }
      }
    }
  }
}
