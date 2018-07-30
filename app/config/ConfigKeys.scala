/*
 * Copyright 2018 HM Revenue & Customs
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

  val simpleAuthFeature: String = "features.simpleAuth.enabled"
  val agentAccessFeature: String = "features.agentAccess.enabled"
  val registrationStatusFeature: String = "features.registrationStatus.enabled"

  val govUkCohoNameChangeUrl: String = "gov-uk.guidance.coho-name-change.url"
  val govUkSetupAgentServices: String = "gov-uk.guidance.setupAgentServices.url"

  val governmentGatewayHost: String = "government-gateway.host"

  val addressLookupFrontend: String = "address-lookup-frontend"
  val addressLookupFrontendHost: String = s"$addressLookupFrontend.host"


  val surveyHost: String = "feedback-survey-frontend.host"
  val surveyUrl: String = "feedback-survey-frontend.url"

  val agentAuthoriseForClient: String = "agent-subscription-frontend.host"

  val bankAccountCoc: String = "bank-account-coc"

  val host: String = "host"

  val timeoutPeriod: String = "timeout.period"
  val timeoutCountDown: String = "timeout.countDown"
}
