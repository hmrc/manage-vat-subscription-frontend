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

package services

import connectors.AgentClientRelationshipConnector
import models.core.ErrorModel
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import utils.TestUtil
import assets.BaseTestConstants._

import scala.concurrent.Future

class AgentClientRelationshipServiceSpec extends TestUtil with MockitoSugar {

  private trait Test {
    val mockConnector: AgentClientRelationshipConnector = mock[AgentClientRelationshipConnector]
    val connectorResponse: Either[ErrorModel, Boolean]

    def setupMocks(): Unit = {
      when(mockConnector.checkRelationship(any(), any())(any(), any()))
        .thenReturn(Future.successful(connectorResponse))
    }

    def target(): AgentClientRelationshipService = {
      setupMocks()
      new AgentClientRelationshipService(mockConnector, mockAppConfig)
    }
  }

  "Calling .checkRelationship" when {

    "A relationship exists" should {
      "return true" in new Test {
        override val connectorResponse = Right(true)
        await(target().checkRelationship(arn, vrn)) shouldBe Right(true)
      }
    }

    "A relationship does not exist" should {
      "return false" in new Test {
        override val connectorResponse = Right(false)
        await(target().checkRelationship(arn, vrn)) shouldBe Right(false)
      }
    }

    "A a downstream error occurs" should {
      "return an error" in new Test {
        val error = ErrorModel(1, "Some error occurred")
        override val connectorResponse = Left(error)
        await(target().checkRelationship(arn, vrn)) shouldBe Left(error)
      }
    }

  }
}
