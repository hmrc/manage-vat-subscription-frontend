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

package mocks

import config.AppConfig
import config.features.Features
import play.api.{Configuration, Mode}
import play.api.Mode.Mode
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Call

class MockAppConfig(implicit val runModeConfiguration: Configuration) extends AppConfig {

  override val mode: Mode = Mode.Test

  override val analyticsToken: String = ""
  override val analyticsHost: String = ""
  override val reportAProblemPartialUrl: String = ""
  override val reportAProblemNonJSUrl: String = ""
  override val whitelistEnabled: Boolean = false
  override val whitelistedIps: Seq[String] = Seq("")
  override val whitelistExcludedPaths: Seq[Call] = Nil
  override val shutterPage: String = "https://www.tax.service.gov.uk/shutter/vat-through-software"
  override val signInUrl: String = "sign-in"
  override val signOutExitSurveyUrl: String = "/some-gg-signout-url"
  override val unauthorisedSignOutUrl: String = ""
  override val surveyUrl: String = "/some-survey-url"
  override val features: Features = new Features
  override val govUkCohoNameChangeUrl: String = "/gov-uk/coho-name-change"
  override val addressLookupCallbackUrl: String = ""
  override val addressLookupService: String = ""
  override val addressLookupUrlHost: String = ""
  override val agentServicesGovUkGuidance: String = "guidance/get-an-hmrc-agent-services-account"
  override val agentAuthoriseForClient: String = "agent-subscription/start"
  override val btaUrl = "ye olde bta url"
  override val vatSummaryUrl = "ye olde vat summary url"
  override val countryCodeJson: JsValue = Json.arr(
    Json.obj(
      "countryCode" -> "GB",
      "country" -> "United Kingdom"
    )
  )
  override val bankAccountCoc: String = ""
  override val signInContinueBaseUrl: String = "/manage-vat-subscription-frontend"
  override val host: String = "/manage-vat-subscription-frontend"
  override val timeoutPeriod: Int = 1800
  override val timeoutCountdown: Int = 20
  override val vatSubscriptionUrl: String = "/subscription"
  override val contactFormServiceIdentifier: String = "VATC"
  override val contactFrontendService: String = "/contact-frontend"
  override val agentInvitationsFastTrack: String = "/agent-invitations-frontend"
  override val deregisterForVat: String = "ye-olde-deregister-url"
  override val feedbackUrl: String = "/feedback"
  override val vatCorrespondenceChangeEmailUrl: String = "mock-change-email-url"
  override val partyTypes: Seq[String] = Seq("2","4","7","11","50","52","59","62")
  override val govUkChangeVatRegistrationDetails: String = "mock-gov-uk-url"
  override val govUkSoftwareGuidanceUrl: String = "software-guidance"
  override val signOutTimeoutUrl: String = "/gg/signout-for-timeout"
  override val vatAgentClientLookupFrontendUrl: String = "/vaclf"
  override def agentClientLookupUrl: String = "/agent-client-lookup"
  override def agentClientUnauthorisedUrl: String = "agent-client-unauthorised"
  override val contactPreferencesService: String = ""
  override def contactPreferencesUrl(vrn: String): String = s"contact-preferences/vat/vrn/$vrn"
  override val govUkVat484Form: String = "/link-to-page"
}
