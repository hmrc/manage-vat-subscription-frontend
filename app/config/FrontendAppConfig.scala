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
import play.api.i18n.Lang
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Call
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.binders.ContinueUrl
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.io.Source

trait AppConfig {
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
  val vatCorrespondenceChangeLandlineNumberUrl: String
  val vatCorrespondenceChangeMobileNumberUrl: String
  val vatCorrespondenceChangeWebsiteUrl: String
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
  val mtdSignUpUrl: String => String
  def languageMap: Map[String,Lang]
  val routeToSwitchLanguage: String => Call
  val accessibilityReportUrl: String
  val vatReturnPeriodFrontendUrl: String
}

@Singleton
class FrontendAppConfig @Inject()(implicit configuration: Configuration, sc: ServicesConfig, environment: Environment) extends AppConfig {

  private def getStringSeq(key: String): Seq[String] =
    configuration.getOptional[Seq[String]](key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  lazy val appName: String = "manage-vat-subscription-frontend"

  private lazy val contactHost: String = sc.getString(Keys.contactFrontendService)
  override lazy val contactFrontendService: String = sc.baseUrl("contact-frontend")
  override lazy val contactFormServiceIdentifier: String = "VATC"

  override lazy val reportAProblemPartialUrl: String = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  override lazy val reportAProblemNonJSUrl: String = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private def whitelistConfig(key: String): Seq[String] = Some(new String(Base64.getDecoder
    .decode(sc.getString(key)), "UTF-8"))
    .map(_.split(",")).getOrElse(Array.empty).toSeq

  override lazy val whitelistEnabled: Boolean = sc.getBoolean(Keys.whitelistEnabled)
  override lazy val whitelistedIps: Seq[String] = whitelistConfig(Keys.whitelistedIps)
  override lazy val whitelistExcludedPaths: Seq[Call] = whitelistConfig(Keys.whitelistExcludedPaths).map(path => Call("GET", path))
  override lazy val shutterPage: String = sc.getString(Keys.whitelistShutterPage)

  private lazy val signInBaseUrl: String = sc.getString(Keys.signInBaseUrl)

  override lazy val signInContinueBaseUrl: String = sc.getString(Keys.signInContinueBaseUrl)
  private lazy val signInContinueUrl: String =
    ContinueUrl(signInContinueBaseUrl + controllers.routes.CustomerCircumstanceDetailsController.redirect().url).encodedUrl

  private lazy val signInOrigin = sc.getString(Keys.appName)
  override lazy val signInUrl: String = s"$signInBaseUrl?continue=$signInContinueUrl&origin=$signInOrigin"

  override lazy val vatSubscriptionUrl: String = sc.baseUrl(Keys.vatSubscription)

  override lazy val govUkCohoNameChangeUrl: String = sc.getString(Keys.govUkCohoNameChangeUrl)

  override val features = new Features

  private lazy val surveyBaseUrl = sc.getString(Keys.surveyHost) + sc.getString(Keys.surveyUrl)
  override def surveyUrl(identifier: String): String = s"$surveyBaseUrl/$identifier"

  private lazy val governmentGatewayHost: String = sc.getString(Keys.governmentGatewayHost)

  override def signOutExitSurveyUrl(identifier: String): String =
    s"$governmentGatewayHost/gg/sign-out?continue=${surveyUrl(identifier)}"
  override lazy val unauthorisedSignOutUrl: String = s"$governmentGatewayHost/gg/sign-out?continue=$signInContinueUrl"

  override lazy val addressLookupUrlHost: String = sc.getString(Keys.addressLookupFrontendHost)

  override def addressLookupService: String = {
    if (features.stubAddressLookup()) {
      host + "/vat-through-software/account/test-only/address-lookup-stub"
    } else {
      sc.baseUrl(Keys.addressLookupFrontend)
    }
  }

  override lazy val contactPreferencesService: String = {
    if (features.stubContactPreferences()) {
      sc.baseUrl("vat-subscription-dynamic-stub")
    } else {
      sc.baseUrl(Keys.contactPreferencesService)
    }
  }

  override def contactPreferencesUrl(vrn: String): String = contactPreferencesService + s"/contact-preferences/vat/vrn/$vrn"

  override lazy val addressLookupCallbackUrl: String =
    signInContinueBaseUrl + controllers.routes.BusinessAddressController.callback("").url

  override lazy val agentServicesGovUkGuidance: String = sc.getString(Keys.govUkSetupAgentServices)

  override lazy val agentAuthoriseForClient: String = sc.getString(Keys.agentAuthoriseForClient) + "/agent-subscription/start"

  override lazy val bankAccountCoc: String = sc.baseUrl(Keys.bankAccountCoc)

  override lazy val btaUrl: String = sc.getString("business-tax-account.host") + "/business-account"
  override lazy val vatSummaryUrl: String = sc.getString("vat-summary-frontend.host") + "/vat-through-software/vat-overview"

  override lazy val countryCodeJson: JsValue = environment.resourceAsStream("country-codes.json") match {
    case Some(inputStream) => Json.parse(Source.fromInputStream(inputStream, "UTF-8").mkString)
    case _ => throw new Exception("Country codes file not found")
  }

  override lazy val host: String = sc.getString(Keys.host)

  override lazy val timeoutPeriod: Int = sc.getInt(Keys.timeoutPeriod)
  override lazy val timeoutCountdown: Int = sc.getInt(Keys.timeoutCountDown)

  override lazy val agentInvitationsFastTrack: String = sc.getString(Keys.agentInvitationsFastTrack)

  override lazy val deregisterForVat: String = sc.getString(Keys.deregistrationForVat)

  override lazy val feedbackUrl: String = s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier" +
    s"&backUrl=${ContinueUrl(host + controllers.routes.CustomerCircumstanceDetailsController.redirect().url).encodedUrl}"

  override lazy val vatCorrespondenceChangeEmailUrl: String = sc.getString(Keys.vatCorrespondenceChangeEmailUrl)
  override lazy val vatCorrespondenceChangeLandlineNumberUrl: String = sc.getString(Keys.vatCorrespondenceChangeLandlineNumberUrl)
  override lazy val vatCorrespondenceChangeMobileNumberUrl: String = sc.getString(Keys.vatCorrespondenceChangeMobileNumberUrl)
  override lazy val vatCorrespondenceChangeWebsiteUrl: String = sc.getString(Keys.vatCorrespondenceChangeWebsiteUrl)

  override lazy val partyTypes: Seq[String] = getStringSeq(Keys.partyTypes)

  override lazy val govUkChangeVatRegistrationDetails: String = sc.getString(Keys.changeVatRegistrationDetails)

  override lazy val govUkSoftwareGuidanceUrl: String = sc.getString(Keys.softwareGuidanceUrl)
  override lazy val govUkVat484Form: String = sc.getString(Keys.vat484Form)

  override lazy val vatAgentClientLookupFrontendUrl: String =
    sc.getString(Keys.vatAgentClientLookupFrontendHost) + sc.getString(Keys.vatAgentClientLookupFrontendUrl)

  def vatAgentClientLookupHandoff(redirectUrl: String): String =
    vatAgentClientLookupFrontendUrl + s"/client-vat-number?redirectUrl=${ContinueUrl(sc.getString(Keys.host) + redirectUrl).encodedUrl}"

  override def agentClientLookupUrl: String =
    if (features.stubAgentClientLookup()) {
      testOnly.controllers.routes.StubAgentClientLookupController.show(controllers.routes.CustomerCircumstanceDetailsController.redirect().url).url
    } else {
      vatAgentClientLookupHandoff(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
    }

  override val agentClientLookupAgentAction: String =
    sc.getString(ConfigKeys.vatAgentClientLookupFrontendHost) +
    sc.getString(ConfigKeys.vatAgentClientLookupFrontendUrl) +
    sc.getString(ConfigKeys.vatAgentClientLookupFrontendAgentAction)

  def vatAgentClientLookupUnauthorised(redirectUrl: String): String =
    vatAgentClientLookupFrontendUrl + s"/unauthorised-for-client?redirectUrl=${ContinueUrl(sc.getString(Keys.host) + redirectUrl).encodedUrl}"

  override def agentClientUnauthorisedUrl: String =
    if (features.stubAgentClientLookup()) {
      testOnly.controllers.routes.StubAgentClientLookupController.unauth(controllers.routes.CustomerCircumstanceDetailsController.redirect().url).url
    } else {
      vatAgentClientLookupUnauthorised(controllers.routes.CustomerCircumstanceDetailsController.redirect().url)
    }

  override lazy val vatOptOutUrl: String = sc.getString(Keys.vatOptOutUrl)
  override lazy val mtdSignUpUrl: String => String = (vrn: String) => s"${sc.getString(Keys.mtdSignUpUrl)}/$vrn"

  override def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  override val routeToSwitchLanguage: String => Call = (lang: String) => controllers.routes.LanguageController.switchToLanguage(lang)

  private lazy val accessibilityReportHost: String = sc.getString(Keys.accessibilityReportHost)
  override lazy val accessibilityReportUrl: String = accessibilityReportHost + sc.getString(Keys.accessibilityReportUrl)

  private lazy val vatReturnPeriodFrontendHost: String = sc.getString(ConfigKeys.vatReturnPeriodFrontendHost)
  override lazy val vatReturnPeriodFrontendUrl: String = vatReturnPeriodFrontendHost + sc.getString(ConfigKeys.vatReturnPeriodFrontendUrl)
}
