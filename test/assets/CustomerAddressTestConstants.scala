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

import models.customerAddress.{AddressLookupJsonBuilder, AddressModel}
import play.api.libs.json.Json


object CustomerAddressTestConstants {

  val addressLine1 = "line 1"
  val addressLine2 = "line 2"
  val addressLine3 = "line 3"
  val addressLine4 = "line 4"
  val postcode = "aa1 1aa"
  val countryName = "United Kingdom"
  val countryCode = "UK"

  val customerAddressMax = AddressModel(
    addressLine1,
    addressLine2,
    Some(addressLine3),
    Some(addressLine4),
    Some(postcode),
    Some(countryCode)
  )

  val customerAddressSome = AddressModel(
    addressLine1,
    addressLine2,
    Some(addressLine3),
    None,
    Some(postcode),
    Some(countryCode)
  )


  val customerAddressMin = AddressModel(
    addressLine1,
    addressLine2,
    None, None, None, None
  )

  val customerAddressToJsonMax = Json.obj(
    "line1" -> addressLine1,
    "line2" -> addressLine2,
    "line3" -> addressLine3,
    "line4" -> addressLine4,
    "postcode" -> postcode,
    "countryCode" -> countryCode
  )

  val customerAddressToJsonMin = Json.obj(
    "line1" -> addressLine1,
    "line2" -> addressLine2
  )

  val customerAddressJsonMax = Json.obj(
    "address" -> Json.obj(
      "lines" -> Json.arr(addressLine1, addressLine2, addressLine3, addressLine4),
      "postcode" -> postcode,
      "country" -> Json.obj(
        "name" -> countryName,
        "code" -> countryCode
      )
    )
  )

  val customerAddressJsonSome = Json.obj(
    "address" -> Json.obj(
      "lines" -> Json.arr(addressLine1, addressLine2, addressLine3),
      "postcode" -> postcode,
      "country" -> Json.obj(
        "name" -> countryName,
        "code" -> countryCode
      )
    )
  )

  val customerAddressJsonMin = Json.obj(
    "address" -> Json.obj(
      "lines" -> Json.arr(addressLine1, addressLine2)
    )
  )

  val customerAddressJsonError = Json.obj(
    "address" -> Json.obj(
      "lines" -> Json.arr(addressLine1)
    )
  )

  val addressLookupBuilder = AddressLookupJsonBuilder("/lookup-address/confirmed")

  val addressLookupJson = Json.obj(fields =
    "continueUrl" -> "/lookup-address/confirmed",
    "navTitle" -> "Business tax account",
    "ukMode" -> true,
    "showPhaseBanner" -> true,
    "lookupPage" -> Json.obj(
      "title" -> "Changes in circumstances",
      "heading" -> "What is the new business address?",
      "filterLabel" -> "Property name or number",
      "postcodeLabel" -> "Postcode"
    ),
    "selectPage" -> Json.obj(
      "submitLabel" -> "Save and continue"
    ),
    "confirmPage" -> Json.obj(
      "heading" -> "Confirm the new business address"
    )
  )

}
