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

package utils

import assets.BaseTestConstants._
import com.codahale.metrics.SharedMetricRegistries
import common.SessionKeys
import config.ServiceErrorHandler
import mocks.MockAppConfig
import models.User
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.{FakeRequest, Injecting}
import uk.gov.hmrc.http.HeaderCarrier

import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext

trait TestUtil extends UnitSpec with GuiceOneAppPerSuite with BeforeAndAfterEach with MaterializerSupport with Injecting {

  override def beforeEach(): Unit = {
    super.beforeEach()
    mockConfig.features.showContactNumbersAndWebsite(true)
    mockConfig.features.allowAgentBankAccountChange(false)
    mockConfig.features.useLanguageSelector(true)
    mockConfig.features.disableBulkPaper(true)
    mockConfig.features.contactDetailsMovedToBTA(true)
    mockConfig.features.tradingNameRowEnabled(true)
    SharedMetricRegistries.clear()
  }

  lazy val mcc: MessagesControllerComponents = inject[MessagesControllerComponents]

  implicit lazy val messagesApi: MessagesApi = inject[MessagesApi]
  implicit lazy val messages: Messages = MessagesImpl(Lang("en-GB"), messagesApi)

  implicit lazy val config: Configuration = app.configuration
  implicit lazy val mockConfig: MockAppConfig = new MockAppConfig
  implicit lazy val serviceErrorHandler: ServiceErrorHandler = inject[ServiceErrorHandler]

  implicit lazy val request: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(SessionKeys.insolventWithoutAccessKey -> "false")
  implicit lazy val fakeRequestWithClientsVRN: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(SessionKeys.CLIENT_VRN -> vrn, SessionKeys.verifiedAgentEmail -> agentEmail)
  implicit lazy val fakeRequestWithVrnAndReturnFreq: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    SessionKeys.CLIENT_VRN -> vrn,
    SessionKeys.NEW_RETURN_FREQUENCY -> "Jan",
    SessionKeys.CURRENT_RETURN_FREQUENCY -> "Monthly"
  )
  lazy val insolventRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(SessionKeys.insolventWithoutAccessKey -> "true")

  implicit lazy val user: User[AnyContentAsEmpty.type] = User[AnyContentAsEmpty.type](vrn,active = true)(request)
  implicit lazy val trustUser: User[AnyContentAsEmpty.type] = User[AnyContentAsEmpty.type](trustVrn,active = true)(request)
  implicit lazy val agentUser: User[AnyContentAsEmpty.type] = User[AnyContentAsEmpty.type](vrn,active = true, Some(arn))(fakeRequestWithClientsVRN)
  implicit lazy val agentTrustUser: User[AnyContentAsEmpty.type] = User[AnyContentAsEmpty.type](trustVrn,active = true, Some(arn))(fakeRequestWithClientsVRN)
  implicit lazy val hc: HeaderCarrier = HeaderCarrier()
  implicit lazy val ec: ExecutionContext = inject[ExecutionContext]
}
