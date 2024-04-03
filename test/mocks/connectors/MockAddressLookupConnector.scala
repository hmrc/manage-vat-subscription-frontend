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

package mocks.connectors

import connectors.AddressLookupConnector
import models.core.ErrorModel
import models.customerAddress.{AddressLookupOnRampModel, AddressModel}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future

trait MockAddressLookupConnector extends AnyWordSpecLike with MockitoSugar with BeforeAndAfterEach {

  val mockAddressLookupConnector: AddressLookupConnector = mock[AddressLookupConnector]

  type AddressLookupGetAddressResponse = Either[ErrorModel, AddressModel]

  type AddressLookupInitialiseResponse = Either[ErrorModel, AddressLookupOnRampModel]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockAddressLookupConnector)
  }

  def setupMockGetAddress(response: Either[ErrorModel, AddressModel]): Unit = {
    when(mockAddressLookupConnector.getAddress(any())(any(), any()))
      .thenReturn(Future.successful(response))
  }

  def setupMockInitialiseJourney(response: Either[ErrorModel, AddressLookupOnRampModel]): Unit = {
    when(mockAddressLookupConnector.initialiseJourney(any())(any(), any()))
      .thenReturn(Future.successful(response))
  }
}

