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

package helpers

import java.util.UUID

import models.circumstanceInfo._
import models.returnFrequency.Jan

object IntegrationTestConstants {
  val sessionId = s"stubbed-${UUID.randomUUID}"
  val userId = s"/auth/oid/1234567890"

  val partyType = "2"
  val clientVRN = "999999999"
  val VRN = "111111111"

  val website = "www.test.com"

  val phoneNumber = "01234 567890"
  val mobileNumber = "07700 123456"
  val faxNumber = "01234 098765"
  val email = "test@test.com"
  val emailVerified = true

  val organisation = CustomerDetails(
    firstName = None,
    lastName = None,
    tradingName = Some("Vatmobile Taxi"),
    organisationName = Some("Vatmobile Taxi LTD")
  )

  val individual = CustomerDetails(
    firstName = Some("Nanana"),
    lastName = Some("Vatman"),
    tradingName = Some("Vatmobile Taxi"),
    organisationName = None
  )

  val contactDetailsModelMax = ContactDetails(
    Some(phoneNumber),
    Some(mobileNumber),
    Some(faxNumber),
    Some(email),
    Some(emailVerified)
  )

  val ppob = PPOB(
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

  val bankDetails = BankDetails(
    Some("***************"),
    Some("****3421"),
    Some("12****")
  )

  val changeIndicators = ChangeIndicators(
    ppob = true,
    bankDetails = true,
    returnPeriod = true,
    deregister = true
  )

  val deregModel = Deregistration(
    Some("just coz"),
    Some("2018-10-02"),
    Some("2018-10-01")
  )

  def customerCircumstancesDetailsMax(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = customerType,
    flatRateScheme = None,
    ppob = ppob,
    bankDetails = Some(bankDetails),
    returnPeriod = Some(Jan),
    deregistration = Some(deregModel),
    changeIndicators = Some(changeIndicators),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppob),
      bankDetails = Some(bankDetails),
      returnPeriod = Some(Jan)
    )),
    partyType = Some(partyType)
  )

  def customerCircumstancesDetailsMin(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = customerType,
    flatRateScheme = None,
    ppob = ppob,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None
  )

  def customerCircumstancesDetailsWithPartyType(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = customerType,
    flatRateScheme = None,
    ppob = ppob,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some("2")
  )
}
