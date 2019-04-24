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

package testOnly.models

import models.customerAddress.AddressLookupJsonBuilder
import models.User
import play.api.i18n.Messages
import play.api.libs.json.{Json, OFormat}

case class StubAddressLookupJourneyConfig(continueUrl: String,
                         lookupPage: Option[LookupPage] = Some(LookupPage()),
                         selectPage: Option[SelectPage] = Some(SelectPage()),
                         confirmPage: Option[ConfirmPage] = Some(ConfirmPage()),
                         navTitle: Option[String] = None,
                         showPhaseBanner: Option[Boolean] = Some(false),
                         ukMode: Option[Boolean] = None)

case class LookupPage(title: Option[String] = None,
                      heading: Option[String] = None,
                      filterLabel: Option[String] = None,
                      postcodeLabel: Option[String] = None)

case class SelectPage(title: Option[String] = None,
                      heading: Option[String] = None,
                      submitLabel: Option[String] = None)

case class ConfirmPage(title: Option[String] = None,
                       heading: Option[String] = None,
                       showConfirmChangeText: Option[Boolean] = Some(false))

object StubAddressLookupJourneyConfig {

  implicit def stubModel(implicit user: User[_], messages: Messages): StubAddressLookupJourneyConfig =
    Json.toJson(AddressLookupJsonBuilder(
      testOnly.controllers.routes.StubAddressLookupController.callback("1").url)
    ).as[StubAddressLookupJourneyConfig]

  implicit val confirmPageFormat: OFormat[ConfirmPage] = Json.format[ConfirmPage]
  implicit val selectPageFormat: OFormat[SelectPage] = Json.format[SelectPage]
  implicit val lookupPageFormat: OFormat[LookupPage] = Json.format[LookupPage]
  implicit val stubAddressLookupJourneyConfig: OFormat[StubAddressLookupJourneyConfig] =
    Json.format[StubAddressLookupJourneyConfig]

}