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

package testOnly.forms

import config.ConfigKeys
import play.api.data.Form
import play.api.data.Forms._
import testOnly.models.{Api1363Version, Api1365Version, FeatureSwitchModel, VatSubscriptionFeatureSwitchModel}

object FeatureSwitchForm {

  val api1365Version: String = "Api1365Version"
  val api1363Version: String = "Api1363Version"
  val enableAnnualAccounting: String = "enableAnnualAccounting"
  val newStatusIndicators: String = "newStatusIndicators"

  val form: Form[FeatureSwitchModel] = Form(
    mapping(
      "vatSubscriptionFeatures" -> mapping(
          api1363Version -> text.transform[Api1363Version](x => Api1363Version(x), _.id),
          api1365Version -> text.transform[Api1365Version](x => Api1365Version(x), _.id),
          enableAnnualAccounting -> boolean,
          newStatusIndicators -> boolean
        )(VatSubscriptionFeatureSwitchModel.apply)(VatSubscriptionFeatureSwitchModel.unapply),
      ConfigKeys.stubAgentClientLookupFeature -> boolean,
      ConfigKeys.stubAddressLookupFeature -> boolean,
      ConfigKeys.stubContactPreferencesFeature -> boolean,
      ConfigKeys.allowAgentBankAccountChange -> boolean,
      ConfigKeys.contactNumbersAndWebsiteFeature -> boolean,
      ConfigKeys.useLanguageSelectorFeature -> boolean,
      ConfigKeys.useOverseasIndicator -> boolean,
      ConfigKeys.changeClientFeature -> boolean,
      ConfigKeys.emailVerifiedFeature -> boolean,
      ConfigKeys.disableBulkPaper -> boolean,
      ConfigKeys.missingTraderAddressIntercept -> boolean,
      ConfigKeys.contactDetailsMovedToBTA -> boolean,
      ConfigKeys.contactPrefMigrationFeature -> boolean,
      ConfigKeys.tradingNameRowFeature -> boolean
    )(FeatureSwitchModel.apply)(FeatureSwitchModel.unapply)
  )
}
