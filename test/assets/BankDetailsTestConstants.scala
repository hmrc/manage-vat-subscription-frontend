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

import models.circumstanceInfo.BankDetails
import play.api.libs.json.{JsValue, Json}

object BankDetailsTestConstants {

  val accName = "**********************"
  val accNum = "**7425"
  val accSort = "69***"

  val bankDetailsModelMax: BankDetails = BankDetails(Some(accName), Some(accNum), Some(accSort))
  val bankDetailsModelMin: BankDetails = BankDetails(None, None, None)

  val bankDetailsJsonMax: JsValue = Json.obj(
    "accountHolderName" -> accName,
    "bankAccountNumber" -> accNum,
    "sortCode" -> accSort
  )
  val bankDetailsJsonMin: JsValue = Json.obj()

}
