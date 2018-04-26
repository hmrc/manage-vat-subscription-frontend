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

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future

trait MockAuth extends UnitSpec with BeforeAndAfterEach with MockitoSugar {

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockAuthConnector)
    mockAuthorised()
  }

  val mockAuthConnector: AuthConnector = mock[AuthConnector]

  def setupAuthResponse(authResult: Future[Enrolments]): OngoingStubbing[Future[Enrolments]] = {
    when(mockAuthConnector.authorise[Enrolments](ArgumentMatchers.any(), ArgumentMatchers.any())(ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(authResult)
  }

  val mockEnrolmentsAuthService: EnrolmentsAuthService = new EnrolmentsAuthService(mockAuthConnector)

  def mockAuthorised(): OngoingStubbing[Future[Enrolments]] = setupAuthResponse(Future.successful(Enrolments(
    Set(
      Enrolment("HMRC-MTD-VAT",
        Seq(EnrolmentIdentifier("", "")),
        "",
        None)
    )
  )))
  def mockUnauthenticated(): OngoingStubbing[Future[Enrolments]] = setupAuthResponse(Future.failed(MissingBearerToken()))
  def mockUnauthorised(): OngoingStubbing[Future[Enrolments]] = setupAuthResponse(Future.failed(InsufficientEnrolments()))

}
