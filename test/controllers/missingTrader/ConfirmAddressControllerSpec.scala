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

package controllers.missingTrader

import assets.BaseTestConstants.vrn
import assets.CircumstanceDetailsTestConstants.{customerInformationModelMaxIndividual, customerInformationModelMin}
import assets.messages.MissingTraderAddressConfirmationPageMessages
import audit.models.MissingTraderAuditModel
import common.SessionKeys
import controllers.ControllerBaseSpec
import forms.MissingTraderForm
import mocks.services.MockPPOBService
import models.core.ErrorModel
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._
import views.html.missingTrader.{ConfirmBusinessAddressView, MissingTraderAddressConfirmationView}

class ConfirmAddressControllerSpec extends ControllerBaseSpec with MockPPOBService {

  val controller = new ConfirmAddressController(
    mcc,
    mockAuthPredicate,
    mockCustomerDetailsService,
    serviceErrorHandler,
    inject[ConfirmBusinessAddressView],
    mockAuditingService,
    inject[MissingTraderAddressConfirmationView],
    mockPPOBService
  )

  "The .show action" when {

    "the user is a missing trader" when {

      "the missingTraderConfirmedAddressKey is in session" should {
        lazy val result = {
          setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxIndividual))
          controller.show(request.withSession(SessionKeys.missingTraderConfirmedAddressKey -> "true"))
        }

        "return 200" in {
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }

        s"have the h1 as ${MissingTraderAddressConfirmationPageMessages.h1}" in {
          messages(Jsoup.parse(contentAsString(result)).select("h1").text) shouldBe MissingTraderAddressConfirmationPageMessages.h1
        }
      }

      "the missingTraderConfirmedAddressKey is not in session" should {
        lazy val result = {
          setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxIndividual))
          controller.show(request)
        }

        "return 200" in {
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }

        "send an audit event" in {
          verifyExtendedAudit(MissingTraderAuditModel(vrn), Some(routes.ConfirmAddressController.show().url))
        }
      }
    }

    "the user is not a missing trader" when {

      "the user is an agent" should {

        lazy val result = {
          mockAgentAuthorised()
          setupMockCustomerDetails(vrn)(Right(customerInformationModelMin))
          controller.show(fakeRequestWithClientsVRN)
        }

        "return 303" in {
          status(result) shouldBe Status.SEE_OTHER
        }

        "redirect to the agent overview URL" in {
          redirectLocation(result) shouldBe Some(mockConfig.agentClientLookupAgentAction)
        }
      }

      "the user is a principal entity" should {

        lazy val result = {
          setupMockCustomerDetails(vrn)(Right(customerInformationModelMin))
          controller.show(request)
        }

        "return 303" in {
          status(result) shouldBe Status.SEE_OTHER
        }

        "redirect to the VAT overview URL" in {
          redirectLocation(result) shouldBe Some(mockConfig.vatSummaryUrl)
        }
      }
    }

    "there is an error calling the customer details service" should {

      lazy val result = {
        setupMockCustomerDetails(vrn)(Left(ErrorModel(Status.NOT_FOUND, "Subscription not found")))
        controller.show(request)
      }

      "return 500" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    insolvencyCheck(controller.show)
  }

  "The .submit action" when {

    "the form is submitted with a 'Yes' option" should {

      lazy val result = {
        setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxIndividual))
        mockCall(vrn)
        controller.submit(request.withFormUrlEncodedBody(MissingTraderForm.yesNo -> MissingTraderForm.yes))
      }

      "return 200" in {
        status(result) shouldBe Status.OK
      }

      "add the missingTraderConfirmedAddressKey to session" in {
        session(result).get(SessionKeys.missingTraderConfirmedAddressKey) shouldBe Some("true")
      }
    }

    "the form is submitted with a 'Yes' option but validate address returns an error" should {

      lazy val result = {
        setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxIndividual))
        mockFailedCall(vrn)
        controller.submit(request.withFormUrlEncodedBody(MissingTraderForm.yesNo -> MissingTraderForm.yes))
      }

      "return 200" in {
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the form is submitted with a 'No' option" should {

      lazy val result = controller.submit(request.withFormUrlEncodedBody(MissingTraderForm.yesNo -> MissingTraderForm.no))

      "return 303" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the initialise PPOB journey action" in {
        redirectLocation(result) shouldBe Some(controllers.routes.BusinessAddressController.initialiseJourney().url)
      }
    }

    "there are errors in the form" should {

      lazy val result = {
        setupMockCustomerDetails(vrn)(Right(customerInformationModelMaxIndividual))
        controller.submit(request.withFormUrlEncodedBody())
      }

      "return 400" in {
        status(result) shouldBe Status.BAD_REQUEST
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    insolvencyCheck(controller.submit)
  }
}
