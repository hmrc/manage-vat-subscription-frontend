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

package models.circumstanceInfo

import models.JsonReadUtil
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class FlatRateScheme(FRSCategory: Option[String], FRSPercentage: Option[BigDecimal],
                          limitedCostTrader: Option[Boolean], startDate: Option[String])

object FlatRateScheme extends JsonReadUtil {

  private val frsCategoryPath = JsPath \ "FRSCategory"
  private val frsPerecentPath =  JsPath \ "FRSPercentage"
  private val limitedCostPath = JsPath \ "limitedCostTrader"
  private val startDatePath = JsPath \ "startDate"

  implicit val frsReader: Reads[FlatRateScheme] = for {
    frsCategory <- frsCategoryPath.readOpt[String]
    frsPercentage <- frsPerecentPath.readOpt[BigDecimal]
    limitedCostTrader <- limitedCostPath.readOpt[Boolean]
    startDate <- startDatePath.readOpt[String]
  } yield FlatRateScheme(frsCategory, frsPercentage, limitedCostTrader, startDate)

  implicit val frsWriter: Writes[FlatRateScheme] = (
    frsCategoryPath.writeNullable[String] and
      frsPerecentPath.writeNullable[BigDecimal] and
      limitedCostPath.writeNullable[Boolean] and
      startDatePath.writeNullable[String]
    )(unlift(FlatRateScheme.unapply))

  implicit val format: Format[FlatRateScheme] = Format[FlatRateScheme](
    frsReader,
    frsWriter
  )
}
