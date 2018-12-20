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

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants._
import assets.CustomerAddressTestConstants._
import assets.messages.{ChangeAddressConfirmationPageMessages, ChangeAddressPageMessages, EmailChangePendingMessages}
import audit.mocks.MockAuditingService
import mocks.services.{MockAddressLookupService, MockBusinessAddressService, MockCustomerCircumstanceDetailsService}
import models.core.SubscriptionUpdateResponseModel
import models.customerAddress.AddressLookupOnRampModel
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.Helpers.{redirectLocation, _}

import scala.concurrent.Future

class BusinessAddressControllerSpec extends ControllerBaseSpec with MockAddressLookupService with
  MockBusinessAddressService with MockAuditingService with MockCustomerCircumstanceDetailsService {

  "Calling the .show action" when {

    object TestBusinessAddressController extends BusinessAddressController(
      messagesApi,
      mockAuthPredicate,
      mockInflightEmailPredicate,
      mockAddressLookupService,
      mockBusinessAddressService,
      mockCustomerDetailsService,
      serviceErrorHandler,
      mockAuditingService,
      mockConfig
    )

    "the user is authorised and does not have any conflicting inflight data" should {

      lazy val result: Future[Result] = {
        mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
        TestBusinessAddressController.show(request)
      }

      "return OK (200)" in {
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      s"have the heading '${ChangeAddressPageMessages.title}'" in {
        Jsoup.parse(bodyOf(result)).select("h1").text shouldBe ChangeAddressPageMessages.title
      }
    }

    "the user is authorised and has a pending change to their email address" should {

      lazy val result: Future[Result] = {
        mockCustomerDetailsSuccess(customerInformationPendingEmailModel)
        TestBusinessAddressController.show(request)
      }

      "return OK (200)" in {
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      s"have the heading '${EmailChangePendingMessages.heading}'" in {
        Jsoup.parse(bodyOf(result)).select("h1").text shouldBe EmailChangePendingMessages.heading
      }
    }
  }

  "Calling .callback" when {

    def setup(addressLookupResponse: RetrieveAddressResponse,
              businessAddressResponse: BusinessAddressResponse): BusinessAddressController = {

      setupMockRetrieveAddress(addressLookupResponse)
      setupMockBusinessAddress(businessAddressResponse)

      new BusinessAddressController(
        messagesApi,
        mockAuthPredicate,
        mockInflightEmailPredicate,
        mockAddressLookupService,
        mockBusinessAddressService,
        mockCustomerDetailsService,
        serviceErrorHandler,
        mockAuditingService,
        mockConfig)
    }

    "address lookup service returns success" when {

      "and business address service returns success" should {

        def controller: BusinessAddressController = setup(
          addressLookupResponse = Right(customerAddressMax),
          businessAddressResponse = Right(SubscriptionUpdateResponseModel(""))
        )

        "for an Individual" should {

          lazy val result = controller.callback("12345")(request)

          "return See Other (303)" in {
            status(result) shouldBe Status.SEE_OTHER
          }

          "Redirect to the confirmation page" in {
            redirectLocation(result) shouldBe Some(controllers.routes.BusinessAddressController.confirmation("non-agent").url)
          }
        }

        "for an Agent" should {

          lazy val result = controller.callback("12345")(fakeRequestWithClientsVRN)

          "return See Other (303)" in {
            mockAgentAuthorised()
            status(result) shouldBe Status.SEE_OTHER
          }

          "Redirect to the confirmation page" in {
            redirectLocation(result) shouldBe Some(controllers.routes.BusinessAddressController.confirmation("agent").url)
          }
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
        mockInflightEmailPredicate,
        mockAddressLookupService,
        mockBusinessAddressService,
        mockCustomerDetailsService,
        serviceErrorHandler,
        mockAuditingService,
        mockConfig)
    }

    "address lookup service returns success" when {

      lazy val controller = setup(addressLookupResponse = Right(AddressLookupOnRampModel("redirect-url")))

      "the user does not have any conflicting inflight data" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
          controller.initialiseJourney(request)
        }

        "return redirect to the url returned" in {
          status(result) shouldBe Status.SEE_OTHER
        }

        "redirect to url returned" in {
          redirectLocation(result) shouldBe Some("redirect-url")
        }
      }

      "the user has a pending change to their email address" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationPendingEmailModel)
          controller.initialiseJourney(request)
        }

        "return OK (200)" in {
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }

        s"have the heading '${EmailChangePendingMessages.heading}'" in {
          Jsoup.parse(bodyOf(result)).select("h1").text shouldBe EmailChangePendingMessages.heading
        }
      }
    }

    "address lookup service returns an error" should {

      lazy val controller = setup(addressLookupResponse = Left(errorModel))
      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationNoPendingIndividual)
        controller.initialiseJourney(request)
      }

      "return InternalServerError" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }

  "calling .confirmation" when {

    lazy val controller = new BusinessAddressController(
      messagesApi,
      mockAuthPredicate,
      mockInflightEmailPredicate,
      mockAddressLookupService,
      mockBusinessAddressService,
      mockCustomerDetailsService,
      serviceErrorHandler,
      mockAuditingService,
      mockConfig)

    "there is an agent email in session" when {

      "the call to the customer details service is successful" should {

        lazy val result = {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
          controller.confirmation("agent")(agentUser)
        }

        "Return status 200 (OK)" in {
          status(result) shouldBe Status.OK
        }

        "render the Business Address confirmation view" in {
          Jsoup.parse(bodyOf(result)).title shouldBe ChangeAddressConfirmationPageMessages.title
        }
      }

      "the call to the customer details service is unsuccessful" should {

        lazy val result = {
          mockCustomerDetailsError()
          controller.confirmation("agent")(agentUser)
        }

        "Return status 200 (OK)" in {
          status(result) shouldBe Status.OK
        }

        "render the Business Address confirmation view" in {
          Jsoup.parse(bodyOf(result)).title shouldBe ChangeAddressConfirmationPageMessages.title
        }
      }
    }

    "there is no agent email in session" should {

      lazy val result = controller.confirmation("non-agent")(request)

      "Return status 200 (OK)" in {
        status(result) shouldBe Status.OK
      }

      "render the Business Address confirmation view" in {
        Jsoup.parse(bodyOf(result)).title shouldBe ChangeAddressConfirmationPageMessages.title
      }
    }
  }
}
