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

import models.customerInfo.CustomerDetailsModel
import BaseTestConstants._
import play.api.libs.json.Json

object CustomerDetailsTestConstants {

  val individualJson = Json.obj(
    "firstName" -> firstName,
    "lastName" -> lastName
  )

  val organisationJson = Json.obj(
    "organisationName" -> organisationName,
    "tradingName" -> tradingName
  )

  val customerDetailsJsonMax = Json.obj(
    "organisationName" -> organisationName,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "tradingName" -> tradingName
  )

  val customerDetailsJsonMin = Json.obj()

  val individual = CustomerDetailsModel(
    firstName = Some(firstName),
    lastName = Some(lastName),
    organisationName = None,
    tradingName = None
  )

  val organisation = CustomerDetailsModel(
    firstName = None,
    lastName = None,
    organisationName = Some(organisationName),
    tradingName = Some(tradingName)
  )

  val customerDetailsMax = CustomerDetailsModel(
    Some(firstName),
    Some(lastName),
    Some(organisationName),
    Some(tradingName)
  )

  val customerDetailsMin = CustomerDetailsModel(
    None,
    None,
    None,
    None
  )



}
