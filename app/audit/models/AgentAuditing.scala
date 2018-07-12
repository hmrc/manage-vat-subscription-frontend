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

package audit.models

import models.User
import play.api.libs.json.{JsValue, Json}

object AgentAuditing {

  val agentTransactionName = "authenticate-agent-for-client"
  val agentAuditType = "AuthenticateAgentForClient"

  case class AgentAuditModel[A](user: User[A], authorisedForClient: Boolean) extends ExtendedAuditModel {
    override val transactionName: String = agentTransactionName

    private case class AuditDetail(isAgent: Boolean,
                                   agentReferenceNumber: Option[String],
                                   requestedClientVrn: String,
                                   isAuthorisedForClient: Boolean)

    override val detail: JsValue = Json.toJson(
      AuditDetail(
        user.isAgent,
        user.arn,
        user.vrn,
        true
      )
    )
    override val auditType: String = agentAuditType
  }

}
