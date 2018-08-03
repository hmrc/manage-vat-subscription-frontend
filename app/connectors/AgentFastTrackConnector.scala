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

import config.AppConfig
import connectors.httpParsers.AgentFastTrackHttpParser._
import connectors.httpParsers.ResponseHttpParser.HttpPostResult
import javax.inject.{Inject, Singleton}
import models.agentFastTrack.AgentFastTrackModel
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AgentFastTrackConnector @Inject()(val http: HttpClient,
                                        val config: AppConfig) {

  def initialiseJourney(agentFastTrack: AgentFastTrackModel)
                      (implicit hc: HeaderCarrier,ec: ExecutionContext): Future[HttpPostResult[String]] = {

    val url = s"${config.agentInvitationsFrontendService}/invitations/agents/fast-track"

    http.POST[AgentFastTrackModel, HttpPostResult[String]](url, agentFastTrack)(implicitly, AgentFastTrackReads, hc, ec)
  }
}
