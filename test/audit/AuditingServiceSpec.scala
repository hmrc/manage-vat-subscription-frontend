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

package audit

import audit.models.TestExtendedAuditModel
import config.FrontendAppConfig
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.HeaderNames
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.http.connector.AuditResult.Success
import utils.TestUtil

import scala.concurrent.{ExecutionContext, Future}

class AuditingServiceSpec extends TestUtil with MockitoSugar {

  val mockAuditConnector: AuditConnector = mock[AuditConnector]
  val mockConfiguration: FrontendAppConfig = mock[FrontendAppConfig]

  val testAuditingService = new AuditService(mockConfiguration, mockAuditConnector)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockAuditConnector, mockConfiguration)
  }

  "AuditService" should {

    "when calling the referer method" should {

      "extract the referer if there is one" in {
        val testPath = "/test/path"
        testAuditingService.referrer(HeaderCarrier().withExtraHeaders(HeaderNames.REFERER -> testPath)) shouldBe testPath
      }

      "default to hyphen '-' if there is no referrer" in {
        testAuditingService.referrer(HeaderCarrier()) shouldBe "-"
      }
    }

    "given an ExtendedAuditModel" should {

      "extract the data and pass it into the AuditConnector" in {

        val testModel = new TestExtendedAuditModel("foo", "bar")
        val testPath = "/test/path"
        val expectedData = testAuditingService.toExtendedDataEvent(mockConfiguration.appName, testModel, testPath)

        when(mockAuditConnector.sendExtendedEvent(
          ArgumentMatchers.refEq(expectedData, "eventId", "generatedAt")
        )(
          ArgumentMatchers.any[HeaderCarrier],
          ArgumentMatchers.any[ExecutionContext]
        )) thenReturn Future.successful(Success)

        testAuditingService.extendedAudit(testModel, Some(testPath))

        verify(mockAuditConnector)
          .sendExtendedEvent(
            ArgumentMatchers.refEq(expectedData, "eventId", "generatedAt")
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
      }
    }
  }
}
