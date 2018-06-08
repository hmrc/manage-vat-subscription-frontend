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

import assets.BaseTestConstants._
import models.circumstanceInfo.CircumstanceDetails
import models.core.ErrorModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future


trait MockCustomerCircumstanceDetailsService extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockCustomerDetailsService: CustomerCircumstanceDetailsService = mock[CustomerCircumstanceDetailsService]

  type CircumstanceDetailsResponse = Either[ErrorModel, CircumstanceDetails]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockCustomerDetailsService)
  }

  def setupMockCustomerDetails(vrn: String)(response: CircumstanceDetailsResponse): OngoingStubbing[Future[CircumstanceDetailsResponse]] = {
    when(mockCustomerDetailsService.getCustomerCircumstanceDetails(ArgumentMatchers.eq(vrn))(ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(response))
  }

  def mockCustomerDetailsSuccess(customerDetails: CircumstanceDetails): OngoingStubbing[Future[CircumstanceDetailsResponse]] = setupMockCustomerDetails(vrn)(Right(customerDetails))
  def mockCustomerDetailsError(): OngoingStubbing[Future[CircumstanceDetailsResponse]] = setupMockCustomerDetails(vrn)(Left(errorModel))
}

