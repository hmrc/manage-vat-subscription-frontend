/*
 * Copyright 2018 HM Revenue & Customs
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

package controllers

import assets.messages.ChangeAddressConfirmationPageMessages
import assets.CustomerAddressTestConstants._
import assets.PPOBAddressTestConstants._
import audit.mocks.MockAuditingService
import mocks.services.MockAddressLookupService
import mocks.services.MockBusinessAddressService
import org.jsoup.Jsoup
import play.api.http.Status
import assets.BaseTestConstants._
import models.core.SubscriptionUpdateResponseModel
import models.customerAddress.AddressLookupOnRampModel
import org.mockito.ArgumentMatchers
import play.api.test.Helpers.redirectLocation
import play.api.test.Helpers._
import org.mockito.Mockito.verify
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class BusinessAddressControllerSpec extends ControllerBaseSpec with MockAddressLookupService with MockBusinessAddressService with MockAuditingService {

  "Calling .callback" when {

    def setup(addressLookupResponse: RetrieveAddressResponse,
              businessAddressResponse: BusinessAddressResponse): BusinessAddressController = {

      setupMockRetrieveAddress(addressLookupResponse)
      setupMockBusinessAddress(businessAddressResponse)

      new BusinessAddressController(
        messagesApi,
        mockAuthPredicate,
        mockAddressLookupService,
        mockBusinessAddressService,
        serviceErrorHandler,
        mockAuditingService,
        mockConfig)
    }

    "address lookup service returns success" when {

      "and business address service returns success" should {

        lazy val controller = setup(
          addressLookupResponse = Right(customerAddressMax),
          businessAddressResponse = Right(SubscriptionUpdateResponseModel(""), ppobAddressModelMax)
        )
        lazy val result = controller.callback("12345")(request)

        "return ok" in {
          status(result) shouldBe Status.OK
        }

        "return the confirmation page" in {
          Jsoup.parse(bodyOf(result)).title shouldBe ChangeAddressConfirmationPageMessages.title
        }
      }
    }

    "address lookup service returns success" when {

      "and business address service returns an error" should {

        lazy val controller = setup(
          addressLookupResponse = Right(customerAddressMax),
          businessAddressResponse = Left(errorModel))
        lazy val result = controller.callback("12345")(request)

        "return InternalServerError" in {
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }

    "address lookup service returns an error" should {

      lazy val controller = setup(
        addressLookupResponse = Left(errorModel),
        businessAddressResponse = Left(errorModel))
      lazy val result = controller.callback("12345")(request)

      "return InternalServerError" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user is not authenticated" should {

      lazy val controller = setup(
        addressLookupResponse = Left(errorModel),
        businessAddressResponse = Left(errorModel))

      unauthenticatedCheck(controller.callback("12345"))
    }
  }

  "Calling .initialiseJourney" when {

    def setup(addressLookupResponse: InitialiseJourneyResponse): BusinessAddressController = {

      setupMockInitialiseJourney(addressLookupResponse)

      new BusinessAddressController(
        messagesApi,
        mockAuthPredicate,
        mockAddressLookupService,
        mockBusinessAddressService,
        serviceErrorHandler,
        mockAuditingService,
        mockConfig)
    }

    "address lookup service returns success" should {

      lazy val controller = setup(addressLookupResponse = Right(AddressLookupOnRampModel("redirect-url")))
      lazy val result = controller.initialiseJourney(false)(request)

      "return redirect to the url returned" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to url returned" in {
        redirectLocation(result) shouldBe Some("redirect-url")

      }
    }

    "address lookup service returns an error" should {

      lazy val controller = setup(addressLookupResponse = Left(errorModel))
      lazy val result = controller.initialiseJourney(false)(request)

      "return InternalServerError" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }

  "The reverse route for the .initialiseJourney method" when {

    "called by an Agent" should {
      "be /change-business-address?isAgent=true" in {
        controllers.routes.BusinessAddressController.initialiseJourney(isAgent = true) shouldBe
          "/vat-through-software/account/change-business-address?isAgent=true"
      }
    }
  }
}
