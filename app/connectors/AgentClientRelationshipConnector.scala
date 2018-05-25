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

import config.FrontendAppConfig
import connectors.httpParsers.AgentClientRelationshipHttpParser.AgentClientRelationshipCheckReads
import connectors.httpParsers.ResponseHttpParser.HttpGetResult
import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AgentClientRelationshipConnector @Inject()(val http: HttpClient,
                                                 val config: FrontendAppConfig) {

  private[connectors] def getRelationshipCheckUrl(arn: String, vrn: String) = {
    val baseUrl = config.agentClientRelationshipBaseUrl + "/agent-client-relationships"
    s"$baseUrl/agent/$arn/service/HMRC-MTD-VAT/client/VRN/$vrn"
  }

  def checkRelationship(arn: String, vrn: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpGetResult[Boolean]] = {
    http.GET[HttpGetResult[Boolean]](getRelationshipCheckUrl(arn, vrn))(AgentClientRelationshipCheckReads, hc, ec)
  }
}
