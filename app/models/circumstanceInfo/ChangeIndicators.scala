/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ChangeIndicators(ppob: Boolean,
                            bankDetails: Boolean,
                            returnPeriod: Boolean,
                            deregister: Boolean)

object ChangeIndicators {

  private val ppobPath = __ \ "PPOBDetails"
  private val bankDetailsPath =  __ \ "bankDetails"
  private val returnPeriodPath = __ \ "returnPeriod"
  private val deregisterPath =  __ \ "deregister"

  implicit val reads: Reads[ChangeIndicators] = (
    ppobPath.read[Boolean] and
      bankDetailsPath.read[Boolean] and
      returnPeriodPath.read[Boolean] and
      deregisterPath.read[Boolean]
    )(ChangeIndicators.apply _)

}
