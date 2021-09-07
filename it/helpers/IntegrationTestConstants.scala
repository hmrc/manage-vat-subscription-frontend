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

package helpers

import java.util.UUID

import models.circumstanceInfo._
import models.returnFrequency.Jan

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

  def customerCircumstancesDetailsMax(customerType: CustomerDetails,
                                      partyType: Option[String] = None): CircumstanceDetails = CircumstanceDetails(
    customerDetails = customerType,
    flatRateScheme = None,
    ppob = ppobMax,
    bankDetails = Some(bankDetails),
    returnPeriod = Some(Jan),
    deregistration = Some(deregModel),
    missingTrader = true,
    changeIndicators = Some(changeIndicators),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobMax),
      bankDetails = Some(bankDetails),
      returnPeriod = Some(Jan),
      tradingName = Some("Pens'n'Dinghy's"),
      businessName = Some("Stationery'n'Boats")
    )),
    partyType = partyType,
    commsPreference = Some("DIGITAL")
  )

  def customerCircumstancesDetailsMin(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    customerDetails = customerType,
    flatRateScheme = None,
    ppob = ppobMin,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None,
    commsPreference = None
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
