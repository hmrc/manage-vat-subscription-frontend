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

package mocks

import controllers.predicates.{AgentOnlyAuthPredicate, AuthPredicate, AuthoriseAsAgent, AuthoriseAsPrinciple}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import _root_.services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.{Retrieval, ~}
import utils.TestUtil

import scala.concurrent.Future

trait MockAuth extends TestUtil with BeforeAndAfterEach with MockitoSugar  {

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

  val mockAuthIndividualPredicate: AuthoriseAsPrinciple = new AuthoriseAsPrinciple(mockEnrolmentsAuthService,messagesApi,mockAppConfig)

  val mockAuthAgentPredicate: AuthoriseAsAgent = new AuthoriseAsAgent(mockEnrolmentsAuthService, messagesApi, mockAppConfig)

  val mockAuthPredicate: AuthPredicate =
    new AuthPredicate(
      mockEnrolmentsAuthService,
      messagesApi,
      mockAuthIndividualPredicate,
      mockAuthAgentPredicate,
      mockAppConfig
    )

  val mockAgentOnlyAuthPredicate: AgentOnlyAuthPredicate =
    new AgentOnlyAuthPredicate(
      mockEnrolmentsAuthService,
      messagesApi,
      mockAuthAgentPredicate,
      mockAppConfig
    )

  def mockIndividualAuthorised(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Individual),
        Enrolments(Set(Enrolment("HMRC-MTD-VAT",
          Seq(EnrolmentIdentifier("VRN", "999999999")),
          "Activated",
          None
        )))
      )
    ))

  def mockAgentAuthorised(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Agent),
        Enrolments(Set(Enrolment("HMRC-AS-AGENT",
          Seq(EnrolmentIdentifier("AgentReferenceNumber", "123456789")),
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
          "Activated",
          Some("mtd-vat-auth")
        )))
      )
    ))

  def mockUserWithoutEnrolment(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(Some(AffinityGroup.Individual),
        Enrolments(Set(Enrolment("OTHER_ENROLMENT",
          Seq(EnrolmentIdentifier("", "")),
          "",
          None
        )))
      )
    ))

  def mockUserWithoutAffinity(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(None,
        Enrolments(Set(Enrolment("HMRC-MTD-VAT",
          Seq(EnrolmentIdentifier("VRN", "999999999")),
          "Activated",
          None
        )))
      )
    ))

  def mockAgentWithoutAffinity(): OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.successful(
      new ~(None,
        Enrolments(Set(Enrolment("HMRC-AS-AGENT",
          Seq(EnrolmentIdentifier("AgentReferenceNumber", "123456789")),
          "Activated",
          Some("mtd-vat-auth")
        )))
      )
    ))

  lazy val mockUnauthenticated: OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.failed(MissingBearerToken()))

  lazy val mockUnauthorised: OngoingStubbing[Future[~[Option[AffinityGroup], Enrolments]]] =
    setupAuthResponse(Future.failed(InsufficientEnrolments()))

}
