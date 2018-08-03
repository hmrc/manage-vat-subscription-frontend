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

import assets.BaseTestConstants.vrn
import mocks.connectors.MockAgentFastTrackConnector
import models.agentFastTrack.AgentFastTrackModel
import models.core.ErrorModel
import play.api.http.Status
import utils.TestUtil

class AgentFastTrackServiceSpec extends TestUtil with MockAgentFastTrackConnector {

  object TestAgentLookupService extends AgentFastTrackService(mockAgentFastTrackConnector, mockConfig)

  "Calling initialiseJourney" when {

    "connector call is successful" should {

      "return successful SubscriptionUpdateResponseModel" in {

        val success = Right("redirect-url")
        setupMockInitialiseJourney(AgentFastTrackModel(vrn))(success)

        await(TestAgentLookupService.initialiseJourney(vrn)) shouldBe success
      }
    }

    "connector call is unsuccessful" should {

      "return left error model" in {

        val err = Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Err"))
        setupMockInitialiseJourney(AgentFastTrackModel(vrn))(err)

        await(TestAgentLookupService.initialiseJourney(vrn)) shouldBe err
      }
    }
  }
}
