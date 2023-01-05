/*
 * Copyright 2023 HM Revenue & Customs
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
                           continueToTrade: Option[Boolean],
                           insolvencyType: Option[String]) {

  val isOrg: Boolean = organisationName.isDefined
  val isInd: Boolean = firstName.isDefined || lastName.isDefined
  val userName: Option[String] = {
    val name = s"${firstName.getOrElse("")} ${lastName.getOrElse("")}".trim
    if (name.isEmpty) None else Some(name)
  }
  val businessName: Option[String] = if (isOrg) organisationName else userName
  val clientName: Option[String] = if (tradingName.isDefined) tradingName else businessName

  val allowedInsolvencyTypes: Seq[String] = Seq("07", "12", "13", "14")
  val blockedInsolvencyTypes: Seq[String] = Seq("08", "09", "10", "15")

  val isInsolventWithoutAccess: Boolean = {
    if(isInsolvent) {
      insolvencyType match {
        case Some(iType) if allowedInsolvencyTypes.contains(iType) => false
        case Some(iType) if blockedInsolvencyTypes.contains(iType) => true
        case _ => !continueToTrade.getOrElse(true)
      }
    } else {
      false
    }
  }
}

object CustomerDetails extends JsonObjectSugar {

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
  private val insolvencyTypePath = __ \ "insolvencyType"

  implicit val reads: Reads[CustomerDetails] = {
      for {
        firstName <- firstNamePath.readNullable[String]
        lastname <- lastNamePath.readNullable[String]
        orgName <- organisationNamePath.readNullable[String]
        tradingName <- tradingNamePath.readNullable[String]
        welshIndicator <- welshIndicatorPath.readNullable[Boolean]
        hasFlatRateScheme <- hasFrsPath.read[Boolean]
        overseasIndicator <- overseasIndicatorPath.read[Boolean]
        nameIsReadOnly <- nameIsReadOnlyPath.readNullable[Boolean]
        isInsolvent <- isInsolventPath.read[Boolean]
        continueToTrade <- continueToTradePath.readNullable[Boolean]
        insolvencyType <- insolvencyTypePath.readNullable[String]
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
        continueToTrade,
        insolvencyType
      )
    }

  implicit val writes: Writes[CustomerDetails] = Writes {
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
        "continueToTrade" -> model.continueToTrade,
        "insolvencyType" -> model.insolvencyType,
        "overseasIndicator" -> model.overseasIndicator
      )
  }
}
