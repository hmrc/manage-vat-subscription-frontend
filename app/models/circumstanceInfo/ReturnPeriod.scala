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

import play.api.libs.json._

sealed trait ReturnPeriod {
  def stdReturnPeriod: String
}

case object MAReturnPeriod extends ReturnPeriod {
  override val stdReturnPeriod: String = "MA"
}

case object MBReturnPeriod extends ReturnPeriod {
  override val stdReturnPeriod: String = "MB"
}


case object MCReturnPeriod extends ReturnPeriod {
  override val stdReturnPeriod: String = "MC"
}


case object MMReturnPeriod extends ReturnPeriod {
  override val stdReturnPeriod: String = "MM"
}

object ReturnPeriod {

  def unapply(arg: ReturnPeriod): String = arg.stdReturnPeriod

  implicit val reads: Reads[ReturnPeriod] = for {
    value <- (__ \ "stdReturnPeriod").read[String] map {
      case MAReturnPeriod.stdReturnPeriod => MAReturnPeriod
      case MBReturnPeriod.stdReturnPeriod => MBReturnPeriod
      case MCReturnPeriod.stdReturnPeriod => MCReturnPeriod
      case MMReturnPeriod.stdReturnPeriod => MMReturnPeriod
    }
  } yield value

  implicit val writes: Writes[ReturnPeriod] = Writes {
    case period if period.stdReturnPeriod.trim.length > 0 => Json.obj("stdReturnPeriod" -> period.stdReturnPeriod)
    case _ => Json.obj()
  }
}
