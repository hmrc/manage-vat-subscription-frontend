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

package models.circumstanceInfo

import models.JsonReadUtil
import play.api.libs.json._
import utils.JsonObjectSugar

case class CustomerDetails(firstName: Option[String],
                           lastName: Option[String],
                           organisationName: Option[String],
                           tradingName: Option[String],
                           welshIndicator: Option[Boolean],
                           hasFlatRateScheme: Boolean = false,
                           overseasIndicator: Boolean,
                           nameIsReadOnly: Option[Boolean],
                           isInsolvent: Boolean,
                           continueToTrade: Option[Boolean]) {

  val isOrg: Boolean = organisationName.isDefined
  val isInd: Boolean = firstName.isDefined || lastName.isDefined
  val userName: Option[String] = {
    val name = s"${firstName.getOrElse("")} ${lastName.getOrElse("")}".trim
    if (name.isEmpty) None else Some(name)
  }
  val businessName: Option[String] = if (isOrg) organisationName else userName
  val clientName: Option[String] = if (tradingName.isDefined) tradingName else businessName

  val isInsolventWithoutAccess: Boolean = continueToTrade match {
    case Some(false) => isInsolvent
    case _ => false
  }
}

object CustomerDetails extends JsonReadUtil with JsonObjectSugar {

  private val firstNamePath = __ \ "firstName"
  private val lastNamePath = __ \ "lastName"
  private val organisationNamePath = __ \ "organisationName"
  private val tradingNamePath = __ \ "tradingName"
  private val hasFrsPath = __ \ "hasFlatRateScheme"
  private val welshIndicatorPath = __ \ "welshIndicator"
  private val overseasIndicatorPath = __ \ "overseasIndicator"
  private val nameIsReadOnlyPath = __ \ "nameIsReadOnly"
  private val isInsolventPath = __ \ "isInsolvent"
  private val continueToTradePath = __ \ "continueToTrade"

  implicit val reads: Boolean => Reads[CustomerDetails] = isRelease10 =>
    if(isRelease10) {
      for {
        firstName <- firstNamePath.readOpt[String]
        lastname <- lastNamePath.readOpt[String]
        orgName <- organisationNamePath.readOpt[String]
        tradingName <- tradingNamePath.readOpt[String]
        welshIndicator <- welshIndicatorPath.readOpt[Boolean]
        hasFlatRateScheme <- hasFrsPath.read[Boolean]
        overseasIndicator <- overseasIndicatorPath.read[Boolean]
        nameIsReadOnly <- nameIsReadOnlyPath.readNullable[Boolean]
        isInsolvent <- isInsolventPath.read[Boolean]
        continueToTrade <- continueToTradePath.readNullable[Boolean]
      } yield CustomerDetails(
        firstName,
        lastname,
        orgName,
        tradingName,
        welshIndicator,
        hasFlatRateScheme,
        overseasIndicator,
        nameIsReadOnly,
        isInsolvent,
        continueToTrade
      )
    } else {
      for {
        firstName <- firstNamePath.readOpt[String]
        lastname <- lastNamePath.readOpt[String]
        orgName <- organisationNamePath.readOpt[String]
        tradingName <- tradingNamePath.readOpt[String]
        welshIndicator <- welshIndicatorPath.readOpt[Boolean]
        hasFlatRateScheme <- hasFrsPath.read[Boolean]
        nameIsReadOnly <- nameIsReadOnlyPath.readNullable[Boolean]
        isInsolvent <- isInsolventPath.read[Boolean]
        continueToTrade <- continueToTradePath.readNullable[Boolean]
      } yield CustomerDetails(
        firstName,
        lastname,
        orgName,
        tradingName,
        welshIndicator,
        hasFlatRateScheme,
        overseasIndicator = false,
        nameIsReadOnly,
        isInsolvent,
        continueToTrade
      )
    }

  implicit val writes: Boolean => Writes[CustomerDetails] = isRelease10 => Writes {
    model =>
      jsonObjNoNulls(
        "firstName" -> model.firstName,
        "lastName" -> model.lastName,
        "organisationName" -> model.organisationName,
        "tradingName" -> model.tradingName,
        "welshIndicator" -> model.welshIndicator,
        "hasFlatRateScheme" -> model.hasFlatRateScheme,
        "nameIsReadOnly" -> model.nameIsReadOnly,
        "isInsolvent" -> model.isInsolvent,
        "continueToTrade" -> model.continueToTrade
      ) ++ (if(isRelease10) Json.obj("overseasIndicator" -> model.overseasIndicator) else Json.obj())
  }
}
