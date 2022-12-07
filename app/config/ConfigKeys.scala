/*
 * Copyright 2022 HM Revenue & Customs
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
  val contactFrontendIdentifier: String = "contact-frontend.serviceId"

  private val googleAnalyticsRoot: String = "google-analytics"
  val googleAnalyticsToken: String = googleAnalyticsRoot + ".token"
  val googleAnalyticsHost: String = googleAnalyticsRoot + ".host"
  val signInBaseUrl: String = "signIn.url"
  val signInContinueBaseUrl: String = "signIn.continueBaseUrl"

  val appName: String = "appName"

  val vatSubscription: String = "vat-subscription"

  val stubAgentClientLookupFeature: String = "features.stubAgentClientLookup"
  val stubAddressLookupFeature: String = "features.stubAddressLookup.enabled"

  // GOV UK
  val changeToBusinessDetailsUrl: String = "gov-uk.guidance.change-to-business-details.url"
  val changeVatRegistrationDetails: String = "gov-uk.guidance.changeVatRegistrationDetails.url"
  val softwareGuidanceUrl: String = "gov-uk.guidance.software-guidance.url"
  val vat484Form: String = "gov-uk.guidance.vat484Form.url"
  val govUkCohoNameChangeUrl: String = "gov-uk.guidance.coho-name-change.url"
  val govUkTrustNameChangeUrl: String = "gov-uk.guidance.trust-name-change.url"
  val govUkSetupAgentServices: String = "gov-uk.guidance.setupAgentServices.url"

  val partyTypesOrgNameRow: String = "party-types-orgNameRow"
  val partyTypesNspItmpOrSAMastered: String = "party-types-nspItmpOrSAMastered"
  val partyTypesTrusts: String = "party-types-trusts"

  val governmentGatewayHost: String = "government-gateway.host"

  val addressLookupFrontend: String = "address-lookup-frontend"
  val addressLookupCallback: String = s"$addressLookupFrontend.callbackUrl"
  val addressLookupFrontendHost: String = s"$addressLookupFrontend.host"

  val surveyHost: String = "feedback-frontend.host"
  val surveyUrl: String = "feedback-frontend.url"

  val agentAuthoriseForClient: String = "agent-subscription-frontend.host"

  val bankAccountCoc: String = "bank-account-coc"

  val host: String = "host"

  val timeoutPeriod: String = "timeout.period"
  val timeoutCountDown: String = "timeout.countDown"

  val agentInvitationsFastTrack: String = "agent-invitations-fast-track.url"

  val vatCorrespondenceFrontendHost: String = "vat-correspondence-details-frontend.host"
  val vatCorrespondenceChangeEmailUrl: String = "vat-correspondence-details-frontend.changeEmailUrl"
  val vatCorrespondenceChangeLandlineNumberUrl: String = "vat-correspondence-details-frontend.changeLandlineNumberUrl"
  val vatCorrespondenceChangeMobileNumberUrl: String = "vat-correspondence-details-frontend.changeMobileNumberUrl"
  val vatCorrespondenceChangeWebsiteUrl: String = "vat-correspondence-details-frontend.changeWebsiteUrl"
  val vatCorrespondenceFixYourEmail: String = "vat-correspondence-details-frontend.fixEmail"

  val vatDesignatoryDetailsFrontendHost: String = "vat-designatory-details-frontend.host"
  val vatDesignatoryDetailsNewTradingNameUrl: String = "vat-designatory-details-frontend.newTradingNameUrl"
  val vatDesignatoryDetailsNewBusinessNameUrl: String = "vat-designatory-details-frontend.newBusinessNameUrl"

  val vatAgentClientLookupFrontendHost: String = "vat-agent-client-lookup-frontend.host"
  val vatAgentClientLookupFrontendUrl: String = "vat-agent-client-lookup-frontend.url"
  val vatAgentClientLookupFrontendClientAccount: String = "vat-agent-client-lookup-frontend.client-vat-account"

  val accessibilityReportUrl: String = "accessibility-statement.service-path"

  val vatReturnPeriodFrontendHost: String = "vat-return-period-frontend.host"
  val vatReturnPeriodFrontendUrl: String = "vat-return-period-frontend.url"

  val businessTaxAccount: String = "business-tax-account"
  val businessTaxAccountHost: String = "business-tax-account.host"
  val businessTaxAccountUrl: String = "business-tax-account.homeUrl"
  val businessTaxAccountDetails = "business-tax-account.accountDetails"

  val gtmContainer: String = "tracking-consent-frontend.gtm.container"
}
