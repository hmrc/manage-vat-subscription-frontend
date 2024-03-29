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

package helpers

import java.util.UUID
import models.circumstanceInfo._
import play.api.libs.json.{JsValue, Json}

object IntegrationTestConstants {
  val sessionId = s"stubbed-${UUID.randomUUID}"
  val userId = s"/auth/oid/1234567890"

  val partyType = "50"
  val clientVRN = "999999999"
  val VRN = "111111111"

  val website = "www.test.com"

  val phoneNumber = "01234 567890"
  val mobileNumber = "07700 123456"
  val faxNumber = "01234 098765"
  val email = "test@test.com"
  val emailVerified = true

  val organisation: CustomerDetails = CustomerDetails(
    firstName = None,
    lastName = None,
    tradingName = Some("Vatmobile Taxi"),
    organisationName = Some("Vatmobile Taxi LTD"),
    welshIndicator = None,
    overseasIndicator = false,
    nameIsReadOnly = Some(false),
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

  val individual: CustomerDetails = CustomerDetails(
    firstName = Some("Nanana"),
    lastName = Some("Vatman"),
    tradingName = Some("Vatmobile Taxi"),
    organisationName = None,
    welshIndicator = None,
    overseasIndicator = false,
    nameIsReadOnly = Some(true),
    isInsolvent = false,
    continueToTrade = None,
    insolvencyType = None
  )

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

  val contactDetailsModelMax: ContactDetails = ContactDetails(
    Some(phoneNumber),
    Some(mobileNumber),
    Some(faxNumber),
    Some(email),
    Some(emailVerified)
  )

  val contactDetailsModelMin: ContactDetails = ContactDetails(
    None,
    None,
    None,
    None,
    None
  )

  val contactDetailsUnverifiedEmail: ContactDetails = ContactDetails(
    Some(phoneNumber),
    Some(mobileNumber),
    Some(faxNumber),
    Some(email),
    Some(false)
  )

  val ppobMax: PPOB = PPOB(
    PPOBAddress(
      "Add Line 1",
      Some("Add Line 2"),
      Some("Add Line 3"),
      Some("Add Line 4"),
      Some("Add Line 5"),
      Some("TE3 3ST"),
      "GB"
    ),
    Some(contactDetailsModelMax),
    Some(website)
  )

  val ppobMin: PPOB = PPOB(
    PPOBAddress(
      "Add Line 1",
      None,
      None,
      None,
      None,
      None,
      countryCode = "GB"
    ),
    Some(contactDetailsModelMin),
    None
  )

  val bankDetails: BankDetails = BankDetails(
    Some("***************"),
    Some("****3421"),
    Some("12****")
  )

  val changeIndicators: ChangeIndicators = ChangeIndicators(
    ppob = true,
    bankDetails = true,
    returnPeriod = true,
    deregister = true
  )

  val deregModel: Deregistration = Deregistration(
    Some("just coz"),
    Some("2018-10-02"),
    Some("2018-10-01")
  )

  def pendingJson(isPending: Boolean): JsValue = if(isPending) {
    Json.obj(
      "PPOBDetails" -> ppobMax,
      "bankDetails" -> bankDetails,
      "returnPeriod" -> Json.obj(
        "stdReturnPeriod" -> "MA"
      ),
      "tradingName" -> "Pens'n'Dinghy's",
      "businessName" -> "Stationery'n'Boats"
    )
  } else {
    Json.obj()
  }

  def customerCircumstancesJsonMax(customerType: CustomerDetails,
                                   partyType: String = "1",
                                   isPending: Boolean = true): JsValue = Json.obj(
    "customerDetails" -> customerType,
    "ppob" -> ppobMax,
    "bankDetails" -> bankDetails,
    "returnPeriod" -> Json.obj(
      "stdReturnPeriod" -> "MA"
    ),
    "partyType" -> partyType,
    "deregistration" -> deregModel,
    "missingTrader" -> true,
    "commsPreference" -> "DIGITAL",
    "changeIndicators" -> Json.obj(
      "PPOBDetails" -> isPending,
      "bankDetails" -> isPending,
      "returnPeriod" -> isPending,
      "deregister" -> isPending
    ),
    "pendingChanges" -> pendingJson(isPending),
    "mandationStatus" -> "MTDfB"
  )

  val customerCircumstancesJsonMin: JsValue = Json.obj(
    "customerDetails" -> Json.obj(
      "tradingName" -> "Vatmobile Taxi",
      "organisationName" -> "Vatmobile Taxi LTD",
      "hasFlatRateScheme" -> false,
      "overseasIndicator" -> false,
      "isInsolvent" -> false
    ),
    "ppob" -> Json.obj(
      "address" -> Json.obj(
        "line1" -> "Add Line 1",
        "countryCode" -> "GB"
      )
    ),
    "missingTrader" -> false,
    "mandationStatus" -> "MTDfB"
  )

  val missingTraderJson: JsValue = Json.obj(
    "customerDetails" -> Json.obj(
      "hasFlatRateScheme" -> false,
      "overseasIndicator" -> false,
      "isInsolvent" -> false
    ),
    "ppob" -> Json.obj(
      "address" -> Json.obj(
        "line1" -> "Add Line 1",
        "countryCode" -> "GB"
      )
    ),
    "missingTrader" -> true,
    "mandationStatus" -> "MTDfB"
  )

  def customerCircumstancesDetailsWithPartyType(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    customerDetails = customerType,
    flatRateScheme = None,
    ppob = ppobMax,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some("7"),
    commsPreference = None
  )
}
