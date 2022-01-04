/*
 * Copyright 2022 HM Revenue & Customs
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

import connectors.httpParsers.ResponseHttpParser.HttpPutResult
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import org.scalamock.handlers.CallHandler3
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.http.Status
import services.PPOBService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockPPOBService extends AnyWordSpecLike with MockFactory with BeforeAndAfterEach {

  val mockPPOBService: PPOBService = mock[PPOBService]

  def mockCall(vrn: String): CallHandler3[String, HeaderCarrier, ExecutionContext, Future[HttpPutResult[SubscriptionUpdateResponseModel]]] = {
    (mockPPOBService.validateBusinessAddress(_: String)(_: HeaderCarrier, _: ExecutionContext))
      .expects(vrn, *, *)
      .returning(Future.successful(Right(SubscriptionUpdateResponseModel(""))))
  }

  def mockFailedCall(vrn: String): CallHandler3[String, HeaderCarrier, ExecutionContext, Future[HttpPutResult[SubscriptionUpdateResponseModel]]] = {
    (mockPPOBService.validateBusinessAddress(_: String)(_: HeaderCarrier, _: ExecutionContext))
      .expects(vrn, *, *)
      .returning(Future.successful(Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Oops"))))
  }

}
