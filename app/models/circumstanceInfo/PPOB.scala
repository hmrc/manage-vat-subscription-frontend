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
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PPOB(address: Option[PPOBAddress])

object PPOB extends JsonReadUtil {

  private val addressPath = JsPath \ "address"

  implicit val ppobReader: Reads[PPOB] = for {
    address <- addressPath.readOpt[PPOBAddress]
  } yield PPOB(address)

  implicit val ppobWriter = new Writes[PPOB] {
    def writes(ppob: PPOB): JsValue = {
      Json.obj(
        "address" -> ppob.address
      )
    }
  }

  implicit val ppobFormat: Format[PPOB] = Format[PPOB](
    ppobReader,
    ppobWriter
  )
}


case class PPOBAddress(line1: Option[String], line2: Option[String], line3: Option[String], line4: Option[String],
                       line5: Option[String], postCode: Option[String], countryCode: Option[String])

object PPOBAddress extends JsonReadUtil {

  private val line1Path = JsPath \ "line1"
  private val line2Path =  JsPath \ "line2"
  private val line3Path = JsPath \ "line3"
  private val line4Path = JsPath \ "line4"
  private val line5Path = JsPath \ "line5"
  private val postCodePath = JsPath \ "postCode"
  private val countryCodePath = JsPath \ "countryCode"

  implicit val ppobAddressReader: Reads[PPOBAddress] = for {
    line1 <- line1Path.readOpt[String]
    line2 <- line2Path.readOpt[String]
    line3 <- line3Path.readOpt[String]
    line4 <- line4Path.readOpt[String]
    line5 <- line5Path.readOpt[String]
    postCode <- postCodePath.readOpt[String]
    countryCode <- countryCodePath.readOpt[String]
  } yield PPOBAddress(line1, line2, line3, line4, line5, postCode, countryCode)

  implicit val ppobAddressWriter: Writes[PPOBAddress] = (
    line1Path.writeNullable[String] and
      line2Path.writeNullable[String] and
      line3Path.writeNullable[String] and
      line4Path.writeNullable[String] and
      line5Path.writeNullable[String] and
      postCodePath.writeNullable[String] and
      countryCodePath.writeNullable[String]
    )(unlift(PPOBAddress.unapply))

  implicit val ppobAddressformat: Format[PPOBAddress] = Format[PPOBAddress](
    ppobAddressReader,
    ppobAddressWriter
  )
}

