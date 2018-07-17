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

package models.returnFrequency

import play.api.Logger
import play.api.libs.json._

sealed trait ReturnPeriod {
  def id: String
  def internalId: String
}

case object Jan extends ReturnPeriod {
  override val internalId: String = "MA"
  override val id: String = "January"
}

case object Feb extends ReturnPeriod {
  override val internalId: String = "MB"
  override val id: String = "February"
}

case object Mar extends ReturnPeriod {
  override val internalId: String = "MC"
  override val id: String = "March"
}

case object Monthly extends ReturnPeriod {
  override val internalId: String = "MM"
  override val id: String = "Monthly"
}


object ReturnPeriod {

  def apply(arg: String): Option[ReturnPeriod] = arg match {
    case Jan.id => Some(Jan)
    case Feb.id => Some(Feb)
    case Mar.id => Some(Mar)
    case Monthly.id => Some(Monthly)
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
    }
  } yield value

  implicit val writes: Writes[ReturnPeriod] = Writes {
    period => Json.obj("stdReturnPeriod" -> period.internalId)
  }

  val auditWrites: Writes[Option[ReturnPeriod]] = Writes {
    case Some(period) => {
      val auditValue = period.internalId match {
        case Jan.internalId => "January, April, July and October"
        case Feb.internalId => "February, May, August and November"
        case Mar.internalId => "March, June, September and December"
        case Monthly.internalId => Monthly.id
      }
      Json.obj("vatReturnFrequency" -> auditValue)
    }
    case _ => JsNull
  }
}