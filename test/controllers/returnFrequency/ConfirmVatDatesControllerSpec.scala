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

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants._
import assets.messages.{ReturnFrequencyMessages => Messages}
import audit.models.UpdateReturnFrequencyAuditModel
import common.SessionKeys
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.services.MockReturnFrequencyService
import models.returnFrequency.{Jan, Monthly}
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class ConfirmVatDatesControllerSpec extends ControllerBaseSpec with MockReturnFrequencyService {

  object TestConfirmVatDatesController extends ConfirmVatDatesController(
    mockAuthPredicate,
    app.injector.instanceOf[ServiceErrorHandler],
    mockReturnFrequencyService,
    mockCustomerDetailsService,
    mockAuditingService,
    mockInFlightReturnPeriodPredicate,
    mockConfig,
    messagesApi
  )

  "Calling the .show action" when {

    "user is authorised" when {

      "current return frequency is in session" when {

        "new return frequency is in session" should {

          lazy val result = TestConfirmVatDatesController.show(request.withSession(
            SessionKeys.CURRENT_RETURN_FREQUENCY -> "March",
            SessionKeys.NEW_RETURN_FREQUENCY -> "January")
          )
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
            document.title shouldBe Messages.ConfirmPage.title
          }
        }

        "new return frequency is not in session" should {

          lazy val result = TestConfirmVatDatesController.show(request.withSession(
            SessionKeys.CURRENT_RETURN_FREQUENCY -> "March"
          ))

          "return 303" in {
            mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
            redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
          }
        }
      }

      "current return frequency is not in session" should {

        lazy val result = TestConfirmVatDatesController.show(request)

        "return 303" in {
          mockCustomerDetailsSuccess(customerInformationModelDeregPending)
          status(result) shouldBe Status.SEE_OTHER
        }

        s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
          redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
        }
      }
    }

    unauthenticatedCheck(TestConfirmVatDatesController.show)
  }

  "Calling the .submit action" when {

    "user is authorised" when {

      "current return frequency is in session" when {

        "new return frequency is in session" when {

          "updateReturnFrequency returns an error" should {

            lazy val result = TestConfirmVatDatesController.submit(request.withSession(
              SessionKeys.NEW_RETURN_FREQUENCY -> "Monthly",
              SessionKeys.CURRENT_RETURN_FREQUENCY -> "January"
            ))

            "return 500" in {
              setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxOrganisation))
              setupMockReturnFrequencyServiceWithFailure()
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
              messages(Jsoup.parse(bodyOf(result)).title) shouldBe internalServerErrorTitle
            }
          }

          "updateReturnFrequency returns success" should {

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

            s"redirect to ${controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show("non-agent").url}" in {
              redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show("non-agent").url)
            }
          }
        }

        "new return frequency is not in session" should {

          lazy val result = TestConfirmVatDatesController.submit(request.withSession(
            SessionKeys.CURRENT_RETURN_FREQUENCY -> "January"
          ))

          "return 303" in {
            setupMockReturnFrequencyServiceWithSuccess()
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
            redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
          }
        }
      }

      "current return frequency is not in session" should {

        lazy val result = TestConfirmVatDatesController.submit(request.withSession(
          SessionKeys.CURRENT_RETURN_FREQUENCY -> "January"
        ))

        "return 303" in {
          setupMockReturnFrequencyServiceWithSuccess()
          status(result) shouldBe Status.SEE_OTHER
        }

        s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
          redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
        }
      }
    }
  }
}
