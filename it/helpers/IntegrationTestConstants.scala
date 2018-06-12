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

  val clientVRN = "999999999"
  val VRN = "111111111"

  val individual = CustomerDetails(
    firstName = None,
    lastName = None,
    tradingName = Some("Vatmobile Taxi"),
    organisationName = Some("Vatmobile Taxi LTD")
  )

  val organisation = CustomerDetails(
    firstName = None,
    lastName = None,
    tradingName = Some("Vatmobile Taxi"),
    organisationName = None
  )

  val ppob = PPOB(
    Some(PPOBAddress(
      Some("Add Line 1"),
      Some("Add Line 2"),
      Some("Add Line 3"),
      Some("Add Line 4"),
      Some("Add Line 5"),
      Some("TE3 3ST"),
      Some("GB")
    ))
  )

  val bankDetails = BankDetails(
    Some("***************"),
    Some("****3421"),
    Some("12****")
  )

  def customerCircumstancesDetailsMax(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated,
    customerType,
    None,
    Some(ppob),
    Some(bankDetails),
    Some(Jan)
  )

  def customerCircumstancesDetailsMin(customerType: CustomerDetails): CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated,
    customerType,
    None,
    None,
    None,
    None
  )
}
