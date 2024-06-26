/*
 * Copyright 2024 HM Revenue & Customs
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

import assets.BaseTestConstants.agentEmail
import models.circumstanceInfo.ContactDetails
import models.updatePPOB.{UpdatePPOB, UpdatePPOBAddress}
import play.api.libs.json.{JsValue, Json}

object UpdatePPOBAddressTestConstants {

  val addLine1 = "Add Line 1"
  val addLine2 = "Add Line 2"
  val addLine3 = "Add Line 3"
  val addLine4 = "Add Line 4"
  val postcode = "TE37 7AD"
  val countryCode = "GB"

  val website = "www.test.com"

  val phoneNumber = "01234 567890"
  val mobileNumber = "07700 123456"
  val faxNumber = "01234 098765"
  val email = "test@test.com"
  val emailVerified = true


  val updatePPOBAddressModelMax = UpdatePPOBAddress(
    Some(addLine1),
    Some(addLine2),
    Some(addLine3),
    Some(addLine4),
    Some(postcode),
    Some(countryCode)
  )

  val updatePPOBAddressModelMin = UpdatePPOBAddress(
    None,
    None,
    None,
    None,
    None,
    None
  )

  val contactDetailsModelMax = ContactDetails(
    Some(phoneNumber),
    Some(mobileNumber),
    Some(faxNumber),
    Some(email),
    Some(emailVerified)
  )

  val contactDetailsModelMin = ContactDetails(None, None, None, None, None)

  val updatePPOBModelMax =
    UpdatePPOB(updatePPOBAddressModelMax, Some(contactDetailsModelMax), Some(website), Some(agentEmail))

  val updatePPOBModelMin = UpdatePPOB(updatePPOBAddressModelMin, None, None, None)

  val updatePPOBJsonMax: JsValue = Json.obj(
    "address" -> Json.obj(
      "line1" -> addLine1,
      "line2" -> addLine2,
      "line3" -> addLine3,
      "line4" -> addLine4,
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
    "websiteAddress" -> website,
    "transactorOrCapacitorEmail" -> agentEmail
  )

  val updatePPOBJsonMin: JsValue = Json.obj(
    "address" -> Json.obj()
  )
}
