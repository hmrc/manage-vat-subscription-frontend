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

import play.api.libs.functional.syntax._
import play.api.libs.json.{__, Reads, Writes}

case class ContactDetails(phoneNumber: Option[String],
                          mobileNumber: Option[String],
                          faxNumber: Option[String],
                          emailAddress: Option[String],
                          emailVerified: Option[Boolean])

object ContactDetails {

  private val phoneNumberPath = __ \ "primaryPhoneNumber"
  private val mobilePath =  __ \ "mobileNumber"
  private val faxNumberPath = __ \ "faxNumber"
  private val emailAddressPath = __ \ "emailAddress"
  private val emailVerifiedPath = __ \ "emailVerified"

  implicit val reads: Reads[ContactDetails] = (
    phoneNumberPath.readNullable[String] and
      mobilePath.readNullable[String] and
      faxNumberPath.readNullable[String] and
      emailAddressPath.readNullable[String] and
      emailVerifiedPath.readNullable[Boolean]
    )(ContactDetails.apply _)

  implicit val writes: Writes[ContactDetails] = (
    phoneNumberPath.writeNullable[String] and
      mobilePath.writeNullable[String] and
      faxNumberPath.writeNullable[String] and
      emailAddressPath.writeNullable[String] and
      emailVerifiedPath.writeNullable[Boolean]
    )(unlift(ContactDetails.unapply))
}
