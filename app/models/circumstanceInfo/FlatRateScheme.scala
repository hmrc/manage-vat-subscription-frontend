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

package models.circumstanceInfo

import play.api.libs.json.{__, Writes, Reads}
import play.api.libs.functional.syntax._

case class FlatRateScheme(FRSCategory: Option[String],
                          FRSPercentage: Option[BigDecimal],
                          limitedCostTrader: Option[Boolean],
                          startDate: Option[String])

object FlatRateScheme {

  private val frsCategoryPath = __ \ "FRSCategory"
  private val frsPerecentPath =  __ \ "FRSPercentage"
  private val limitedCostPath = __ \ "limitedCostTrader"
  private val startDatePath = __ \ "startDate"

  implicit val reads: Reads[FlatRateScheme] = (
    frsCategoryPath.readNullable[String] and
      frsPerecentPath.readNullable[BigDecimal] and
      limitedCostPath.readNullable[Boolean] and
      startDatePath.readNullable[String]
    )(FlatRateScheme.apply _)

  implicit val writes: Writes[FlatRateScheme] = (
    frsCategoryPath.writeNullable[String] and
      frsPerecentPath.writeNullable[BigDecimal] and
      limitedCostPath.writeNullable[Boolean] and
      startDatePath.writeNullable[String]
    )(unlift(FlatRateScheme.unapply))

}
