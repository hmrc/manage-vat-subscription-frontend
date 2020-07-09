/*
 * Copyright 2020 HM Revenue & Customs
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

package config.features

import javax.inject.{Inject, Singleton}

import config.ConfigKeys
import play.api.Configuration

@Singleton
class Features @Inject()(implicit config: Configuration) {

  val stubAgentClientLookup = new Feature(ConfigKeys.stubAgentClientLookupFeature)
  val stubAddressLookup = new Feature(ConfigKeys.stubAddressLookupFeature)
  val stubContactPreferences = new Feature(ConfigKeys.stubContactPreferencesFeature)
  val allowAgentBankAccountChange = new Feature(ConfigKeys.allowAgentBankAccountChange)
  val showContactNumbersAndWebsite = new Feature(ConfigKeys.contactNumbersAndWebsiteFeature)
  val useLanguageSelector = new Feature(ConfigKeys.useLanguageSelectorFeature)
  val useOverseasIndicator = new Feature(ConfigKeys.useOverseasIndicator)
  val changeClientFeature = new Feature(ConfigKeys.changeClientFeature)
  val emailVerifiedFeature = new Feature(ConfigKeys.emailVerifiedFeature)
  val disableBulkPaper = new Feature(ConfigKeys.disableBulkPaper)
  val missingTraderAddressIntercept = new Feature(ConfigKeys.missingTraderAddressIntercept)
  val contactDetailsMovedToBTA = new Feature(ConfigKeys.contactDetailsMovedToBTA)
  val contactPrefMigrationFeature = new Feature(ConfigKeys.contactPrefMigrationFeature)
}