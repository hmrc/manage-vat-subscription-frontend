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

package config

object ConfigKeys {

  val contactFrontendService: String = "contact-frontend.host"

  private val googleAnalyticsRoot: String = "google-analytics"
  val googleAnalyticsToken: String = googleAnalyticsRoot + ".token"
  val googleAnalyticsHost: String = googleAnalyticsRoot + ".host"

  val whitelistEnabled: String = "whitelist.enabled"
  val whitelistedIps: String = "whitelist.allowedIps"
  val whitelistExcludedPaths: String = "whitelist.excludedPaths"
  val whitelistShutterPage: String = "whitelist.shutter-page-url"

  val signInBaseUrl: String = "signIn.url"
  val signInContinueBaseUrl: String = "signIn.continueBaseUrl"

  val appName: String = "appName"

  val vatSubscription: String = "vat-subscription"

  val contactDetailsSectionFeature: String = "features.contactDetailsSection.enabled"
  val stubAgentClientLookupFeature: String = "features.stubAgentClientLookup"
  val stubAddressLookupFeature: String = "features.stubAddressLookup.enabled"
  val stubContactPreferencesFeature: String = "features.stubContactPreferences.enabled"
  val allowAgentBankAccountChange: String = "features.allowAgentBankAccountChange.enabled"
  val contactNumbersAndWebsiteFeature: String = "features.showContactNumbersAndWebsite.enabled"
  val useLanguageSelectorFeature: String = "features.useLanguageSelector.enabled"
  val changeClientFeature: String = "features.changeClientFeature.enabled"
  val useNewAddressLookupFeature: String = "features.useNewAddressLookupFeature.enabled"
  val accessibilityReportFeature: String = "features.accessibilityReport.enabled"
  val useVatReturnPeriodFrontend: String = "features.useVatReturnPeriodFrontend.enabled"

  // GOV UK
  val changeVatRegistrationDetails: String = "gov-uk.guidance.changeVatRegistrationDetails.url"
  val softwareGuidanceUrl: String = "gov-uk.guidance.software-guidance.url"
  val vat484Form: String = "gov-uk.guidance.vat484Form.url"
  val govUkCohoNameChangeUrl: String = "gov-uk.guidance.coho-name-change.url"
  val govUkSetupAgentServices: String = "gov-uk.guidance.setupAgentServices.url"

  val partyTypes: String = "party-types"

  val governmentGatewayHost: String = "government-gateway.host"

  val addressLookupFrontend: String = "address-lookup-frontend"
  val addressLookupFrontendHost: String = s"$addressLookupFrontend.host"

  val surveyHost: String = "feedback-frontend.host"
  val surveyUrl: String = "feedback-frontend.url"

  val agentAuthoriseForClient: String = "agent-subscription-frontend.host"

  val bankAccountCoc: String = "bank-account-coc"

  val host: String = "host"

  val timeoutPeriod: String = "timeout.period"
  val timeoutCountDown: String = "timeout.countDown"

  val agentInvitationsFastTrack: String = "agent-invitations-fast-track.url"

  val deregistrationForVat: String = "deregister-vat-frontend.url"

  val vatCorrespondenceChangeEmailUrl: String = "vat-correspondence-details-frontend.changeEmailUrl"
  val vatCorrespondenceChangeLandlineNumberUrl: String = "vat-correspondence-details-frontend.changeLandlineNumberUrl"
  val vatCorrespondenceChangeMobileNumberUrl: String = "vat-correspondence-details-frontend.changeMobileNumberUrl"
  val vatCorrespondenceChangeWebsiteUrl: String = "vat-correspondence-details-frontend.changeWebsiteUrl"

  val vatAgentClientLookupFrontendHost: String = "vat-agent-client-lookup-frontend.host"
  val vatAgentClientLookupFrontendUrl: String = "vat-agent-client-lookup-frontend.url"
  val vatAgentClientLookupFrontendAgentAction: String = "vat-agent-client-lookup-frontend.agent-action-extension"

  val contactPreferencesService: String = "contact-preferences"

  val vatOptOutUrl: String = "vat-opt-out-frontend.url"

  val accessibilityReportHost: String = "accessibilityReport.host"
  val accessibilityReportUrl: String = "accessibilityReport.url"

  val vatReturnPeriodFrontendHost: String = "vat-return-period-frontend.host"
  val vatReturnPeriodFrontendUrl: String = "vat-return-period-frontend.url"
}
