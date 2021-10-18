/*
 * Copyright 2021 HM Revenue & Customs
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

import assets.BaseTestConstants.internalServerErrorTitleUser
import assets.CircumstanceDetailsTestConstants._
import assets.messages.ChangePendingMessages
import mocks.MockAuth
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._


class InFlightPPOBPredicateSpec extends MockAuth  {

  "The InFlightPPOBPredicate" when {

    "the user has nothing inflight" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
        await(mockInFlightPPOBPredicate.refine(user))
      }

      "allow the request to pass through the predicate" in {
        result shouldBe Right(user)
      }
    }

    "the user has an inflight email address which is different to the current email address" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationPendingEmailModel)
        await(mockInFlightPPOBPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(contentAsString(result))

      "return 409 Conflict" in {
        status(result) shouldBe Status.CONFLICT
      }

      "show the 'change pending' error page" in {
        messages(document.select("h1").text) shouldBe ChangePendingMessages.heading
      }
    }

    "the user has an inflight ppob address which is different to the current ppob address" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationPendingPPOBModel)
        await(mockInFlightPPOBPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 409 Conflict" in {
        status(result) shouldBe Status.CONFLICT
      }

      "show the 'change pending' error page" in {
        messages(document.select("h1").text) shouldBe ChangePendingMessages.heading
      }
    }

    "the user has an inflight phone number which is different to the current phone number" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationPendingPhoneModel)
        await(mockInFlightPPOBPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(contentAsString(result))

      "return 409 Conflict" in {
        status(result) shouldBe Status.CONFLICT
      }

      "show the 'change pending' error page" in {
        messages(document.select("h1").text) shouldBe ChangePendingMessages.heading
      }
    }

    "the user has an inflight mobile number which is different to the current mobile number" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationPendingMobileModel)
        await(mockInFlightPPOBPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 409 Conflict" in {
        status(result) shouldBe Status.CONFLICT
      }

      "show the 'change pending' error page" in {
        messages(document.select("h1").text) shouldBe ChangePendingMessages.heading
      }
    }

    "the user has an inflight website which is different to the current website" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationPendingWebsiteModel)
        await(mockInFlightPPOBPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 409 Conflict" in {
        status(result) shouldBe Status.CONFLICT
      }

      "show the 'change pending' error page" in {
        messages(document.select("h1").text) shouldBe ChangePendingMessages.heading
      }
    }

    "the Customer Details Service returns an error" should {

      lazy val result = {
        mockCustomerDetailsError()
        await(mockInFlightPPOBPredicate.refine(user)).left.get
      }
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 500" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "show the technical difficulties page" in {
        messages(document.title) shouldBe internalServerErrorTitleUser
      }
    }
  }
}
