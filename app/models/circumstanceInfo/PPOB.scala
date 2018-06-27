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

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PPOB(address: PPOBAddress,
                rlsIndicator: Option[String],
                contactDetails: Option[ContactDetails],
                websiteAddress: Option[String])

object PPOB {

  private val addressPath = __ \ "address"
  private val rlsIndicatorPath = __ \ "RLS"
  private val contactDetailsPath = __ \ "contactDetails"
  private val websiteAddressPath = __ \ "websiteAddress"

  implicit val reads: Reads[PPOB] = (
    addressPath.read[PPOBAddress] and
      rlsIndicatorPath.readNullable[String] and
      contactDetailsPath.readNullable[ContactDetails] and
      websiteAddressPath.readNullable[String]
    )(PPOB.apply _)

  implicit val writes: Writes[PPOB] = (
    addressPath.write[PPOBAddress] and
      rlsIndicatorPath.writeNullable[String] and
      contactDetailsPath.writeNullable[ContactDetails] and
      websiteAddressPath.writeNullable[String]
    )(unlift(PPOB.unapply))

}
