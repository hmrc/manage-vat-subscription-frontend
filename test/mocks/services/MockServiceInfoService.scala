/*
 * Copyright 2019 HM Revenue & Customs
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

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.twirl.api.Html
import services.ServiceInfoService
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext

trait MockServiceInfoService extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockServiceInfoService: ServiceInfoService = mock[ServiceInfoService]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockServiceInfoService)
  }

  def getPartial(response: Html)(implicit executionContext: ExecutionContext): Unit= {
    when(mockServiceInfoService.getPartial()(ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any()))
      .thenReturn(response)
  }
}