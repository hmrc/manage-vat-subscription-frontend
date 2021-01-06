/*
 * Copyright 2021 HM Revenue & Customs
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

import config.AppConfig
import models.User
import models.customerAddress.AddressLookupJsonBuilder
import play.api.i18n.MessagesApi
import play.api.libs.json.{Json, OFormat}

case class StubAddressLookupJourneyConfig(version: Int,
                                          options: Options,
                                          labels: Labels)

case class Options(continueUrl: String,
                   deskProServiceName: Option[String] = None,
                   showPhaseBanner: Option[Boolean] = Some(false),
                   ukMode: Option[Boolean] = None,
                   timeoutConfig: TimeoutConfig)

case class TimeoutConfig(timeoutAmount: Int, timeoutUrl: String)

case class Labels(en: EnCy, cy: EnCy)

case class EnCy(appLevelLabels: AppLevelLabels,
                selectPageLabels: Option[SelectPage],
                lookupPageLabels: Option[LookupPage],
                confirmPageLabels: Option[ConfirmPage])

case class AppLevelLabels(navTitle: Option[String] = None,
                          phaseBannerHtml: Option[String] = None)

case class SelectPage(title: Option[String] = None,
                      heading: Option[String] = None,
                      submitLabel: Option[String] = None)

case class LookupPage(title: Option[String] = None,
                      heading: Option[String] = None,
                      filterLabel: Option[String] = None,
                      postcodeLabel: Option[String] = None)

case class ConfirmPage(title: Option[String] = None,
                       heading: Option[String] = None,
                       showConfirmChangeText: Option[Boolean] = Some(false))

object StubAddressLookupJourneyConfig {

  implicit def stubModel(implicit user: User[_], messagesApi: MessagesApi, config: AppConfig): StubAddressLookupJourneyConfig = {
    Json.toJson(AddressLookupJsonBuilder(
      testOnly.controllers.routes.StubAddressLookupController.callback("1").url)
    ).as[StubAddressLookupJourneyConfig]
  }
  implicit val confirmPageFormat: OFormat[ConfirmPage] = Json.format[ConfirmPage]
  implicit val selectPageFormat: OFormat[SelectPage] = Json.format[SelectPage]
  implicit val lookupPageFormat: OFormat[LookupPage] = Json.format[LookupPage]
  implicit val TimeoutConfigFormat: OFormat[TimeoutConfig] = Json.format[TimeoutConfig]
  implicit val optionsFormat: OFormat[Options] = Json.format[Options]

  implicit val AppLevelLabelsFormat: OFormat[AppLevelLabels] = Json.format[AppLevelLabels]
  implicit val EnCyFormat: OFormat[EnCy] = Json.format[EnCy]
  implicit val LabelsFormat: OFormat[Labels] = Json.format[Labels]

  implicit val stubAddressLookupJourneyConfig: OFormat[StubAddressLookupJourneyConfig] =
    Json.format[StubAddressLookupJourneyConfig]

}
