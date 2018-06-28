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

package models.updatePPOB

import models.JsonReadUtil
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class UpdatePPOBAddress(line1: String,
                             line2: String,
                             line3: Option[String],
                             line4: Option[String],
                             postCode: Option[String],
                             nonUkCountryCode: Option[String])

object UpdatePPOBAddress extends JsonReadUtil {

  private val line1Path = JsPath \ "line1"
  private val line2Path =  JsPath \ "line2"
  private val line3Path = JsPath \ "line3"
  private val line4Path = JsPath \ "line4"
  private val postCodePath = JsPath \ "postCode"
  private val nonUkCountryCodePath = JsPath \ "nonUKCountryCode"

  implicit val writes: Writes[UpdatePPOBAddress] = (
    line1Path.write[String] and
      line2Path.write[String] and
      line3Path.writeNullable[String] and
      line4Path.writeNullable[String] and
      postCodePath.writeNullable[String] and
      nonUkCountryCodePath.writeNullable[String]
    )(unlift(UpdatePPOBAddress.unapply))
}

