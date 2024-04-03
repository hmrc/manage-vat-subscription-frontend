/*
 * Copyright 2024 HM Revenue & Customs
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

package mocks

import _root_.services.EnrolmentsAuthService
import assets.BaseTestConstants._
import controllers.predicates._
import mocks.services.MockCustomerCircumstanceDetailsService
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.mockito.stubbing.OngoingStubbing
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.{Retrieval, ~}
import utils.TestUtil
import views.html.errors.agent.UnauthorisedView
import views.html.errors.UserInsolventError
import views.html.errors.{ChangePendingView, NotSignedUpView}

import scala.concurrent.Future

trait MockAuth extends TestUtil with MockCustomerCircumstanceDetailsService {

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockAuthConnector)
    mockIndividualAuthorised()
  }

  lazy val mockAuthConnector: AuthConnector = mock[AuthConnector]

  def setupAuthResponse(authResult: Future[~[Option[AffinityGroup], Enrolments]]): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] = {
    when(mockAuthConnector.authorise(
      ArgumentMatchers.any(), ArgumentMatchers.any[Retrieval[~[Option[AffinityGroup], Enrolments]]]())(
      ArgumentMatchers.any(), ArgumentMatchers.any())
    ).thenReturn(authResult)
  }

  val mockEnrolmentsAuthService: EnrolmentsAuthService = new EnrolmentsAuthService(mockAuthConnector)

  val mockAuthAsAgentWithClient: AuthoriseAsAgentWithClient =
    new AuthoriseAsAgentWithClient(
      mockEnrolmentsAuthService,
      serviceErrorHandler,
      mcc
    )

  val mockAuthPredicate: AuthPredicate =
    new AuthPredicate(
      messagesApi,
      mockEnrolmentsAuthService,
      mockCustomerDetailsService,
      mockAuthAsAgentWithClient,
      serviceErrorHandler,
      inject[UnauthorisedView],
      inject[NotSignedUpView],
      inject[UserInsolventError],
      mcc
    )

  val mockAgentOnlyAuthPredicate: AuthoriseAsAgentOnly =
    new AuthoriseAsAgentOnly(
      mockEnrolmentsAuthService,
      serviceErrorHandler,
      inject[UnauthorisedView],
      mcc,
      mockConfig,
      ec
    )

  val mockInFlightPPOBPredicate: InFlightPPOBPredicate =
    new InFlightPPOBPredicate(
      mockCustomerDetailsService,
      serviceErrorHandler,
      inject[ChangePendingView],
      mcc,
      mockConfig,
      ec
    )

  val mockInFlightRepaymentBankAccountPredicate: InFlightRepaymentBankAccountPredicate =
    new InFlightRepaymentBankAccountPredicate(
      mockCustomerDetailsService,
      serviceErrorHandler
    )

  def mockIndividualAuthorised(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Individual),
        Enrolments(Set(Enrolment("HMRC-MTD-VAT",
          Seq(EnrolmentIdentifier("VRN", vrn)),
          "Activated"
        )))
      )
    ))

  def mockAgentAuthorised(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Agent),
        Enrolments(Set(Enrolment("HMRC-AS-AGENT",
          Seq(EnrolmentIdentifier("AgentReferenceNumber", arn)),
          "Activated",
          Some("mtd-vat-auth")
        )))
      )
    ))

  def mockAgentWithoutEnrolment(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Agent),
        Enrolments(Set(Enrolment("OTHER_ENROLMENT",
          Seq(EnrolmentIdentifier("", "")),
          "Activated"
        )))
      )
    ))

  def mockIndividualWithoutEnrolment(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Individual),
        Enrolments(Set(Enrolment("OTHER_ENROLMENT",
          Seq(EnrolmentIdentifier("", "")),
          ""
        )))
      )
    ))

  def mockUserWithoutAffinity(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(None,
        Enrolments(Set(Enrolment("HMRC-MTD-VAT",
          Seq(EnrolmentIdentifier("VRN", vrn)),
          "Activated"
        )))
      )
    ))

  def mockAgentWithoutAffinity(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(None,
        Enrolments(Set(Enrolment("HMRC-AS-AGENT",
          Seq(EnrolmentIdentifier("AgentReferenceNumber", arn)),
          "Activated",
          Some("mtd-vat-auth")
        )))
      )
    ))

  def mockMissingBearerToken(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.failed(MissingBearerToken()))

  def mockUnauthorised(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.failed(InsufficientEnrolments()))

}
