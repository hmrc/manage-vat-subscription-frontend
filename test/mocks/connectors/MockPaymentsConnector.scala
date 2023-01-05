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

package mocks.connectors

import connectors.PaymentsConnector
import models.core.ErrorModel
import models.payments.{PaymentRedirectModel, PaymentStartModel}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future

trait MockPaymentsConnector extends AnyWordSpecLike with MockitoSugar with BeforeAndAfterEach {

  val mockPaymentsConnector: PaymentsConnector = mock[PaymentsConnector]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockPaymentsConnector)
  }

  def setupMockPostPaymentsDetails(model: PaymentStartModel)(response: Either[ErrorModel, PaymentRedirectModel]): Unit = {
    when(mockPaymentsConnector.postPaymentsDetails(ArgumentMatchers.eq(model))(any(), any()))
      .thenReturn(Future.successful(response))
  }

}
