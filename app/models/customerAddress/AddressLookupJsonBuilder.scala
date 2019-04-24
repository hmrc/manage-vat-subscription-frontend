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

package models.customerAddress

import models.User
import play.api.libs.json._
import play.api.i18n.Messages
import views.utils.ServiceNameUtil


case class AddressLookupJsonBuilder(continueUrl: String)(implicit user: User[_], messages: Messages) {

  // general journey overrides
  val navTitle: String = ServiceNameUtil.generateHeader
  val showPhaseBanner = true
  val ukMode = true

  // lookup page overrides
  val lookupPage = Map(
    "title" -> messages("address_lookupPage.heading"),
    "heading" -> messages("address_lookupPage.heading"),
    "filterLabel" -> messages("address_lookupPage.filter"),
    "postcodeLabel" -> messages("address_lookupPage.postcode")
  )

  val selectPage = Map(
    "title" -> messages("address_lookupPage.selectPage.heading"),
    "heading" -> messages("address_lookupPage.selectPage.heading"),
    "editAddressLinkText" -> messages("address_lookupPage.selectPage.editLink"),
    "submitLabel" -> messages("common.continue")
  )

  val editPage = Map(
    "submitLabel" -> messages("common.continue")
  )

  val confirmPage = Json.obj(
    "title" -> messages("address_lookupPage.confirmPage.heading"),
    "heading" -> messages("address_lookupPage.confirmPage.heading"),
    "showConfirmChangeText" -> false
  )
}

object AddressLookupJsonBuilder {

    implicit val writes: Writes[AddressLookupJsonBuilder] = new Writes[AddressLookupJsonBuilder] {
      def writes(data: AddressLookupJsonBuilder): JsObject = Json.obj(fields =

        "continueUrl" -> data.continueUrl,
        "showPhaseBanner" -> data.showPhaseBanner,
        "navTitle" -> data.navTitle,
        "ukMode" -> data.ukMode,

        "lookupPage" -> data.lookupPage,

        "selectPage" -> data.selectPage,

        "editPage" -> data.editPage,

        "confirmPage" -> data.confirmPage
      )
    }
  }
