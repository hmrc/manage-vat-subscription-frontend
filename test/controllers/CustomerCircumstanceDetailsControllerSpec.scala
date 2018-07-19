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

import assets.BaseTestConstants.{arn, vrn}
import assets.messages.{CustomerCircumstanceDetailsPageMessages => Messages}
import assets.CircumstanceDetailsTestConstants._
import audit.mocks.MockAuditingService
import audit.models.{AuthenticateAgentAuditModel, ViewVatSubscriptionAuditModel}
import config.ServiceErrorHandler
import mocks.services.MockCustomerCircumstanceDetailsService
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class CustomerCircumstanceDetailsControllerSpec extends ControllerBaseSpec with MockCustomerCircumstanceDetailsService with MockAuditingService {

  object TestCustomerCircumstanceDetailsController extends CustomerCircumstanceDetailsController(
    mockAuthPredicate,
    mockCustomerDetailsService,
    app.injector.instanceOf[ServiceErrorHandler],
    mockAuditingService,
    mockConfig,
    messagesApi
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      lazy val result = TestCustomerCircumstanceDetailsController.show(request)
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(ViewVatSubscriptionAuditModel(user, customerInformationModelMaxOrganisation)),
            ArgumentMatchers.eq[Option[String]](Some(controllers.routes.CustomerCircumstanceDetailsController.show().url))
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the CustomerDetails Page" in {
        document.title shouldBe Messages.title
      }
    }

    "the user is authorised and an Error is returned" should {

      lazy val result = TestCustomerCircumstanceDetailsController.show(request)

      "return 500" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    unauthenticatedCheck(TestCustomerCircumstanceDetailsController.show)
  }
}
