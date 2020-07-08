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

package config

object ConfigKeys {


  val contactFrontendService: String = "contact-frontend.host"

  private val googleAnalyticsRoot: String = "google-analytics"
  val googleAnalyticsToken: String = googleAnalyticsRoot + ".token"
  val googleAnalyticsHost: String = googleAnalyticsRoot + ".host"
  val signInBaseUrl: String = "signIn.url"
  val signInContinueBaseUrl: String = "signIn.continueBaseUrl"

  val appName: String = "appName"

  val vatSubscription: String = "vat-subscription"

  val stubAgentClientLookupFeature: String = "features.stubAgentClientLookup"
  val stubAddressLookupFeature: String = "features.stubAddressLookup.enabled"
  val stubContactPreferencesFeature: String = "features.stubContactPreferences.enabled"
  val allowAgentBankAccountChange: String = "features.allowAgentBankAccountChange.enabled"
  val contactNumbersAndWebsiteFeature: String = "features.showContactNumbersAndWebsite.enabled"
  val useLanguageSelectorFeature: String = "features.useLanguageSelector.enabled"
  val useOverseasIndicator: String = "features.useOverseasIndicator.enabled"
  val changeClientFeature: String = "features.changeClientFeature.enabled"
  val emailVerifiedFeature: String = "features.emailVerifiedFeature.enabled"
  val missingTraderAddressIntercept: String = "features.missingTraderAddressIntercept.enabled"
  val contactDetailsMovedToBTA: String = "features.contactDetailsMovedToBTA.enabled"
  val contactPrefMigrationFeature: String = "features.contactPrefMigration.enabled"

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

  val vatCorrespondenceChangeEmailUrl: String = "vat-correspondence-details-frontend.changeEmailUrl"
  val vatCorrespondenceChangeLandlineNumberUrl: String = "vat-correspondence-details-frontend.changeLandlineNumberUrl"
  val vatCorrespondenceChangeMobileNumberUrl: String = "vat-correspondence-details-frontend.changeMobileNumberUrl"
  val vatCorrespondenceChangeWebsiteUrl: String = "vat-correspondence-details-frontend.changeWebsiteUrl"

  val vatAgentClientLookupFrontendHost: String = "vat-agent-client-lookup-frontend.host"
  val vatAgentClientLookupFrontendUrl: String = "vat-agent-client-lookup-frontend.url"
  val vatAgentClientLookupFrontendClientAccount: String = "vat-agent-client-lookup-frontend.client-vat-account"

  val contactPreferencesService: String = "contact-preferences"

  val accessibilityReportHost: String = "accessibilityReport.host"
  val accessibilityReportUrl: String = "accessibilityReport.url"

  val vatReturnPeriodFrontendHost: String = "vat-return-period-frontend.host"
  val vatReturnPeriodFrontendUrl: String = "vat-return-period-frontend.url"

  val businessTaxAccount: String = "business-tax-account"
  val businessTaxAccountHost: String = "business-tax-account.host"
  val businessTaxAccountUrl: String = "business-tax-account.homeUrl"
  val businessTaxAccountMessagesUrl: String = "business-tax-account.messagesUrl"
  val businessTaxAccountManageAccountUrl: String = "business-tax-account.manageAccountUrl"
  val businessTaxAccountPartialUrl = "business-tax-account.partialUrl"
  val businessTaxAccountDetails = "business-tax-account.accountDetails"

  val helpAndContactFrontendBase: String = "help-and-contact-frontend.host"
  val helpAndContactHelpUrl: String = "help-and-contact-frontend.helpUrl"

  val disableBulkPaper:String =  "features.disableBulkPaper.enabled"


}
