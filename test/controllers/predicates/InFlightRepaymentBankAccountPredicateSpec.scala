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

import assets.CircumstanceDetailsTestConstants._
import mocks.MockAuth
import play.api.test.Helpers._

class InFlightRepaymentBankAccountPredicateSpec extends MockAuth {

  "The InFlightRepaymentBankAccountPredicate" when {

    "getCustomerCircumstanceDetails call is successful" when {

      "bank account change indicator is true" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
          await(mockInFlightRepaymentBankAccountPredicate.refine(user).left.get)
        }

        "return 303" in {
          status(result) shouldBe SEE_OTHER
        }

        s"redirect to ${controllers.routes.CustomerCircumstanceDetailsController.redirect().url}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
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


      "bank account change indicator is false user is agent" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationModelDeregPending)
          await(mockInFlightRepaymentBankAccountPredicate.refine(agentUser))
        }

        "not allowed request through" in {
          result shouldBe Right(agentUser)
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
        await(mockInFlightRepaymentBankAccountPredicate.refine(user).left.get)
      }

      "return 500" in {
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }
    }
  }
}
