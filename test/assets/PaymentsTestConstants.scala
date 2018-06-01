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

package assets

import BaseTestConstants._
import models.core.ErrorModel
import models.payments.PaymentStartModel
import play.api.http.Status
import play.api.libs.json.{JsObject, Json}

object PaymentsTestConstants {

  val paymentStart1: PaymentStartModel = PaymentStartModel(vrn, agent = true, "someReturnUrl", "someBackUrl")

  val successPaymentsResponseJson: JsObject = Json.obj("nextUrl" -> "continueUrl")

  val successBadJson = Some(Json.obj("nextUrl" -> 1))

  val successPaymentsResponse: String = "continueUrl"

  val errorModel = ErrorModel(Status.BAD_REQUEST, "Error Message")

}