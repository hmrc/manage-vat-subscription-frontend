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

import models.returnFrequency.ReturnPeriod
import play.api.libs.json._

case class UpdateReturnFrequencyAuditModel(agentReferenceNumber: Option[String],
                                           vrn: String,
                                           currentReturnFrequency: ReturnPeriod,
                                           requestedReturnFrequency: ReturnPeriod,
                                           formBundle: String) extends ExtendedAuditModel {

  override val transactionName: String = "change-vat-return-frequency"
  override val detail: JsValue = Json.toJson(this)
  override val auditType: String = "ChangeVatSubscriptionDetails"
}

object UpdateReturnFrequencyAuditModel {

  implicit val writes: Writes[UpdateReturnFrequencyAuditModel] = Writes { model =>

    val filteredJson = Json.obj(
      "isAgent" -> model.agentReferenceNumber.isDefined,
      "agentReferenceNumber" -> model.agentReferenceNumber,
      "vrn" -> model.vrn,
      "currentReturnFrequency" -> model.currentReturnFrequency.auditValue,
      "requestedReturnFrequency" -> model.requestedReturnFrequency.auditValue,
      "formBundle" -> model.formBundle
    ).fields.filterNot(_._2 == JsNull)

    JsObject(filteredJson)
  }
}
