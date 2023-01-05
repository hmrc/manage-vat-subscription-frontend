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

package mocks.services

import models.core.ErrorModel
import models.payments.PaymentRedirectModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar
import services.PaymentsService

import scala.concurrent.Future

trait MockPaymentsService extends AnyWordSpecLike with MockitoSugar with BeforeAndAfterEach {

  val mockPaymentsService: PaymentsService = mock[PaymentsService]

  type PaymentsResponse = Either[ErrorModel, PaymentRedirectModel]

  override def beforeEach(): Unit = {

    super.beforeEach()
    reset(mockPaymentsService)
  }

  def setupMockPaymentsService(response: PaymentsResponse): OngoingStubbing[Future[PaymentsResponse]] = {
    when(mockPaymentsService.postPaymentDetails
    (ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    (ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(response))
  }

}
