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

package controllers.predicates

import assets.BaseTestConstants.vrn
import assets.CircumstanceDetailsTestConstants._
import assets.ReturnPeriodTestConstants._
import common.SessionKeys
import mocks.MockAuth
import models.User
import org.jsoup.Jsoup
import play.api.test.FakeRequest
import play.api.test.Helpers._
import assets.BaseTestConstants._

class InFlightReturnFrequencyPredicateSpec extends MockAuth {

  "The InFlightReturnFrequencyPredicate" when {

    "user has CURRENT_RETURN_FREQUENCY in session" should {

      lazy val fakeRequest = FakeRequest().withSession(
        SessionKeys.CURRENT_RETURN_FREQUENCY -> "Monthly"
      )

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
        await(mockInFlightReturnPeriodPredicate.refine(User(vrn)(fakeRequest)))
      }

      "allow the request through" in {
        result shouldBe Right(user)
      }
    }

    "user has no CURRENT_RETURN_FREQUENCY in session" when {

      "getCustomerCircumstanceDetails call fails" should {

        lazy val result = {
          mockCustomerDetailsError()
          await(mockInFlightReturnPeriodPredicate.refine(user)).left.get
        }

        "return 500" in {
          status(result) shouldBe INTERNAL_SERVER_ERROR
          messages(Jsoup.parse(bodyOf(result)).title) shouldBe internalServerErrorTitle
        }
      }

      "getCustomerCircumstanceDetails call is successful" when {

        "return period change indicator is true" should {

          lazy val result = {
            mockCustomerDetailsSuccess(customerInformationModelMaxIndividual)
            await(mockInFlightReturnPeriodPredicate.refine(user).left.get)
          }

          "return 303" in {
            status(result) shouldBe SEE_OTHER
          }

          s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.redirect().url}" in {
            redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
          }
        }

        "return period change indicator is false" when {

          "no return period is returned" should {

            lazy val result = {
              mockCustomerDetailsSuccess(customerInformationPendingEmailModel)
              await(mockInFlightReturnPeriodPredicate.refine(user).left.get)
            }

            "return 303" in {
              status(result) shouldBe SEE_OTHER
            }

            s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.redirect().url}" in {
              redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
            }
          }

          "return period is returned" should {

            lazy val result = {
              mockCustomerDetailsSuccess(customerInformationModelDeregPending)
              await(mockInFlightReturnPeriodPredicate.refine(user).left.get)
            }

            "return 303" in {
              status(result) shouldBe SEE_OTHER
            }

            s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
              redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
            }

            "add the current return frequency to the session" in {
              session(result).get(SessionKeys.CURRENT_RETURN_FREQUENCY) shouldBe Some(returnPeriodMar)
            }
          }
        }

        "changeIndicators is not returned" when {

          "return period is returned" should {

            lazy val result = {
              mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
              await(mockInFlightReturnPeriodPredicate.refine(user).left.get)
            }

            "return 303" in {
              status(result) shouldBe SEE_OTHER
            }

            s"redirect to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
              redirectLocation(result) shouldBe Some(controllers.returnFrequency.routes.ChooseDatesController.show().url)
            }

            "add the current return frequency to the session" in {
              session(result).get(SessionKeys.CURRENT_RETURN_FREQUENCY) shouldBe Some(returnPeriodMar)
            }
          }
        }
      }
    }
  }
}
