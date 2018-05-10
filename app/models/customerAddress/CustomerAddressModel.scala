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

package models.customerAddress


import models.core.ErrorModel
import play.api.http.Status
import play.api.libs.json.{Format, Json, Reads, __}
import play.api.libs.functional.syntax._

case class CustomerAddressModel(line1: String,
                                line2: String,
                                line3: Option[String] = None,
                                line4: Option[String] = None,
                                postcode: Option[String] = None,
                                countryCode: Option[String] = None)

object CustomerAddressModel {

  def applyWithFields(address: List[String],
                      postcode: Option[String],
                      countryCode: Option[String]): Either[ErrorModel,CustomerAddressModel] = {

    address match {
      case List(l1,l2) =>
        Right(CustomerAddressModel(l1,l2,None,None,postcode,countryCode))
      case List(l1,l2,l3) =>
        Right(CustomerAddressModel(l1,l2,Some(l3),None,postcode,countryCode))
      case List(l1,l2,l3,l4) =>
        Right(CustomerAddressModel(l1,l2,Some(l3),Some(l4),postcode,countryCode))
      case _ => Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Invalid Json returned from Address Lookup"))
    }

  }
  val customerAddressReads: Reads[Either[ErrorModel,CustomerAddressModel]] = (
    (__ \\ "lines").read[List[String]] and
      (__ \\ "postcode").readNullable[String] and
      (__ \\ "code").readNullable[String]
    )(CustomerAddressModel.applyWithFields _)

  implicit val format: Format[CustomerAddressModel] = Json.format[CustomerAddressModel]
}

