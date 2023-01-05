/*
 * Copyright 2023 HM Revenue & Customs
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

import models.circumstanceInfo.FlatRateScheme
import play.api.libs.json.{JsValue, Json}

object FlatRateSchemeTestConstants {

  val frsCategory = "003"
  val frsPercentage = 59.99
  val frsStartDate = "2001-01-01"
  val frsLimitedCostTrader = true


  val frsModelMax: FlatRateScheme = FlatRateScheme(Some(frsCategory), Some(frsPercentage), Some(frsLimitedCostTrader), Some(frsStartDate))
  val frsModelMin: FlatRateScheme = FlatRateScheme(None, None, None, None)

  val frsJsonMax: JsValue = Json.obj(
    "FRSCategory" -> frsCategory,
    "FRSPercentage" -> frsPercentage,
    "startDate" -> frsStartDate,
    "limitedCostTrader" -> frsLimitedCostTrader
  )
  val frsJsonMin: JsValue = Json.obj()


}
