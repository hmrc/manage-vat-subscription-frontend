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

import models.core.ErrorModel
import models.customerAddress.AddressModel
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.reset
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import services.AddressLookupService
import uk.gov.hmrc.play.test.UnitSpec
import org.mockito.Mockito._
import scala.concurrent.Future

trait MockAddressLookupService extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockAddressLookupService: AddressLookupService = mock[AddressLookupService]

  type AddressLookupResponse = Either[ErrorModel, AddressModel]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockAddressLookupService)
  }

  def setupMockAddressLookup(response: AddressLookupResponse): OngoingStubbing[Future[AddressLookupResponse]]  = {
    when(mockAddressLookupService.retrieveAddress(anyString())(any(), any()))
      .thenReturn(Future.successful(response))
  }
}
