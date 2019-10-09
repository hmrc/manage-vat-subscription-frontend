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

package models.returnFrequency

import play.api.Logger
import play.api.libs.json._

sealed trait ReturnPeriod {
  def id: String
  def internalId: String
  def auditValue: String
}

case object Jan extends ReturnPeriod {
  override val internalId: String = "MA"
  override val id: String = "January"
  override val auditValue: String = "January, April, July and October"
}

case object Feb extends ReturnPeriod {
  override val internalId: String = "MB"
  override val id: String = "February"
  override val auditValue: String = "February, May, August and November"
}

case object Mar extends ReturnPeriod {
  override val internalId: String = "MC"
  override val id: String = "March"
  override val auditValue: String = "March, June, September and December"
}

case object Monthly extends ReturnPeriod {
  override val internalId: String = "MM"
  override val id: String = "Monthly"
  override val auditValue: String = "Every month"
}

case object Annual extends ReturnPeriod {
  override val internalId: String = "AN"
  override val id: String = "Annually"
  override val auditValue: String = "Annually"
}

object ReturnPeriod {

  val validAnnualPeriodKeys = Seq("YA", "YB", "YC", "YD", "YE", "YF", "YG", "YH", "YI", "YJ", "YK", "YL")

  def apply(arg: String): Option[ReturnPeriod] = arg match {
    case Jan.id => Some(Jan)
    case Feb.id => Some(Feb)
    case Mar.id => Some(Mar)
    case Monthly.id => Some(Monthly)
    case Annual.id => Some(Annual)
    case unknown =>
      Logger.warn(s"[ConfirmVatDatesController].[getReturnFrequency] Session contains invalid frequency: $unknown")
      None
  }

  def unapply(arg: ReturnPeriod): String = arg.id

  implicit val reads: Reads[ReturnPeriod] = for {
    value <- (__ \ "stdReturnPeriod").read[String] map {
      case Jan.internalId => Jan
      case Feb.internalId => Feb
      case Mar.internalId => Mar
      case Monthly.internalId => Monthly
      case otherPeriodKey if validAnnualPeriodKeys.contains(otherPeriodKey) => Annual
    }
  } yield value

  implicit val writes: Writes[ReturnPeriod] = Writes {
    period => Json.obj("stdReturnPeriod" -> period.internalId)
  }
}
