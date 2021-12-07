/*
 * Copyright 2021 HM Revenue & Customs
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

package models.customerAddress

import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}

case class AddressModel(line1: Option[String],
                        line2: Option[String],
                        line3: Option[String],
                        line4: Option[String],
                        postcode: Option[String],
                        countryCode: Option[String])

object AddressModel {

  val customerAddressReads: Reads[AddressModel] = for {
    lines <- (__ \\ "lines").readNullable[Seq[String]]
    postcode <- (__ \\ "postcode").readNullable[String]
    countryCode <- (__ \\ "code").readNullable[String]
  } yield {
    lines match {
      case Some(someSequence) => AddressModel(
        extractValue(someSequence, 0),
        extractValue(someSequence, 1),
        extractValue(someSequence, 2),
        extractValue(someSequence, 3),
        postcode, countryCode)
      case None => AddressModel(None, None, None, None, postcode, countryCode)
    }
  }

  def extractValue(input: Seq[String], index: Int): Option[String] = {
    if(input.size > index) Some(input(index)) else None
  }

  implicit val format: Format[AddressModel] = Json.format[AddressModel]

  private val line1Path = JsPath \ "line1"
  private val line2Path =  JsPath \ "line2"
  private val line3Path = JsPath \ "line3"
  private val line4Path = JsPath \ "line4"
  private val postCodePath = JsPath \ "postCode"
  private val countryCodePath = JsPath \ "countryCode"

  val auditWrites: Writes[AddressModel] = (
    line1Path.writeNullable[String] and
      line2Path.writeNullable[String] and
      line3Path.writeNullable[String] and
      line4Path.writeNullable[String] and
      postCodePath.writeNullable[String] and
      countryCodePath.writeNullable[String]
  )(unlift(AddressModel.unapply))

}

