/*
 * Copyright 2019 HM Revenue & Customs
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

import play.api.libs.json.{JsValue, Json}

object ReturnPeriodTestConstants {

  val returnPeriodJan: String = "January"
  val returnPeriodFeb: String = "February"
  val returnPeriodMar: String = "March"
  val returnPeriodMonthly: String = "Monthly"

  val returnPeriodMA: String = "MA"
  val returnPeriodMB: String = "MB"
  val returnPeriodMC: String = "MC"
  val returnPeriodMM: String = "MM"

  val returnPeriodMAJson: JsValue = Json.obj("stdReturnPeriod" -> returnPeriodMA)
  val returnPeriodMBJson: JsValue = Json.obj("stdReturnPeriod" -> returnPeriodMB)
  val returnPeriodMCJson: JsValue = Json.obj("stdReturnPeriod" -> returnPeriodMC)
  val returnPeriodMMJson: JsValue = Json.obj("stdReturnPeriod" -> returnPeriodMM)

}
