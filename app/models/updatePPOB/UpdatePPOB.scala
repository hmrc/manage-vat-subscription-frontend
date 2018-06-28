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

import models.circumstanceInfo.ContactDetails
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class UpdatePPOB(address: UpdatePPOBAddress,
                      contactDetails: Option[ContactDetails],
                      websiteAddress: Option[String])

object UpdatePPOB {

  private val addressPath = __ \ "address"
  private val contactDetailsPath = __ \ "contactDetails"
  private val websiteAddressPath = __ \ "websiteAddress"

  implicit val writes: Writes[UpdatePPOB] = (
    addressPath.write[UpdatePPOBAddress] and
      contactDetailsPath.writeNullable[ContactDetails] and
      websiteAddressPath.writeNullable[String]
    )(unlift(UpdatePPOB.unapply))

}