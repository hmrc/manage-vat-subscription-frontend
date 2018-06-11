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

package models.customerAddress

import play.api.libs.json._

case class AddressLookupJsonBuilder(continueUrl: String) {

  // general journey overrides
  val navTitle = "Business tax account"
  val showPhaseBanner = true
  val ukMode = true

  // lookup page overrides
  val lookupPage = Map(
    "title" -> "Changes in circumstances",
    "heading" -> "Select the new business address",
    "filterLabel" -> "Property name or number",
    "postcodeLabel" -> "Postcode"
//    "manualAddressLinkText" -> "bob" // ignored whilst ukMode == true?
  )
}

object AddressLookupJsonBuilder {

    implicit val writes: Writes[AddressLookupJsonBuilder] = new Writes[AddressLookupJsonBuilder] {
      def writes(data: AddressLookupJsonBuilder): JsObject = Json.obj(fields =

        "continueUrl" -> data.continueUrl,
        "showPhaseBanner" -> data.showPhaseBanner,
        "navTitle" -> data.navTitle,
        "ukMode" -> data.ukMode,

        "lookupPage" -> data.lookupPage
      )
    }
  }
