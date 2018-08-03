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

package connectors

import assets.BaseTestConstants.vrn
import connectors.httpParsers.ResponseHttpParser.HttpPostResult
import mocks.MockHttp
import models.agentFastTrack.AgentFastTrackModel
import models.core.ErrorModel
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

import scala.concurrent.Future

class AgentFastTrackConnectorSpec extends TestUtil with MockHttp {

  val errorModel = HttpResponse(Status.BAD_REQUEST, responseString = Some("Error Message"))

  object TestAgentFastTrackConnector extends AgentFastTrackConnector(mockHttp, mockConfig)

  "AgentFastTrackConnector" when {

    val continueUrl = "continue-url"

    def postPaymentsDetailsResult: Future[HttpPostResult[String]] =
      TestAgentFastTrackConnector.initialiseJourney(AgentFastTrackModel(vrn))

    "for initialiseJourney method" when {

      "when given a successful response" should {

        "return a Right with the redirect path" in {
          setupMockHttpPost(s"${mockConfig.agentInvitationsFrontendService}/invitations/agents/fast-track")(Right(continueUrl))
          await(postPaymentsDetailsResult) shouldBe Right(continueUrl)
        }
      }

      "given an unsuccessful response" should {

        "return an Left with an ErrorModel" in {
          val failedResponse = Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Bad things"))
          setupMockHttpPost(s"${mockConfig.agentInvitationsFrontendService}/invitations/agents/fast-track")(failedResponse)
          await(postPaymentsDetailsResult) shouldBe failedResponse
        }
      }
    }
  }

}
