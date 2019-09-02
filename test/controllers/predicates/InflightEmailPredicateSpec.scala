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
import assets.messages.EmailChangePendingMessages
import mocks.MockAuth
import org.jsoup.Jsoup
import play.api.http.Status

class InflightEmailPredicateSpec extends MockAuth {

  "The InflightEmailPredicate" when {

    "the user has nothing inflight" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
        await(mockInflightEmailPredicate.refine(user))
      }

      "allow the request to pass through the predicate" in {
        result shouldBe Right(user)
      }
    }

    "the user has an inflight email address that is the same as the current email address" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationModelMaxIndividual)
        await(mockInflightEmailPredicate.refine(user))
      }

      "allow the request to pass through the predicate" in {
        result shouldBe Right(user)
      }
    }

    "the user has an inflight email address that is different to the current email address" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationPendingEmailModel)
        await(mockInflightEmailPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        status(result) shouldBe Status.OK
      }

      "show the 'Email change pending' error page" in {
        document.title shouldBe EmailChangePendingMessages.title
      }
    }

    "the Customer Details Service returns an error" should {

      lazy val result = {
        mockCustomerDetailsError()
        await(mockInflightEmailPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 500" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "show the technical difficulties page" in {
        document.title shouldBe "There is a problem with the service - VAT - GOV.UK"
      }
    }
  }
}
