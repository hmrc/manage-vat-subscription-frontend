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

package controllers

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants._
import assets.ReturnPeriodTestConstants.{returnPeriodFeb, returnPeriodJan}
import assets.messages.{CustomerCircumstanceDetailsPageMessages => Messages}
import audit.models.ViewVatSubscriptionAuditModel
import common.SessionKeys
import models.User
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import org.scalamock.scalatest.MockFactory
import play.api.http.Status
import play.api.mvc.{AnyContent, Request}
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import play.twirl.api.Html
import services.ServiceInfoService
import views.html.customerInfo.CustomerCircumstanceDetailsView

import scala.concurrent.ExecutionContext

class CustomerCircumstanceDetailsControllerSpec extends ControllerBaseSpec {

  val dummyHtml: Html = Html("""<div id="dummyHtml">Dummy html</div>""")
  object TestCustomerCircumstanceDetailsController extends CustomerCircumstanceDetailsController(
    mockAuthPredicate,
    mockCustomerDetailsService,
    serviceErrorHandler,
    mockAuditingService,
    mockServiceInfoService,
    inject[CustomerCircumstanceDetailsView],
    mcc,
    mockConfig,
    ec
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      lazy val result = TestCustomerCircumstanceDetailsController.show("non-agent")(request.withSession(
        SessionKeys.NEW_RETURN_FREQUENCY -> returnPeriodJan ,
        SessionKeys.CURRENT_RETURN_FREQUENCY -> returnPeriodFeb
      ))
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        getPartial(Html(""))
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(ViewVatSubscriptionAuditModel(user, customerInformationModelMaxOrganisation)),
            ArgumentMatchers.eq[Option[String]](Some(controllers.routes.CustomerCircumstanceDetailsController.show("non-agent").url))
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
        session(result).get(SessionKeys.NEW_RETURN_FREQUENCY) shouldBe None
        session(result).get(SessionKeys.CURRENT_RETURN_FREQUENCY) shouldBe None
      }
    }

    "the user is authorised and an Error is returned" should {

      lazy val result = TestCustomerCircumstanceDetailsController.show("non-agent")(request)

      "return 500" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        messages(Jsoup.parse(bodyOf(result)).title) shouldBe internalServerErrorTitle
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    "The reverse route for the .show method" when {

      "called by an Agent" should {
        "be //change-business-details?isAgent=true" in {
          controllers.routes.CustomerCircumstanceDetailsController.show("agent").url shouldBe
            "/vat-through-software/account/change-business-details/agent"
        }
      }
    }

    unauthenticatedCheck(TestCustomerCircumstanceDetailsController.show("non-agent"))
  }

  "calling the .redirect action" when {

    "the user is an Agent" should {

      "redirect to the 'agent' endpoint" in {
        mockAgentAuthorised()
        lazy val result = TestCustomerCircumstanceDetailsController.redirect()(agentUser)
        redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.show(agentUser.redirectSuffix).url)
      }
    }

    "the user is a Principal entity" should {

      "redirect to the 'non-agent' endpoint" in {
        lazy val result = TestCustomerCircumstanceDetailsController.redirect()(user)
        redirectLocation(result) shouldBe Some(controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url)
      }
    }

    unauthenticatedCheck(TestCustomerCircumstanceDetailsController.redirect())
  }
}
