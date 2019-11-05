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
import audit.models.ContactPreferenceAuditModel
import controllers.ControllerBaseSpec
import mocks.services.MockContactPreferenceService
import models.contactPreferences.ContactPreference
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import views.html.returnFrequency.ChangeReturnFrequencyConfirmationView

import scala.concurrent.ExecutionContext

class ChangeReturnFrequencyConfirmationSpec extends ControllerBaseSpec with MockContactPreferenceService {

  object TestChangeReturnFrequencyConfirmation extends ChangeReturnFrequencyConfirmation(
    mockAuthPredicate,
    mockCustomerDetailsService,
    mockContactPreferenceService,
    serviceErrorHandler,
    mockAuditingService,
    inject[ChangeReturnFrequencyConfirmationView],
    mcc,
    mockConfig,
    ec
  )

  "Calling the .show action" when {

    "the user is authorised" when {

      "the user is an agent" when {

        "the call to the customer details service is successful" should {

          lazy val result = {
            mockContactPreferenceSuccess(ContactPreference("DIGITAL"))
            mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
            await(TestChangeReturnFrequencyConfirmation.show("agent")(agentUser))
          }

          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Business Address confirmation view" in {
            messages(Jsoup.parse(bodyOf(result)).select("h1").text) shouldBe Messages.ReceivedPage.heading
          }
        }

        "the call to the customer details service is unsuccessful" should {

          lazy val result = {
            mockCustomerDetailsError()
            mockContactPreferenceSuccess(ContactPreference("DIGITAL"))
            await(TestChangeReturnFrequencyConfirmation.show("agent")(agentUser))
          }

          "return 200" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Business Address confirmation view" in {
            messages(Jsoup.parse(bodyOf(result)).select("h1").text) shouldBe Messages.ReceivedPage.heading
          }
        }
      }

      "the user is not an agent" when {

        "display the correct content for a user that has a digital contact preference" should {
          lazy val result = {
            mockContactPreferenceSuccess(ContactPreference("DIGITAL"))
            await(TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request))
          }
          lazy val document = Jsoup.parse(bodyOf(result))

          "return 200" in {
            status(result) shouldBe Status.OK

            verify(mockAuditingService)
              .extendedAudit(
                ArgumentMatchers.any[ContactPreferenceAuditModel],
                ArgumentMatchers.any[Option[String]]

              )(
                ArgumentMatchers.any[HeaderCarrier],
                ArgumentMatchers.any[ExecutionContext]
              )
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Change Return Frequency Confirmation Page" in {
            messages(document.select("h1").text) shouldBe Messages.ReceivedPage.heading
            messages(document.select("#content article p:nth-of-type(1)").text()) shouldBe Messages.ReceivedPage.digitalPref
          }
        }

        "display the correct content for a user that has a paper contact preference" should {

          lazy val result = {
            mockContactPreferenceSuccess(ContactPreference("PAPER"))
            await(TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request))
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
            messages(document.select("h1").text) shouldBe Messages.ReceivedPage.heading
            messages(document.select("#content article p:nth-of-type(1)").text()) shouldBe Messages.ReceivedPage.paperPref
          }
        }

        "display the correct content when an error is returned from contactPreferences" should {

          lazy val result = {
            mockContactPreferenceError()
            await(TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix)(request))
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
            messages(document.select("h1").text) shouldBe Messages.ReceivedPage.heading
            messages(document.select("#content article p:nth-of-type(1)").text()) shouldBe Messages.ReceivedPage.contactPrefError
          }
        }

      }
    }

    unauthenticatedCheck(TestChangeReturnFrequencyConfirmation.show(user.redirectSuffix))
  }
}
