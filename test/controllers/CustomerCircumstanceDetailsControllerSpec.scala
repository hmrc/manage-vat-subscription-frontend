/*
 * Copyright 2023 HM Revenue & Customs
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
import assets.ReturnPeriodTestConstants.{returnPeriodFeb, returnPeriodJan}
import assets.messages.{CustomerCircumstanceDetailsPageMessages => Messages}
import audit.models.ViewVatSubscriptionAuditModel
import common.SessionKeys
import mocks.services.MockServiceInfoService
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import views.html.customerInfo.CustomerCircumstanceDetailsView

import scala.concurrent.ExecutionContext

class CustomerCircumstanceDetailsControllerSpec extends ControllerBaseSpec with MockServiceInfoService {

  val dummyHtml: Html = Html("""<div id="dummyHtml">Dummy html</div>""")

  object TestCustomerCircumstanceDetailsController extends CustomerCircumstanceDetailsController(
    mockAuthPredicate,
    mockCustomerDetailsService,
    serviceErrorHandler,
    mockAuditingService,
    mockServiceInfoService,
    inject[CustomerCircumstanceDetailsView],
    mcc
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      lazy val result = TestCustomerCircumstanceDetailsController.show(request.withSession(
        SessionKeys.mtdVatvcNewReturnFrequency -> returnPeriodJan,
        SessionKeys.mtdVatvcCurrentReturnFrequency -> returnPeriodFeb
      ))
      lazy val document = Jsoup.parse(contentAsString(result))

      "return 200" in {
        getPartial(Html(""))
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(ViewVatSubscriptionAuditModel(user, customerInformationModelMaxOrganisation)),
            ArgumentMatchers.eq[Option[String]](Some(controllers.routes.CustomerCircumstanceDetailsController.show.url))
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
        messages(document.select("h1").text) shouldBe Messages.heading
      }

      "remove the data" in {
        session(result).get(SessionKeys.mtdVatvcNewReturnFrequency) shouldBe None
        session(result).get(SessionKeys.mtdVatvcCurrentReturnFrequency) shouldBe None
      }
    }

    "the user is authorised and an Error is returned" should {

      lazy val result = TestCustomerCircumstanceDetailsController.show(request)

      "return 500" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        messages(Jsoup.parse(contentAsString(result)).title) shouldBe internalServerErrorTitleUser
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    "The reverse route for the .show method" should {

      "be the expected route" in {
        controllers.routes.CustomerCircumstanceDetailsController.show.url shouldBe
          "/vat-through-software/account/change-business-details"
      }
    }

    unauthenticatedCheck(TestCustomerCircumstanceDetailsController.show)

    insolvencyCheck(TestCustomerCircumstanceDetailsController.show)
  }
}