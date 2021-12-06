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

package controllers

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants._
import assets.CustomerDetailsTestConstants.organisation
import assets.messages.ChangeBusinessNamePageMessages
import audit.models.HandOffToCOHOAuditModel
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import views.html.businessName.{AltChangeBusinessNameView, ChangeBusinessNameView}

import scala.concurrent.{ExecutionContext, Future}

class ChangeBusinessNameControllerSpec extends ControllerBaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  object TestChangeBusinessNameController extends ChangeBusinessNameController(
    mockAuthPredicate,
    mockCustomerDetailsService,
    serviceErrorHandler,
    mockAuditingService,
    inject[ChangeBusinessNameView],
    inject[AltChangeBusinessNameView],
    mcc,
    mockConfig,
    ec
  )

  "Calling the .show action" when {

    "the user has the customer information necessary to access the page" when {

      "the user's data is mastered in ETMP" should {

        lazy val result: Future[Result] = {
          mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
          TestChangeBusinessNameController.show(request)
        }

        "return SEE_OTHER (303)" in {
          status(result) shouldBe Status.SEE_OTHER
        }

        "redirect to the change business name page on vat-designatory-details-frontend" in {
          redirectLocation(result) shouldBe Some(mockConfig.vatDesignatoryDetailsBusinessNameUrl)
        }
      }

      "the user's data is not mastered in ETMP" when {

        "the user has a party type to indicate their org name is mastered in NSP/ITMP" should {

          lazy val result: Future[Result] = {
            mockCustomerDetailsSuccess(
              customerInformationModelMaxOrganisation.copy(
                customerDetails = organisation.copy(nameIsReadOnly = Some(true)),
                partyType = Some("Z1"))
            )
            TestChangeBusinessNameController.show(request)
          }

          lazy val body = Jsoup.parse(contentAsString(result))

          "return OK (200)" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "have the correct heading" in {
            body.select("h1").text shouldBe ChangeBusinessNamePageMessages.heading
          }

          "have the correct guidance regarding making the change via an alternate service" in {
            body.select("p.govuk-body:nth-child(3)").text shouldBe ChangeBusinessNamePageMessages.altP2
          }

          "have a link to the GOV.UK change business details page" in {
            body.select(".govuk-body > a").attr("href") shouldBe mockConfig.govUkChangeToBusinessDetails
          }
        }

        "the user has a party type to indicate they are a charity or trust" should {

          lazy val result: Future[Result] = {
            mockCustomerDetailsSuccess(
              customerInformationModelMaxOrganisation.copy(
                customerDetails = organisation.copy(nameIsReadOnly = Some(true)),
                partyType = Some("9"))
            )
            TestChangeBusinessNameController.show(request)
          }

          lazy val body = Jsoup.parse(contentAsString(result))

          "return OK (200)" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "have the correct heading" in {
            body.select("h1").text shouldBe ChangeBusinessNamePageMessages.heading
          }

          "have the correct guidance regarding making the change via the Charities Commission" in {
            body.select("p.govuk-body:nth-child(3)").text shouldBe ChangeBusinessNamePageMessages.altP2Trust
          }

          "have a link to the GOV.UK trusts/charities name change page" in {
            body.select(".govuk-body > a").attr("href") shouldBe mockConfig.govUkTrustNameChangeUrl
          }
        }

        "the user has any other supported party type" should {

          lazy val result: Future[Result] = {
            mockCustomerDetailsSuccess(
              customerInformationModelMaxOrganisation.copy(
                customerDetails = organisation.copy(nameIsReadOnly = Some(true)),
                partyType = Some("4"))
            )
            TestChangeBusinessNameController.show(request)
          }

          lazy val body = Jsoup.parse(contentAsString(result))

          "return OK (200)" in {
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "have the correct heading" in {
            body.select("h1").text shouldBe ChangeBusinessNamePageMessages.heading
          }

          "have a link to Companies House" in {
            body.select(".govuk-body > a").attr("href") shouldBe routes.ChangeBusinessNameController.handOffToCOHO.url
          }
        }
      }
    }

    "the user does not have an organisation name" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationModelMaxIndividual)
        TestChangeBusinessNameController.show(request)
      }

      "return SEE_OTHER (303)" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the business details page" in {
        redirectLocation(result) shouldBe Some(routes.CustomerCircumstanceDetailsController.show.url)
      }
    }

    "the user has an overseas indicator of 'true'" should {

      lazy val result = {
        mockCustomerDetailsSuccess(overseasCompany)
        TestChangeBusinessNameController.show(request)
      }

      "return SEE_OTHER (303)" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the business details page" in {
        redirectLocation(result) shouldBe Some(routes.CustomerCircumstanceDetailsController.show.url)
      }
    }

    "the user has an invalid party type" should {

      lazy val result = {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation.copy(partyType = Some("99")))
        TestChangeBusinessNameController.show(request)
      }

      "return SEE_OTHER (303)" in {
        status(result) shouldBe Status.SEE_OTHER
      }

      "redirect to the business details page" in {
        redirectLocation(result) shouldBe Some(routes.CustomerCircumstanceDetailsController.show.url)
      }
    }

    "the user is authorised and an Error is returned from Customer Details" should {

      lazy val result = TestChangeBusinessNameController.show(request)

      "return ISE (500)" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        messages(Jsoup.parse(contentAsString(result)).title) shouldBe internalServerErrorTitleUser
      }
    }

    unauthenticatedCheck(TestChangeBusinessNameController.show)

    insolvencyCheck(TestChangeBusinessNameController.show)
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
            ArgumentMatchers.eq[Option[String]](Some(controllers.routes.ChangeBusinessNameController.show.url))
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
        messages(Jsoup.parse(contentAsString(result)).title) shouldBe internalServerErrorTitleUser
      }
    }

    "the user is authorised and an Error is returned from Customer Details" should {

      lazy val result = TestChangeBusinessNameController.handOffToCOHO(request)

      "return ISE (500)" in {
        mockCustomerDetailsError()
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        messages(Jsoup.parse(contentAsString(result)).title) shouldBe internalServerErrorTitleUser
      }
    }

    unauthenticatedCheck(TestChangeBusinessNameController.handOffToCOHO)

    insolvencyCheck(TestChangeBusinessNameController.handOffToCOHO)
  }
}
