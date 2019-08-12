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

import java.util.Base64

import config.features.Features
import config.{ConfigKeys => Keys}
import javax.inject.{Inject, Singleton}
import play.api.Mode.Mode
import play.api.i18n.Lang
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Call
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.binders.ContinueUrl
import uk.gov.hmrc.play.config.ServicesConfig

import scala.io.Source

trait AppConfig extends ServicesConfig {
  val analyticsToken: String
  val analyticsHost: String
  val reportAProblemPartialUrl: String
  val reportAProblemNonJSUrl: String
  val whitelistEnabled: Boolean
  val whitelistedIps: Seq[String]
  val whitelistExcludedPaths: Seq[Call]
  val shutterPage: String
  val signInUrl: String
  val features: Features
  val govUkCohoNameChangeUrl: String
  def surveyUrl(identifier: String): String
  def signOutExitSurveyUrl(identifier: String): String
  val unauthorisedSignOutUrl: String
  val addressLookupCallbackUrl: String
  def addressLookupService: String
  val addressLookupUrlHost: String
  val agentServicesGovUkGuidance: String
  val agentAuthoriseForClient: String
  val btaUrl: String
  val vatSummaryUrl: String
  val countryCodeJson: JsValue
  val signInContinueBaseUrl: String
  val bankAccountCoc: String
  val host: String
  val timeoutPeriod: Int
  val timeoutCountdown: Int
  val vatSubscriptionUrl: String
  val contactFormServiceIdentifier: String
  val contactFrontendService: String
  val agentInvitationsFastTrack: String
  val deregisterForVat: String
  val feedbackUrl: String
  val vatCorrespondenceChangeEmailUrl: String
  val partyTypes: Seq[String]
  val govUkChangeVatRegistrationDetails: String
  val govUkSoftwareGuidanceUrl: String
  val govUkVat484Form: String
  val vatAgentClientLookupFrontendUrl: String
  val agentClientLookupAgentAction: String
  def agentClientLookupUrl: String
  def agentClientUnauthorisedUrl: String
  val contactPreferencesService: String
  def contactPreferencesUrl(vrn: String): String
  val vatOptOutUrl: String
  def languageMap:Map[String,Lang]
  val routeToSwitchLanguage :String => Call
}

@Singleton
class FrontendAppConfig @Inject()(environment: Environment, implicit val runModeConfiguration: Configuration) extends ServicesConfig with AppConfig {

