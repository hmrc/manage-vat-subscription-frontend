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

package models.circumstanceInfo

import play.api.libs.json._

sealed trait MandationStatus {
  def value: String
}

case object MTDfBMandated extends MandationStatus {
  override val value: String = "MTDfB Mandated"
}

case object MTDfBVoluntary extends MandationStatus {
  override val value: String = "MTDfB Voluntary"
}


case object NonMTDfB extends MandationStatus {
  override val value: String = "Non MTDfB"
}


case object NonDigital extends MandationStatus {
  override val value: String = "Non Digital"
}


object MandationStatus {

  def unapply(arg: MandationStatus): Option[String] = Some(arg.value)

  implicit val reads: Reads[MandationStatus] = for {
    status <- JsPath.read[String].map {
      case MTDfBMandated.value => MTDfBMandated
      case MTDfBVoluntary.value => MTDfBVoluntary
      case NonMTDfB.value => NonMTDfB
      case NonDigital.value => NonDigital
    }
  } yield status

  implicit val writes: Writes[MandationStatus] = Writes(
    status => JsString(status.value)
  )
}
