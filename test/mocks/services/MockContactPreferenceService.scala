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

package mocks.services

import assets.BaseTestConstants.{errorModel, vrn}
import models.contactPreferences.ContactPreference
import models.core.ErrorModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import services.ContactPreferenceService
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future

trait MockContactPreferenceService extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockContactPreferenceService: ContactPreferenceService = mock[ContactPreferenceService]

  type ContactPreferenceResponse = Either[ErrorModel, ContactPreference]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockContactPreferenceService)
  }

  def setupMockContactPreference(vrn: String)(response: ContactPreferenceResponse): OngoingStubbing[Future[ContactPreferenceResponse]] = {
    when(mockContactPreferenceService.getContactPreference(ArgumentMatchers.eq(vrn))(ArgumentMatchers.any(), ArgumentMatchers.any()))
      .thenReturn(Future.successful(response))
  }

  def mockContactPreferenceSuccess(contactPreference: ContactPreference): OngoingStubbing[Future[ContactPreferenceResponse]] =
    setupMockContactPreference(vrn)(Right(contactPreference))

  def mockContactPreferenceError(): OngoingStubbing[Future[ContactPreferenceResponse]] =
    setupMockContactPreference(vrn)(Left(errorModel))

}