  private def getStringSeq(key: String): Seq[String] = runModeConfiguration.getStringSeq(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  override protected def mode: Mode = environment.mode

  lazy val appName: String = runModeConfiguration.getString("appName").getOrElse(throw new Exception("Missing configuration key: appName"))

  private lazy val contactHost: String = getString(Keys.contactFrontendService)
  override lazy val contactFrontendService: String = baseUrl("contact-frontend")
  override lazy val contactFormServiceIdentifier: String = "VATC"

  override lazy val analyticsToken: String = getString(Keys.googleAnalyticsToken)
  override lazy val analyticsHost: String = getString(Keys.googleAnalyticsHost)
  override lazy val reportAProblemPartialUrl: String = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  override lazy val reportAProblemNonJSUrl: String = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private def whitelistConfig(key: String): Seq[String] = Some(new String(Base64.getDecoder
    .decode(getString(key)), "UTF-8"))
    .map(_.split(",")).getOrElse(Array.empty).toSeq

  override lazy val whitelistEnabled: Boolean = getBoolean(Keys.whitelistEnabled)
  override lazy val whitelistedIps: Seq[String] = whitelistConfig(Keys.whitelistedIps)
  override lazy val whitelistExcludedPaths: Seq[Call] = whitelistConfig(Keys.whitelistExcludedPaths).map(path => Call("GET", path))
  override lazy val shutterPage: String = getString(Keys.whitelistShutterPage)

  private lazy val signInBaseUrl: String = getString(Keys.signInBaseUrl)

  override lazy val signInContinueBaseUrl: String = getString(Keys.signInContinueBaseUrl)
  private lazy val signInContinueUrl: String =
    ContinueUrl(signInContinueBaseUrl + controllers.routes.CustomerCircumstanceDetailsController.redirect().url).encodedUrl

  private lazy val signInOrigin = getString(Keys.appName)
  override lazy val signInUrl: String = s"$signInBaseUrl?continue=$signInContinueUrl&origin=$signInOrigin"

  override lazy val vatSubscriptionUrl: String = baseUrl(Keys.vatSubscription)

  override lazy val govUkCohoNameChangeUrl: String = getString(Keys.govUkCohoNameChangeUrl)

  override val features = new Features

  private lazy val surveyBaseUrl = getString(Keys.surveyHost) + getString(Keys.surveyUrl)
  override def surveyUrl(identifier: String): String = s"$surveyBaseUrl/$identifier"

  private lazy val governmentGatewayHost: String = getString(Keys.governmentGatewayHost)

  override def signOutExitSurveyUrl(identifier: String): String =
    s"$governmentGatewayHost/gg/sign-out?continue=${surveyUrl(identifier)}"
  override lazy val unauthorisedSignOutUrl: String = s"$governmentGatewayHost/gg/sign-out?continue=$signInContinueUrl"

  override lazy val addressLookupUrlHost: String = getString(Keys.addressLookupFrontendHost)

  override def addressLookupService: String = {
    if (features.stubAddressLookup()) {
      host + "/vat-through-software/account/test-only/address-lookup-stub"
    } else {
      baseUrl(Keys.addressLookupFrontend)
    }
  }

  override lazy val contactPreferencesService: String = {
    if (features.stubContactPreferences()) {
      baseUrl("vat-subscription-dynamic-stub")
    } else {
      baseUrl(Keys.contactPreferencesService)
    }
  }

  override def contactPreferencesUrl(vrn: String): String = contactPreferencesService + s"/contact-preferences/vat/vrn/$vrn"

  override lazy val addressLookupCallbackUrl: String =
    signInContinueBaseUrl + controllers.routes.BusinessAddressController.callback("").url

  override lazy val agentServicesGovUkGuidance: String = getString(Keys.govUkSetupAgentServices)

  override lazy val agentAuthoriseForClient: String = getString(Keys.agentAuthoriseForClient) + "/agent-subscription/start"

  override lazy val bankAccountCoc: String = baseUrl(Keys.bankAccountCoc)

  override lazy val btaUrl: String = getString("business-tax-account.host") + "/business-account"
  override lazy val vatSummaryUrl: String = getString("vat-summary-frontend.host") + "/vat-through-software/vat-overview"

  override lazy val countryCodeJson: JsValue = environment.resourceAsStream("country-codes.json") match {
    case Some(inputStream) => Json.parse(Source.fromInputStream(inputStream, "UTF-8").mkString)
    case _ => throw new Exception("Country codes file not found")
  }

  override lazy val host: String = getString(Keys.host)

  override lazy val timeoutPeriod: Int = getInt(Keys.timeoutPeriod)
  override lazy val timeoutCountdown: Int = getInt(Keys.timeoutCountDown)

  override lazy val agentInvitationsFastTrack: String = getString(Keys.agentInvitationsFastTrack)

  override lazy val deregisterForVat: String = getString(Keys.deregistrationForVat)

  override lazy val feedbackUrl: String = s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier" +
    s"&backUrl=${ContinueUrl(host + controllers.routes.CustomerCircumstanceDetailsController.redirect().url).encodedUrl}"

  override lazy val vatCorrespondenceChangeEmailUrl: String = getString(Keys.vatCorrespondenceChangeEmailUrl)

  override lazy val partyTypes: Seq[String] = getStringSeq(Keys.partyTypes)

  override lazy val govUkChangeVatRegistrationDetails: String = getString(Keys.changeVatRegistrationDetails)

  override lazy val govUkSoftwareGuidanceUrl: String = getString(Keys.softwareGuidanceUrl)
  override lazy val govUkVat484Form: String = getString(Keys.vat484Form)

  override lazy val vatAgentClientLookupFrontendUrl: String =
    getString(Keys.vatAgentClientLookupFrontendHost) + getString(Keys.vatAgentClientLookupFrontendUrl)

  def vatAgentClientLookupHandoff(redirectUrl: String): String =
    vatAgentClientLookupFrontendUrl + s"/client-vat-number?redirectUrl=${ContinueUrl(getString(Keys.host) + redirectUrl).encodedUrl}"

  override def agentClientLookupUrl: String =
    if (features.stubAgentClientLookup()) {
      testOnly.controllers.routes.StubAgentClientLookupController.show(controllers.routes.CustomerCircumstanceDetailsController.redirect().url).url
    } else {
      vatAgentClientLookupHandoff(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
    }

  override val agentClientLookupAgentAction: String =
    getString(ConfigKeys.vatAgentClientLookupFrontendHost) +
    getString(ConfigKeys.vatAgentClientLookupFrontendUrl) +
    getString(ConfigKeys.vatAgentClientLookupFrontendAgentAction)

  def vatAgentClientLookupUnauthorised(redirectUrl: String): String =
    vatAgentClientLookupFrontendUrl + s"/unauthorised-for-client?redirectUrl=${ContinueUrl(getString(Keys.host) + redirectUrl).encodedUrl}"

  override def agentClientUnauthorisedUrl: String =
    if (features.stubAgentClientLookup()) {
      testOnly.controllers.routes.StubAgentClientLookupController.unauth(controllers.routes.CustomerCircumstanceDetailsController.redirect().url).url
    } else {
      vatAgentClientLookupUnauthorised(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
    }

  override lazy val vatOptOutUrl: String = getString(Keys.vatOptOutUrl)

  override def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  override val routeToSwitchLanguage: String => Call = (lang: String) => controllers.routes.LanguageController.switchToLanguage(lang)
}
