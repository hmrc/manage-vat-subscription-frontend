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

import connectors.SubscriptionConnector
import models.circumstanceInfo.CircumstanceDetails
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import models.updatePPOB.UpdatePPOB
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockSubscriptionConnector extends AnyWordSpecLike with MockFactory with BeforeAndAfterEach with Matchers {

  val mockSubscriptionConnector: SubscriptionConnector = mock[SubscriptionConnector]

  type SubscriptionUpdateResponse = Either[ErrorModel, SubscriptionUpdateResponseModel]

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  def setupMockUserDetails(vrn: String)(response: Either[ErrorModel, CircumstanceDetails]): Unit = {
    (mockSubscriptionConnector.getCustomerCircumstanceDetails(_: String)(_: HeaderCarrier, _: concurrent.ExecutionContext))
      .expects(vrn, *, *)
      .returns(Future.successful(response))
  }

  def setupMockUpdateBusinessAddress(response: SubscriptionUpdateResponse): Unit = {
    (mockSubscriptionConnector.updatePPOB(_: String, _: UpdatePPOB)(_: HeaderCarrier, _: ExecutionContext))
      .expects(*, *, *, *)
      .returns(Future.successful(response))
  }

  def setupMockValidateBusinessAddress(vrn: String, response: SubscriptionUpdateResponse): Unit = {
    (mockSubscriptionConnector.validateBusinessAddress(_: String)(_: HeaderCarrier, _: ExecutionContext))
      .expects(vrn, *, *)
      .returns(Future.successful(response))
  }
}

