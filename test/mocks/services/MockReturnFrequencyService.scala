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

package mocks.services

import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.{reset, _}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import services.ReturnFrequencyService
import uk.gov.hmrc.play.test.UnitSpec
import assets.BaseTestConstants.formBundle

import scala.concurrent.Future

trait MockReturnFrequencyService extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockReturnFrequencyService: ReturnFrequencyService = mock[ReturnFrequencyService]

  type ServiceResponse = Either[ErrorModel, SubscriptionUpdateResponseModel]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockReturnFrequencyService)
  }

  def setupMockReturnFrequencyService(response: ServiceResponse): OngoingStubbing[Future[ServiceResponse]]  = {
    when(mockReturnFrequencyService.updateReturnFrequency(anyString(), any())(any(), any()))
      .thenReturn(Future.successful(response))
  }

  def setupMockReturnFrequencyServiceWithSuccess(): OngoingStubbing[Future[ServiceResponse]] = {
    setupMockReturnFrequencyService(Right(SubscriptionUpdateResponseModel(formBundle)))
  }

  def setupMockReturnFrequencyServiceWithFailure(): OngoingStubbing[Future[ServiceResponse]] = {
    setupMockReturnFrequencyService(Left(ErrorModel(500, "Boo")))
  }
}
