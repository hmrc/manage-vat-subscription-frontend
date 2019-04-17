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

package testOnly.forms

import config.ConfigKeys
import play.api.data.Form
import play.api.data.Forms._
import testOnly.models.{Api1363Version, Api1365Version, FeatureSwitchModel, VatSubscriptionFeatureSwitchModel}

object FeatureSwitchForm {

  val api1365Version: String = "Api1365Version"
  val api1363Version: String = "Api1363Version"
  val latestApi1363Version: String = "latestApi1363Version"
  val stubDes: String = "stubDes"

  val form: Form[FeatureSwitchModel] = Form(
    mapping(
      ConfigKeys.simpleAuthFeature -> boolean,
      ConfigKeys.agentAccessFeature -> boolean,
      ConfigKeys.registrationStatusFeature -> boolean,
      ConfigKeys.contactDetailsSectionFeature -> boolean,
      "vatSubscriptionFeatures" -> mapping(
          latestApi1363Version -> boolean,
          api1363Version -> text.transform[Api1363Version](x => Api1363Version(x), _.id),
          api1365Version -> text.transform[Api1365Version](x => Api1365Version(x), _.id),
          stubDes -> boolean
        )(VatSubscriptionFeatureSwitchModel.apply)(VatSubscriptionFeatureSwitchModel.unapply),
      ConfigKeys.stubAgentClientLookupFeature -> boolean,
      ConfigKeys.stubAddressLookupFeature -> boolean,
      ConfigKeys.stubContactPreferencesFeature -> boolean,
      ConfigKeys.useContactPreferencesFeature -> boolean,
      ConfigKeys.allowAgentBankAccountChange -> boolean,
      ConfigKeys.makingTaxDigitalSectionFeature -> boolean,
      ConfigKeys.useLanguageSelectorFeature -> boolean
    )(FeatureSwitchModel.apply)(FeatureSwitchModel.unapply)
  )

}
