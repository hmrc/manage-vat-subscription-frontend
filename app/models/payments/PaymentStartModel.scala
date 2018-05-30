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

package models.payments

import play.api.libs.json.{JsObject, Json, Writes}

case class PaymentStartModel(vrn: String, agent: Boolean, returnUrl: String, backUrl: String)

object PaymentStartModel {
  implicit val writes: Writes[PaymentStartModel] = new Writes[PaymentStartModel] {

    def writes(data: PaymentStartModel): JsObject = Json.obj(fields =
      "vrn" -> data.vrn,
      "agent" -> data.agent,
      "returnUrl" -> data.returnUrl,
      "backUrl" -> data.backUrl
    )

  }
}
