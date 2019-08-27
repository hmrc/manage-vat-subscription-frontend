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

import config.AppConfig
import play.api.libs.json.Json

object BusinessAddressITConstants {

  def alfPostBody: String =
    Json.stringify(Json.obj(
      "continueUrl" -> "http://localhost:9150/vat-through-software/account/change-business-address/callback?id=",
      "showPhaseBanner" -> true,
      "navTitle" -> "Update your client's VAT details",
      "ukMode" -> true,
      "lookupPage" -> Json.obj(
        "title" -> "What is the new principal place of business?",
        "heading" -> "What is the new principal place of business?",
        "filterLabel" -> "Property name or number",
        "postcodeLabel" -> "Postcode"
      ),
      "selectPage" -> Json.obj(
        "title" -> "Select the new address",
        "heading" -> "Select the new address",
        "editAddressLinkText" -> "Edit the address manually",
        "submitLabel" -> "Continue"
      ),
      "editPage" -> Json.obj("submitLabel" -> "Continue"),
      "confirmPage" -> Json.obj(
        "title" -> "Confirm the new principal place of business",
        "heading" -> "Confirm the new principal place of business",
        "showConfirmChangeText" -> false)
    ))


  val startHeading = "What is the new principal place of business?"
  val filter = "Property name or number"
  val postcode = "Postcode"
  val postcodeLink = "The address does not have a post code"
  val selectHeading = "Select the new address"
  val editAddressLinkText = "Edit the address manually"
  val confirmHeading = "Confirm the new principal place of business"
  val continue = "Continue"

  val startHeadingCy = "Beth yw’r prif fan busnes newydd?"
  val filterCy = "Enw neu rif yr eiddo"
  val postcodeCy = "Cod post"
  val postcodeLinkCy = "Nid oes gan y cyfeiriad god post"
  val selectHeadingCy = "Dewiswch y cyfeiriad newydd"
  val editAddressLinkTextCy = "Golygwch y cyfeiriad â llaw"
  val confirmHeadingCy = "Cadarnhewch y prif fan busnes newydd"
  val continueCy = "Yn eich blaen"

  val clientServiceNameCy = "Cyfrif Treth Busnes"
  val agentServiceNameCy = "Diweddaru manylion TAW eich cleient"
  val phaseBannerHtml: AppConfig => String = conf =>
    s"This is a new service – your <a id='beta-banner-feedback' href='${conf.feedbackUrl}'>feedback</a> will help us to improve it."
  val phaseBannerHtmlCy: AppConfig => String = conf =>
    s"Gwasanaeth newydd yw hwn – bydd eich <a id='beta-banner-feedback' href='${conf.feedbackUrl}'>adborth</a> yn ein helpu i’w wella."
  val clientServiceName = "Business tax account"
  val agentServiceName = "Update your client's VAT details"

  val clientAddressLookupV2Json: AppConfig => String = conf => Json.stringify(Json.obj(fields =
    "version" -> 2,
    "options" -> Json.obj(
      "continueUrl" -> s"${conf.addressLookupCallbackUrl}",
      "deskProServiceName" -> "VATC",
      "showPhaseBanner" -> true,
      "ukMode" -> true,
      "timeoutConfig" -> Json.obj(
        "timeoutAmount" -> conf.timeoutPeriod,
        "timeoutUrl" -> conf.unauthorisedSignOutUrl
      )
    ),
    "labels" -> Json.obj(
      "en" -> Json.obj(
        "appLevelLabels" -> Json.obj(
          "navTitle" -> clientServiceName,
          "phaseBannerHtml" -> phaseBannerHtml(conf)
        ),
        "selectPageLabels" -> Json.obj(
          "title" -> selectHeading,
          "heading" -> selectHeading,
          "submitLabel" -> continue,
          "editAddressLinkText" -> editAddressLinkText
        ),
        "lookupPageLabels" -> Json.obj(
          "title" -> startHeading,
          "heading" -> startHeading,
          "filterLabel" -> filter,
          "postcodeLabel" -> postcode
        ),
        "confirmPageLabels" -> Json.obj(
          "title" -> confirmHeading,
          "heading" -> confirmHeading,
          "showConfirmChangeText" -> false
        ),
        "editPageLabels" -> Json.obj(
          "submitLabel" -> continue
        )
      ),
      "cy" -> Json.obj(
        "appLevelLabels" -> Json.obj(
          "navTitle" -> clientServiceNameCy,
          "phaseBannerHtml" -> phaseBannerHtmlCy(conf)
        ),
        "selectPageLabels" -> Json.obj(
          "title" -> selectHeadingCy,
          "heading" -> selectHeadingCy,
          "submitLabel" -> continueCy,
          "editAddressLinkText" -> editAddressLinkTextCy
        ),
        "lookupPageLabels" -> Json.obj(
          "title" -> startHeadingCy,
          "heading" -> startHeadingCy,
          "filterLabel" -> filterCy,
          "postcodeLabel" -> postcodeCy
        ),
        "confirmPageLabels" -> Json.obj(
          "title" -> confirmHeadingCy,
          "heading" -> confirmHeadingCy,
          "showConfirmChangeText" -> false
        ),
        "editPageLabels" -> Json.obj(
          "submitLabel" -> continueCy
        )
      )
    )
  ))

}
