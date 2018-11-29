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
import models.circumstanceInfo.{BankDetails, CircumstanceDetails, PendingChanges}
import play.api.libs.json._
import utils.JsonObjectSugar

case class ViewVatSubscriptionAuditModel(user: User[_], vatDetails: CircumstanceDetails) extends ExtendedAuditModel {

  override val transactionName: String = "view-vat-subscription-details"
  override val auditType: String = "GetVatSubscriptionDetails"
  override val detail: JsValue = Json.toJson(this)

}

object ViewVatSubscriptionAuditModel extends JsonObjectSugar {

  implicit val writes: Writes[ViewVatSubscriptionAuditModel] = Writes { model =>
    jsonObjNoNulls(
      "isAgent" -> model.user.arn.isDefined,
      "agentReferenceNumber" -> model.user.arn,
      "vrn" -> model.user.vrn,
      "businessName" -> model.vatDetails.customerDetails.organisationName,
      "businessAddress" -> Json.toJson(model.vatDetails.pendingPPOBAddress.fold(model.vatDetails.ppobAddress)(x => x)),
      "repaymentBankDetails" -> Json.toJson(model.vatDetails.pendingBankDetails.fold(model.vatDetails.bankDetails)(x => Some(x)))(BankDetails.auditWrites),
      "vatReturnFrequency" -> model.vatDetails.pendingReturnPeriod.fold(model.vatDetails.returnPeriod.map(_.auditValue).orNull)(x => x.auditValue),
      "email" -> model.vatDetails.ppob.contactDetails.map(_.emailAddress.map(x => x)),
      "inFlightChanges" -> Json.toJson(model.vatDetails.pendingChanges)(PendingChanges.auditWrites),
      "partyType" -> Json.toJson(model.vatDetails.partyType)
    )
  }
}
