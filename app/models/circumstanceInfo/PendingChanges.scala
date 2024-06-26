/*
 * Copyright 2024 HM Revenue & Customs
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

import models.returnFrequency.ReturnPeriod
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Reads, Writes, __}

case class PendingChanges(ppob: Option[PPOB],
                          bankDetails: Option[BankDetails],
                          returnPeriod: Option[ReturnPeriod],
                          tradingName: Option[String],
                          businessName: Option[String])

object PendingChanges {

  private val ppobPath = __ \ "PPOBDetails"
  private val bankDetailsPath =  __ \ "bankDetails"
  private val returnPeriodPath = __ \ "returnPeriod" \ "stdReturnPeriod"
  private val tradingNamePath = __ \ "tradingName"
  private val businessNamePath = __ \ "organisationName"

  implicit val reads: Reads[PendingChanges] = (
    ppobPath.readNullable[PPOB] and
    bankDetailsPath.readNullable[BankDetails] and
    returnPeriodPath.readNullable[ReturnPeriod] and
    tradingNamePath.readNullable[String] and
    businessNamePath.readNullable[String]
  )(PendingChanges.apply _)

  val auditWrites: Writes[Option[PendingChanges]] = Writes {
    case Some(pending) =>
      Json.obj(
        "businessAddress" -> pending.ppob.isDefined,
        "repaymentBankDetails" -> pending.bankDetails.isDefined,
        "vatReturnDates" -> pending.returnPeriod.isDefined,
        "emailAddress" -> pending.ppob.fold(false)(_.contactDetails.fold(false)(_.emailAddress.isDefined))
      )
    case _ =>
      Json.obj(
        "businessAddress" -> false,
        "repaymentBankDetails" -> false,
        "vatReturnDates" -> false,
        "emailAddress" -> false
      )
  }
}
