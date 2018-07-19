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

import play.api.libs.json._
import utils.JsonObjectSugar

case class BankAccountHandOffAuditModel(vrn: String,
                                        agentReferenceNumber: Option[String],
                                        repaymentsRedirectUrl: String) extends ExtendedAuditModel {

  override val transactionName: String = "start-change-repayment-bank-account-journey"
  override val detail: JsValue = Json.toJson(this)
  override val auditType: String = "StartChangeOfRepaymentBankAccount"
}

object BankAccountHandOffAuditModel extends JsonObjectSugar {
  implicit val writes: Writes[BankAccountHandOffAuditModel] = Writes { model =>
    jsonObjNoNulls(
      "isAgent" -> model.agentReferenceNumber.isDefined,
      "agentReferenceNumber" -> model.agentReferenceNumber,
      "vrn" -> model.vrn,
      "repaymentsRedirectUrl" -> model.repaymentsRedirectUrl
    )
  }
}

