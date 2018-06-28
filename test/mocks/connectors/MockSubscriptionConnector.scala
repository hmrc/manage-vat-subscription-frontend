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

package mocks.connectors

import connectors.SubscriptionConnector
import models.circumstanceInfo.{CircumstanceDetails, CustomerDetails}
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.play.test.UnitSpec
import org.mockito.ArgumentMatchers._

import scala.concurrent.Future

trait MockSubscriptionConnector extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockSubscriptionConnector: SubscriptionConnector = mock[SubscriptionConnector]

  type CustomerDetailsResponse = Either[ErrorModel, CustomerDetails]
  type SubscriptionUpdateResponse = Either[ErrorModel, SubscriptionUpdateResponseModel]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockSubscriptionConnector)
  }

  def setupMockUserDetails(vrn: String)(response: Either[ErrorModel, CircumstanceDetails]): Unit = {
    when(mockSubscriptionConnector.getCustomerCircumstanceDetails(ArgumentMatchers.eq(vrn))(ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(response))
  }

  def setupMockUpdateBusinessAddress(response: Either[ErrorModel, SubscriptionUpdateResponseModel]): Unit = {
    when(mockSubscriptionConnector.updatePPOB(anyString(), any())(any(), any()))
      .thenReturn(Future.successful(response))
  }

  def setupMockUpdateReturnFrequency(response: Either[ErrorModel, SubscriptionUpdateResponseModel]): Unit = {
    when(mockSubscriptionConnector.updateReturnFrequency(anyString(), any())(any(), any()))
      .thenReturn(Future.successful(response))
  }
}

