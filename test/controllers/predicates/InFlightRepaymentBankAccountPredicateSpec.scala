/*
 * Copyright 2024 HM Revenue & Customs
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

import assets.CircumstanceDetailsTestConstants._
import mocks.MockAuth
import org.jsoup.Jsoup
import play.api.test.Helpers.{LOCATION, await, contentAsString, defaultAwaitTimeout}
import assets.BaseTestConstants._
import play.api.http.Status
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.mvc.Results.BadRequest

import scala.concurrent.Future

class InFlightRepaymentBankAccountPredicateSpec extends MockAuth {

  "The InFlightRepaymentBankAccountPredicate" when {

    "getCustomerCircumstanceDetails call is successful" when {

      "bank account change indicator is true" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
          await(mockInFlightRepaymentBankAccountPredicate.refine(user)).swap.getOrElse(BadRequest)
        }

        "return 303" in {
          result.header.status shouldBe Status.SEE_OTHER
        }

        s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.show.url}" in {
          result.header.headers.get(LOCATION) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.show.url)
        }
      }

      "bank account change indicator is false" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationModelDeregPending)
          await(mockInFlightRepaymentBankAccountPredicate.refine(user))
        }

        "allow the request through" in {
          result shouldBe Right(user)
        }
      }

      "changeIndicators is not returned" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
          await(mockInFlightRepaymentBankAccountPredicate.refine(user))
        }

        "allow the request through" in {
          result shouldBe Right(user)
        }
      }
    }

    "getCustomerCircumstanceDetails call is unsuccessful" should {

      lazy val result = {
        mockCustomerDetailsError()
        await(mockInFlightRepaymentBankAccountPredicate.refine(user)).swap.getOrElse(BadRequest)
      }

      "return 500" in {
        result.header.status shouldBe INTERNAL_SERVER_ERROR
        Jsoup.parse(contentAsString(Future.successful(result))).title shouldBe internalServerErrorTitleUser
      }
    }

    "the user is an agent" should {

      lazy val result = await(mockInFlightRepaymentBankAccountPredicate.refine(agentUser)).swap.getOrElse(BadRequest)

      "return 303" in {
        result.header.status shouldBe Status.SEE_OTHER
      }

      "redirect to the CustomerCircumstanceDetailsController show action" in {
        result.header.headers.get(LOCATION) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.show.url)
      }
    }
  }
}
