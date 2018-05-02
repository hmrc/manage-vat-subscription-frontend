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

package models.customerInfo

import play.api.libs.json._

sealed trait MandationStatus {
  val status: String
}
case object MTDfBMandated extends MandationStatus {
  override val status: String = "MTDfB Mandated"
}

case object MTDfBVoluntary extends MandationStatus {
  override val status: String = "MTDfB Voluntary"
}

case object NonMTDfB extends MandationStatus {
  override val status: String = "Non MTDfB"
}

case object NonDigital extends MandationStatus {
  override val status: String = "Non Digital"
}


object MandationStatus {

  def unapply(arg: MandationStatus): Option[String] = Some(arg.status)

  val reads: Reads[MandationStatus] = for {
    value <- JsPath.read[String].map {
      case MTDfBMandated.status => MTDfBMandated
      case MTDfBVoluntary.status => MTDfBVoluntary
      case NonMTDfB.status => NonMTDfB
      case NonDigital.status => NonDigital
    }
  } yield value

  val writes: Writes[MandationStatus] = Writes(
    (status: MandationStatus) => JsString(status.status)
  )

  implicit val format: Format[MandationStatus] = Format[MandationStatus](reads, writes)
}
