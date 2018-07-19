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

import assets.CircumstanceDetailsTestConstants._
import assets.messages.ChangeBusinessNamePageMessages
import audit.mocks.MockAuditingService
import audit.models.HandOffToCOHOAuditModel
import mocks.services.MockCustomerCircumstanceDetailsService
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class ChangeBusinessNameControllerSpec extends ControllerBaseSpec with MockCustomerCircumstanceDetailsService with MockAuditingService {

  object TestChangeBusinessNameController extends ChangeBusinessNameController(
    mockAuthPredicate, mockCustomerDetailsService, serviceErrorHandler, mockAuditingService, mockConfig, messagesApi
  )

  "Calling the .show action" when {

    "the user is authorised and an Organisation Name exists" should {

      lazy val result: Future[Result] = TestChangeBusinessNameController.show(request)

      "return OK (200)" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      s"have the heading '${ChangeBusinessNamePageMessages.h1}'" in {
        Jsoup.parse(bodyOf(result)).select("h1").text shouldBe ChangeBusinessNamePageMessages.h1
      }
    }

    "the user is authorised and an Individual Name exists" should {

      lazy val result = TestChangeBusinessNameController.show(request)

      "return ISE (500)" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxIndividual)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user is authorised and an Error is returned from Customer Details" should {

      lazy val result = TestChangeBusinessNameController.show(request)

      "return ISE (500)" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    unauthenticatedCheck(TestChangeBusinessNameController.show)
  }


  "Calling the .handOffToCOHO action" when {

    "the user is authorised and an Organisation Name exists" should {

      lazy val result: Future[Result] = TestChangeBusinessNameController.handOffToCOHO(request)

      "return OK (200)" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.SEE_OTHER

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(HandOffToCOHOAuditModel(user, customerInformationModelMaxOrganisation.customerDetails.organisationName.get)),
            ArgumentMatchers.eq[Option[String]](Some(controllers.routes.ChangeBusinessNameController.show().url))
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
      }

      s"redirect to COHO '${mockConfig.govUkCohoNameChangeUrl}'" in {
        redirectLocation(result) shouldBe Some(mockConfig.govUkCohoNameChangeUrl)
      }
    }

    "the user is authorised and an Individual Name exists" should {

      lazy val result = TestChangeBusinessNameController.handOffToCOHO(request)

      "return ISE (500)" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxIndividual)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user is authorised and an Error is returned from Customer Details" should {

      lazy val result = TestChangeBusinessNameController.handOffToCOHO(request)

      "return ISE (500)" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    unauthenticatedCheck(TestChangeBusinessNameController.handOffToCOHO)
  }

}
