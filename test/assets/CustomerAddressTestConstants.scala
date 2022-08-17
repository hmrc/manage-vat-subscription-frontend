/*
 * Copyright 2022 HM Revenue & Customs
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

import assets.messages.{AddressLookupMessages, BaseMessages}
import models.customerAddress.AddressModel
import play.api.libs.json.{JsObject, Json}


object CustomerAddressTestConstants extends BaseMessages {

  val addressLine1 = "line 1"
  val veryLongAddressLine1 = "liiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiine 1"
  val foreignCharsLine1 = "1 Falsche Straße"
  val addressLine2 = "line 2"
  val addressLine3 = "line 3"
  val addressLine4 = "line 4"
  val postcode = "aa1 1aa"
  val countryName = "United Kingdom"
  val countryCode = "UK"

  val phaseBannerHtml = "This is a new service – your <a id='beta-banner-feedback' href='/feedback'>feedback</a> will help us to improve it."
  val phaseBannerHtmlCy = "Gwasanaeth newydd yw hwn – bydd eich <a id='beta-banner-feedback' href='/feedback'>adborth</a> yn ein helpu i’w wella."

  val customerAddressMax: AddressModel = AddressModel(
    Some(addressLine1),
    Some(addressLine2),
    Some(addressLine3),
    Some(addressLine4),
    Some(postcode),
    Some(countryCode)
  )

  val customerAddressSome: AddressModel = AddressModel(
    Some(addressLine1),
    Some(addressLine2),
    Some(addressLine3),
    None,
    Some(postcode),
    Some(countryCode)
  )

  val customerAddressLong: AddressModel = customerAddressMax.copy(line1 = Some(veryLongAddressLine1))
  val customerAddressZero: AddressModel = customerAddressMax.copy(line1 = Some(""))
  val customerAddressForeignChars: AddressModel = customerAddressMax.copy(line1 = Some(foreignCharsLine1))

  val customerAddressMin: AddressModel = AddressModel(None, None, None, None, None, None)

  val customerAddressToJsonMax: JsObject = Json.obj(
    "line1" -> addressLine1,
    "line2" -> addressLine2,
    "line3" -> addressLine3,
    "line4" -> addressLine4,
    "postcode" -> postcode,
    "countryCode" -> countryCode
  )

  val customerAddressToJsonMin: JsObject = Json.obj()

  val customerAddressJsonMax: JsObject = Json.obj(
    "address" -> Json.obj(
      "lines" -> Json.arr(addressLine1, addressLine2, addressLine3, addressLine4),
      "postcode" -> postcode,
      "country" -> Json.obj(
        "name" -> countryName,
        "code" -> countryCode
      )
    )
  )

  val customerAddressJsonSome: JsObject = Json.obj(
    "address" -> Json.obj(
      "lines" -> Json.arr(addressLine1, addressLine2, addressLine3),
      "postcode" -> postcode,
      "country" -> Json.obj(
        "name" -> countryName,
        "code" -> countryCode
      )
    )
  )

  val customerAddressJsonMin: JsObject = Json.obj()

  val customerAddressJsonError: JsObject = Json.obj(
    "address" -> Json.obj(
      "lines" -> 4
    )
  )

  val clientAddressLookupV2Json: JsObject = Json.obj(fields =
    "version" -> 2,
    "options" -> Json.obj(
      "continueUrl" -> "/lookup-address/confirmed",
      "accessibilityFooterUrl" -> "/vat-through-software/accessibility-statement",
      "deskProServiceName" -> "VATC",
      "showPhaseBanner" -> true,
      "ukMode" -> false,
      "timeoutConfig" -> Json.obj(
        "timeoutAmount" -> 1800,
        "timeoutUrl" -> "/unauth-signout"
      )
    ),
    "labels" -> Json.obj(
      "en" -> Json.obj(
        "appLevelLabels" -> Json.obj(
          "navTitle" -> clientServiceName,
          "phaseBannerHtml" -> phaseBannerHtml
        ),
        "selectPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.selectHeading,
          "heading" -> AddressLookupMessages.selectHeading,
          "submitLabel" -> continue,
          "editAddressLinkText" -> AddressLookupMessages.editAddressLinkText
        ),
        "lookupPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.startHeading,
          "heading" -> AddressLookupMessages.startHeading,
          "filterLabel" -> AddressLookupMessages.filter,
          "postcodeLabel" -> AddressLookupMessages.postcode,
          "submitLabel" -> AddressLookupMessages.submitLabel,
          "manualAddressLinkText" -> AddressLookupMessages.manualAddressLink
        ),
        "confirmPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.confirmHeading,
          "heading" -> AddressLookupMessages.confirmHeading,
          "showConfirmChangeText" -> false
        ),
        "editPageLabels" -> Json.obj(
          "submitLabel" -> continue,
          "postcodeLabel" -> AddressLookupMessages.postcodeLabel
        )
      ),
      "cy" -> Json.obj(
        "appLevelLabels" -> Json.obj(
          "navTitle" -> AddressLookupMessages.clientServiceNameCy,
          "phaseBannerHtml" -> phaseBannerHtmlCy
        ),
        "selectPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.selectHeadingCy,
          "heading" -> AddressLookupMessages.selectHeadingCy,
          "submitLabel" -> AddressLookupMessages.continueCy,
          "editAddressLinkText" -> AddressLookupMessages.editAddressLinkTextCy
        ),
        "lookupPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.startHeadingCy,
          "heading" -> AddressLookupMessages.startHeadingCy,
          "filterLabel" -> AddressLookupMessages.filterCy,
          "postcodeLabel" -> AddressLookupMessages.postcodeCy,
         "submitLabel" -> AddressLookupMessages.submitLabelCy,
          "manualAddressLinkText" -> AddressLookupMessages.manualAddressLinkCy
        ),
        "confirmPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.confirmHeadingCy,
          "heading" -> AddressLookupMessages.confirmHeadingCy,
          "showConfirmChangeText" -> false
        ),
        "editPageLabels" -> Json.obj(
          "submitLabel" -> AddressLookupMessages.continueCy,
          "postcodeLabel" -> AddressLookupMessages.postcodeLabelCy
        )
      )
    )
  )

  val agentAddressLookupV2Json: JsObject = Json.obj(fields =
    "version" -> 2,
    "options" -> Json.obj(
      "continueUrl" -> "/lookup-address/confirmed",
      "accessibilityFooterUrl" -> "/vat-through-software/accessibility-statement",
      "deskProServiceName" -> "VATC",
      "showPhaseBanner" -> true,
      "ukMode" -> false,
      "timeoutConfig" -> Json.obj(
        "timeoutAmount" -> 1800,
        "timeoutUrl" -> "/unauth-signout"
      )
    ),
    "labels" -> Json.obj(
      "en" -> Json.obj(
        "appLevelLabels" -> Json.obj(
          "navTitle" -> agentServiceName,
          "phaseBannerHtml" -> phaseBannerHtml
        ),
        "selectPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.selectHeading,
          "heading" -> AddressLookupMessages.selectHeading,
          "submitLabel" -> continue,
          "editAddressLinkText" -> AddressLookupMessages.editAddressLinkText
        ),
        "lookupPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.startHeading,
          "heading" -> AddressLookupMessages.startHeading,
          "filterLabel" -> AddressLookupMessages.filter,
          "postcodeLabel" -> AddressLookupMessages.postcode,
          "submitLabel" -> AddressLookupMessages.submitLabel,
          "manualAddressLinkText" -> AddressLookupMessages.manualAddressLink
        ),
        "confirmPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.confirmHeading,
          "heading" -> AddressLookupMessages.confirmHeading,
          "showConfirmChangeText" -> false
        ),
        "editPageLabels" -> Json.obj(
          "submitLabel" -> continue,
          "postcodeLabel" -> AddressLookupMessages.postcodeLabel
        )
      ),
      "cy" -> Json.obj(
        "appLevelLabels" -> Json.obj(
          "navTitle" -> AddressLookupMessages.agentServiceNameCy,
          "phaseBannerHtml" -> phaseBannerHtmlCy
        ),
        "selectPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.selectHeadingCy,
          "heading" -> AddressLookupMessages.selectHeadingCy,
          "submitLabel" -> AddressLookupMessages.continueCy,
          "editAddressLinkText" -> AddressLookupMessages.editAddressLinkTextCy
        ),
        "lookupPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.startHeadingCy,
          "heading" -> AddressLookupMessages.startHeadingCy,
          "filterLabel" -> AddressLookupMessages.filterCy,
          "postcodeLabel" -> AddressLookupMessages.postcodeCy,
          "submitLabel" -> AddressLookupMessages.submitLabelCy,
          "manualAddressLinkText" -> AddressLookupMessages.manualAddressLinkCy
        ),
        "confirmPageLabels" -> Json.obj(
          "title" -> AddressLookupMessages.confirmHeadingCy,
          "heading" -> AddressLookupMessages.confirmHeadingCy,
          "showConfirmChangeText" -> false
        ),
        "editPageLabels" -> Json.obj(
          "submitLabel" -> AddressLookupMessages.continueCy,
          "postcodeLabel" -> AddressLookupMessages.postcodeLabelCy
        )
      )
    )
  )

}
