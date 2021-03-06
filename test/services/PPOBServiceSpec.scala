/*
 * Copyright 2021 HM Revenue & Customs
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

package services

import mocks.connectors.MockSubscriptionConnector
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import assets.CustomerAddressTestConstants._
import assets.BaseTestConstants.{formBundle, vrn}
import assets.PPOBAddressTestConstants._
import assets.CircumstanceDetailsTestConstants.{customerInformationModelMaxOrganisation, partyType}
import audit.mocks.MockAuditingService
import audit.models.ChangeAddressAuditModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import uk.gov.hmrc.http.HeaderCarrier
import utils.TestUtil

import scala.concurrent.ExecutionContext

class PPOBServiceSpec extends TestUtil with MockSubscriptionConnector with MockAuditingService {

  def setup(subscriptionResponse: SubscriptionUpdateResponse): PPOBService = {

    setupMockUpdateBusinessAddress(subscriptionResponse)
    setupMockUserDetails(vrn)(Right(customerInformationModelMaxOrganisation))
    new PPOBService(mockSubscriptionConnector, mockAuditingService)
  }

  "Calling .updatePPOB" should {

    val subscriptionResult = SubscriptionUpdateResponseModel(formBundle)

    lazy val service = setup(Right(subscriptionResult))
    lazy val result = service.updatePPOB(user, customerAddressMax, "")

    "return successful SubscriptionUpdateResponseModel" in {
      await(result) shouldBe Right(subscriptionResult)

      verify(mockAuditingService)
        .extendedAudit(
          ArgumentMatchers.eq(ChangeAddressAuditModel(user, ppobAddressModelMax, customerAddressMax, Some(partyType))),
          ArgumentMatchers.eq[Option[String]](Some(controllers.routes.BusinessAddressController.callback("").url))
        )(
          ArgumentMatchers.any[HeaderCarrier],
          ArgumentMatchers.any[ExecutionContext]
        )
    }
  }

  "Calling .validateBusinessAddress" should {

    lazy val service = new PPOBService(mockSubscriptionConnector, mockAuditingService)

    "return a right when the subscription connector returns a right" in {
      val validateCallResult = Right(SubscriptionUpdateResponseModel(formBundle))

      lazy val result = {
        setupMockValidateBusinessAddress(vrn, validateCallResult)
        service.validateBusinessAddress(vrn)
      }

      await(result) shouldBe validateCallResult
    }

    "return a left when the subscription connector returns a left" in {
      val validateCallResult = Left(ErrorModel(Status.NOT_FOUND, "STUFF MISSING I SUPPOSE"))

      lazy val result = {
        setupMockValidateBusinessAddress(vrn, validateCallResult)
        service.validateBusinessAddress(vrn)
      }

      await(result) shouldBe validateCallResult
    }

  }
}
