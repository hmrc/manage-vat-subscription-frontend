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

import models.JsonReadUtil
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CustomerDetails(firstName: Option[String],
                           lastName: Option[String],
                           organisationName: Option[String],
                           tradingName: Option[String],
                           hasFlatRateScheme: Boolean = false) {

  val isOrg: Boolean = organisationName.isDefined
  val isInd: Boolean = firstName.isDefined || lastName.isDefined
  val userName: Option[String] = {
    val name = s"${firstName.getOrElse("")} ${lastName.getOrElse("")}".trim
    if (name.isEmpty) None else Some(name)
  }
  val businessName: Option[String] = if(isOrg) organisationName else userName
  val clientName: Option[String] = if(tradingName.isDefined) tradingName else businessName
}

object CustomerDetails extends JsonReadUtil {

  private val firstNamePath = __ \ "firstName"
  private val lastNamePath = __ \ "lastName"
  private val organisationNamePath = __ \ "organisationName"
  private val tradingNamePath = __ \ "tradingName"
  private val hasFrsPath = __ \ "hasFlatRateScheme"

  implicit val reads: Reads[CustomerDetails] = (
    firstNamePath.readOpt[String] and
      lastNamePath.readOpt[String] and
      organisationNamePath.readOpt[String] and
      tradingNamePath.readOpt[String] and
      hasFrsPath.read[Boolean]
    ) (CustomerDetails.apply _)

  implicit val writes: Writes[CustomerDetails] = (
    firstNamePath.writeNullable[String] and
      lastNamePath.writeNullable[String] and
      organisationNamePath.writeNullable[String] and
      tradingNamePath.writeNullable[String] and
      hasFrsPath.write[Boolean]
    ) (unlift(CustomerDetails.unapply))

}
