/*
 * Copyright 2024 HM Revenue & Customs
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

import play.api.libs.json.{Format, JsValue, Json}

case class GetClientBusinessNameAuditModel(agentReferenceNumber: String,
                                           vrn: String,
                                           businessName: String,
                                           isAgent: Boolean = true) extends ExtendedAuditModel {

  override val transactionName: String = GetClientBusinessNameAuditModel.agentTransactionName
  override val detail: JsValue = Json.toJson(this)
  override val auditType: String = GetClientBusinessNameAuditModel.agentAuditType
}

object GetClientBusinessNameAuditModel {

  val agentTransactionName = "agent-authenticated-for-client-confirmation"
  val agentAuditType = "AuthenticateAgentForClientConfirmation"

  implicit val format: Format[GetClientBusinessNameAuditModel] = Json.format[GetClientBusinessNameAuditModel]
}
