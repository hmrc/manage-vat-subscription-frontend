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

package models.customerAddress


import models.JsonReadUtil
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, _}

case class AddressModel(line1: String,
                        line2: String,
                        line3: Option[String],
                        line4: Option[String],
                        postcode: Option[String],
                        countryCode: String)

object AddressModel extends JsonReadUtil {

  val customerAddressReads: Reads[AddressModel] = (
    (__ \\ "lines")(0).read[String] and
    (__ \\ "lines")(1).read[String] and
    (__ \\ "lines")(2).readNullable[String] and
    (__ \\ "lines")(3).readNullable[String] and
    (__ \\ "postcode").readNullable[String] and
    (__ \\ "code").read[String]
  )(AddressModel.apply _)

  implicit val format: Format[AddressModel] = Json.format[AddressModel]

  private val line1Path = JsPath \ "line1"
  private val line2Path =  JsPath \ "line2"
  private val line3Path = JsPath \ "line3"
  private val line4Path = JsPath \ "line4"
  private val postCodePath = JsPath \ "postCode"
  private val countryCodePath = JsPath \ "countryCode"

  val auditWrites: Writes[AddressModel] = (
    line1Path.write[String] and
      line2Path.write[String] and
      line3Path.writeNullable[String] and
      line4Path.writeNullable[String] and
      postCodePath.writeNullable[String] and
      countryCodePath.write[String]
  )(unlift(AddressModel.unapply))

}

