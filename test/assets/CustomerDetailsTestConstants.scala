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

package assets

import models.circumstanceInfo.CustomerDetails
import play.api.libs.json.Json

object CustomerDetailsTestConstants {

  val orgName = "Ancient Antiques Ltd"
  val tradingName = "Dusty Relics"
  val firstName = "Fred"
  val lastName = "Flintstone"

  val individualJson = Json.obj(
    "firstName" -> firstName,
    "lastName" -> lastName,
    "hasFlatRateScheme" -> false
  )

  val organisationJson = Json.obj(
    "organisationName" -> orgName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false
  )

  val customerDetailsJsonMax = Json.obj(
    "organisationName" -> orgName,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false
  )


  val customerDetailsJsonMin = Json.obj(
    "hasFlatRateScheme" -> false
  )

  val individual = CustomerDetails(
      firstName = Some(firstName),
      lastName = Some(lastName),
      organisationName = None,
      tradingName = None
  )

  val organisation = CustomerDetails(
    firstName = None,
    lastName = None,
    organisationName = Some(orgName),
    tradingName = Some(tradingName)
  )

  val customerDetailsMax = CustomerDetails(
    Some(firstName),
    Some(lastName),
    Some(orgName),
    Some(tradingName)
  )

  val customerDetailsMin = CustomerDetails(
    None,
    None,
    None,
    None
  )



}
