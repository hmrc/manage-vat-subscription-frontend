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

package mocks.services

import models.core.SubscriptionUpdateResponseModel
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.{reset, _}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar
import services.PPOBService

import scala.concurrent.Future

trait MockBusinessAddressService extends AnyWordSpecLike with MockitoSugar with BeforeAndAfterEach {

  val mockBusinessAddressService: PPOBService = mock[PPOBService]

  type BusinessAddressResponse = Either[models.core.Error, SubscriptionUpdateResponseModel]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockBusinessAddressService)
  }

  def setupMockBusinessAddress(response: BusinessAddressResponse): OngoingStubbing[Future[BusinessAddressResponse]]  = {
    when(mockBusinessAddressService.updatePPOB(any(), any(), anyString())(any(), any()))
      .thenReturn(Future.successful(response))
  }
}
