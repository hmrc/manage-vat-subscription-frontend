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

package assets

import models.circumstanceInfo.{ContactDetails, PPOB, PPOBAddress}
import play.api.libs.json.{JsValue, Json}

object PPOBAddressTestConstants {

  val addLine1 = "Add Line 1"
  val addLine1Pending = "Changed Line 1"
  val addLine2 = "Add Line 2"
  val addLine3 = "Add Line 3"
  val addLine4 = "Add Line 4"
  val addLine5 = "Add Line 5"
  val postcode = "TE37 7AD"
  val countryCode = "GB"

  val website = "www.test.com"

  val phoneNumber = "01234 567890"
  val mobileNumber = "07700 123456"
  val faxNumber = "01234 098765"
  val email = "test@test.com"
  val emailVerified = true

  val phoneNumberPending = "01234 345234"
  val mobileNumberPending = "07700 345234"
  val faxNumberPending = "01234 123564"
  val emailPending = "pending@test.com"

  val ppobAddressModelMax = PPOBAddress(
    addLine1,
    Some(addLine2),
    Some(addLine3),
    Some(addLine4),
    Some(addLine5),
    Some(postcode),
    countryCode
  )

  val ppobAddressModelMaxPending = PPOBAddress(
    addLine1Pending,
    Some(addLine2),
    Some(addLine3),
    Some(addLine4),
    Some(addLine5),
    Some(postcode),
    countryCode
  )

  val ppobAddressModelMin = PPOBAddress(
    addLine1,
    None,
    None,
    None,
    None,
    None,
    countryCode
  )

  val contactDetailsModelMax = ContactDetails(
    Some(phoneNumber),
    Some(mobileNumber),
    Some(faxNumber),
    Some(email),
    Some(emailVerified)
  )

  val contactDetailsModelMaxPending = ContactDetails(
    Some(phoneNumberPending),
    Some(mobileNumberPending),
    Some(faxNumberPending),
    Some(emailPending),
    Some(emailVerified)
  )

  val contactDetailsModelMin = ContactDetails(None, None, None, None, None)

  val ppobModelMax = PPOB(ppobAddressModelMax,Some(contactDetailsModelMax), Some(website))

  val ppobModelMaxPending = PPOB(ppobAddressModelMaxPending,Some(contactDetailsModelMaxPending), Some(website))

  val ppobModelMin = PPOB(ppobAddressModelMin,None,None)

  val ppobJsonMax: JsValue = Json.obj(
    "address" -> Json.obj(
      "line1" -> addLine1,
      "line2" -> addLine2,
      "line3" -> addLine3,
      "line4" -> addLine4,
      "line5" -> addLine5,
      "postCode" -> postcode,
      "countryCode" -> countryCode
    ),
    "contactDetails" -> Json.obj(
      "primaryPhoneNumber" -> phoneNumber,
      "mobileNumber" -> mobileNumber,
      "faxNumber" -> faxNumber,
      "emailAddress" -> email,
      "emailVerified" -> emailVerified
    ),
    "websiteAddress" -> website
  )

  val ppobJsonMin: JsValue = Json.obj(
    "address" -> Json.obj(
      "line1" -> addLine1,
      "countryCode" -> countryCode
    )
  )

}
