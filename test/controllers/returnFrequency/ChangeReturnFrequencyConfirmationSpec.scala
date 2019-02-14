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
import assets.messages.{ReturnFrequencyMessages => Messages}
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.services.{MockContactPreferenceService, MockCustomerCircumstanceDetailsService}
import models.contactPreferences.ContactPreference
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._

class ChangeReturnFrequencyConfirmationSpec extends ControllerBaseSpec with MockCustomerCircumstanceDetailsService with MockContactPreferenceService {

  object TestChangeReturnFrequencyConfirmation extends ChangeReturnFrequencyConfirmation(
    messagesApi,
    mockAuthPredicate,
    mockCustomerDetailsService,
    mockContactPreferenceService,
    app.injector.instanceOf[ServiceErrorHandler],
    mockConfig
  )

  "Calling the .show action" when {

    "the user is authorised" when {

      "the user is an agent" when {

        "the call to the customer details service is successful" should {

          lazy val result = {
            mockConfig.features.useContactPreferences(false)
            mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
            TestChangeReturnFrequencyConfirmation.show("agent")(agentUser)
          }

          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Business Address confirmation view" in {
            Jsoup.parse(bodyOf(result)).title shouldBe Messages.ReceivedPage.heading
          }
        }

        "the call to the customer details service is unsuccessful" should {

          lazy val result = {
            mockCustomerDetailsError()
            TestChangeReturnFrequencyConfirmation.show("agent")(agentUser)
          }

          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Business Address confirmation view" in {
            Jsoup.parse(bodyOf(result)).title shouldBe Messages.ReceivedPage.heading
          }
        }
      }

      "the user is not an agent and the 'useContactPreference' feature is disabled" should {

        lazy val result = {
          mockConfig.features.useContactPreferences(false)
          TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request)
        }
        lazy val document = Jsoup.parse(bodyOf(result))

        "return 200" in {
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }

        "render the Change Return Frequency Confirmation Page" in {
          document.title shouldBe Messages.ReceivedPage.heading
        }
      }

      "the user is not an agent and the 'useContactPreference' feature is enabled" when {

        "display the correct content for a user that has a digital contact preference" should {
          lazy val result = {
            mockConfig.features.useContactPreferences(true)
            mockContactPreferenceSuccess(ContactPreference("DIGITAL"))
            TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request)
          }
          lazy val document = Jsoup.parse(bodyOf(result))

          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Change Return Frequency Confirmation Page" in {
            document.title shouldBe Messages.ReceivedPage.heading
          }
        }

        "display the correct content for a user that has a paper contact preference" should {

          lazy val result = {
            mockConfig.features.useContactPreferences(true)
            mockContactPreferenceSuccess(ContactPreference("PAPER"))
            TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request)
          }
          lazy val document = Jsoup.parse(bodyOf(result))


          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Change Return Frequency Confirmation Page" in {
            document.title shouldBe Messages.ReceivedPage.heading
          }
        }

        "display the correct content when an error is returned from contactPreferences" should {

          lazy val result = {
            mockConfig.features.useContactPreferences(true)
            mockContactPreferenceError()
            TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request)
          }
          lazy val document = Jsoup.parse(bodyOf(result))

          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Change Return Frequency Confirmation Page" in {
            document.title shouldBe Messages.ReceivedPage.heading
          }
        }

      }
    }

    unauthenticatedCheck(TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix))
  }
}
