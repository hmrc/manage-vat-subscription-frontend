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

package assets

import models.circumstanceInfo.CustomerDetails
import play.api.libs.json.{JsObject, Json}

object CustomerDetailsTestConstants {

  val orgName = "Ancient Antiques Ltd"
  val tradingName = "Dusty Relics"
  val firstName = "Fred"
  val lastName = "Flintstone"

  val individualJson: JsObject = Json.obj(
    "firstName" -> firstName,
    "lastName" -> lastName,
    "hasFlatRateScheme" -> false,
    "overseasIndicator" -> false,
    "nameIsReadOnly" -> false,
    "isInsolvent" -> false
  )

  val organisationJson: JsObject = Json.obj(
    "organisationName" -> orgName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false,
    "overseasIndicator" -> false,
    "nameIsReadOnly" -> false,
    "isInsolvent" -> false
  )

  val customerDetailsJsonMax: JsObject = Json.obj(
    "organisationName" -> orgName,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false,
    "welshIndicator" -> false,
    "overseasIndicator" -> false,
    "nameIsReadOnly" -> false,
    "isInsolvent" -> false,
    "continueToTrade" -> Some(true)
  )

  val customerDetailsJsonMin: JsObject = Json.obj(
    "hasFlatRateScheme" -> false,
    "overseasIndicator" -> false,
    "isInsolvent" -> false
  )

  val individual: CustomerDetails = CustomerDetails(
    firstName = Some(firstName),
    lastName = Some(lastName),
    organisationName = None,
    tradingName = None,
    welshIndicator = None,
    overseasIndicator = false,
    nameIsReadOnly = Some(false),
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

  val organisation: CustomerDetails = CustomerDetails(
    firstName = None,
    lastName = None,
    organisationName = Some(orgName),
    tradingName = Some(tradingName),
    welshIndicator = None,
    overseasIndicator = false,
    nameIsReadOnly = Some(false),
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

  val overseasOrganisation: CustomerDetails = organisation.copy(overseasIndicator = true)

  val customerDetailsMax: CustomerDetails = CustomerDetails(
    Some(firstName),
    Some(lastName),
    Some(orgName),
    Some(tradingName),
    Some(false),
    overseasIndicator = false,
    nameIsReadOnly = Some(false),
    isInsolvent = false,
    continueToTrade = Some(true),
    insolvencyType = None
  )

  val customerDetailsInsolvent: CustomerDetails = customerDetailsMax.copy(isInsolvent = true, continueToTrade = Some(false))

  val customerDetailsMin: CustomerDetails = CustomerDetails(
    None,
    None,
    None,
    None,
    None,
    overseasIndicator = false,
    nameIsReadOnly = None,
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

  ///////////////// Release 8 data -- separated for ease of removal

  val organisationR8: CustomerDetails = CustomerDetails(
    firstName = None,
    lastName = None,
    organisationName = Some(orgName),
    tradingName = Some(tradingName),
    welshIndicator = None,
    overseasIndicator = false,
    nameIsReadOnly = Some(false),
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

  val organisationJsonR8: JsObject = Json.obj(
    "organisationName" -> orgName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false,
    "isInsolvent" -> false
  )

  val customerDetailsJsonMinR8: JsObject = Json.obj(
    "hasFlatRateScheme" -> false,
    "isInsolvent" -> false
  )

  val customerDetailsMaxR8: CustomerDetails = CustomerDetails(
    Some(firstName),
    Some(lastName),
    Some(orgName),
    Some(tradingName),
    Some(false),
    overseasIndicator = false,
    nameIsReadOnly = Some(false),
    isInsolvent = false,
    continueToTrade = Some(true),
    insolvencyType = None
  )

  val customerDetailsMinR8: CustomerDetails = CustomerDetails(
    None,
    None,
    None,
    None,
    None,
    overseasIndicator = false,
    nameIsReadOnly = None,
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

  val customerDetailsJsonMaxR8: JsObject = Json.obj(
    "organisationName" -> orgName,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false,
    "welshIndicator" -> false,
    "nameIsReadOnly" -> false,
    "isInsolvent" -> false,
    "continueToTrade" -> Some(true)
  )

  val customerDetailsJsonMinWithTrueOverseas: JsObject = Json.obj(
    "hasFlatRateScheme" -> false,
    "overseasIndicator" -> true,
    "isInsolvent" -> false
  )
}
