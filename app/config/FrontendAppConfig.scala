/*
 * Copyright 2024 HM Revenue & Customs
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

import config.features.Features
import config.{ConfigKeys => Keys}
import javax.inject.{Inject, Singleton}
import play.api.i18n.Lang
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Call
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.bootstrap.binders.SafeRedirectUrl
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.io.Source

trait AppConfig {
  val reportAProblemPartialUrl: String
  val reportAProblemNonJSUrl: String
  val signInUrl: String
  val features: Features
  val govUkCohoNameChangeUrl: String
  val govUkTrustNameChangeUrl: String
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
  val contactFrontendService: String
  val contactFormServiceIdentifier: String
  val agentInvitationsFastTrack: String
  val feedbackUrl: String
  val vatCorrespondenceChangeEmailUrl: String
  val vatCorrespondenceChangeLandlineNumberUrl: String
  val vatCorrespondenceChangeMobileNumberUrl: String
  val vatCorrespondenceChangeWebsiteUrl: String
  val vatCorrespondenceFixYourEmail : String
  val vatDesignatoryDetailsTradingNameUrl: String
  val vatDesignatoryDetailsBusinessNameUrl: String
  def partyTypes: Seq[String]
  val partyTypesNspItmpOrSAMastered: Seq[String]
  val partyTypesTrusts: Seq[String]
  val govUkChangeToBusinessDetails: String
  val govUkChangeVatRegistrationDetails: String
  val govUkSoftwareGuidanceUrl: String
  val govUkVat484Form: String
  val vatAgentClientLookupFrontendUrl: String
  val agentClientLookupAgentAction: String
  def agentClientLookupUrl: String
  def agentClientUnauthorisedUrl: String
  def languageMap: Map[String,Lang]
  val routeToSwitchLanguage: String => Call
  val accessibilityReportUrl: String
  val vatReturnPeriodFrontendUrl: String
  val btaBaseUrl: String
  val btaHomeUrl: String
  val btaAccountDetails: String
  val gtmContainer: String
  def addressLookUpConfirmUrl(id : String) : String
  val webChatUrl: String
}

@Singleton
class FrontendAppConfig @Inject()(implicit configuration: Configuration, servicesConfig: ServicesConfig, environment: Environment) extends AppConfig {

  private def getStringSeq(key: String): Seq[String] =
    configuration.getOptional[Seq[String]](key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  lazy val appName: String = "manage-vat-subscription-frontend"

  override val webChatUrl: String = servicesConfig.getString("webChat.baseUrl") + servicesConfig.getString("webChat.url")

  private lazy val contactHost: String = servicesConfig.getString(Keys.contactFrontendService)
  override lazy val contactFormServiceIdentifier: String = servicesConfig.getString(Keys.contactFrontendIdentifier)

  override lazy val contactFrontendService: String = servicesConfig.baseUrl("contact-frontend")

  override lazy val reportAProblemPartialUrl: String = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  override lazy val reportAProblemNonJSUrl: String = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  private lazy val signInBaseUrl: String = servicesConfig.getString(Keys.signInBaseUrl)

  override lazy val signInContinueBaseUrl: String = servicesConfig.getString(Keys.signInContinueBaseUrl)
  private lazy val signInContinueUrl: String =
    SafeRedirectUrl(signInContinueBaseUrl + controllers.routes.CustomerCircumstanceDetailsController.show.url).encodedUrl

  private lazy val signInOrigin = servicesConfig.getString(Keys.appName)
  override lazy val signInUrl: String = s"$signInBaseUrl?continue=$signInContinueUrl&origin=$signInOrigin"

  override lazy val vatSubscriptionUrl: String = servicesConfig.baseUrl(Keys.vatSubscription)

  override lazy val govUkCohoNameChangeUrl: String = servicesConfig.getString(Keys.govUkCohoNameChangeUrl)

  override lazy val govUkTrustNameChangeUrl: String = servicesConfig.getString(Keys.govUkTrustNameChangeUrl)

  override val features = new Features

  private lazy val surveyBaseUrl = servicesConfig.getString(Keys.surveyHost) + servicesConfig.getString(Keys.surveyUrl)
  override def surveyUrl(identifier: String): String = s"$surveyBaseUrl/$identifier"

  private lazy val governmentGatewayHost: String = servicesConfig.getString(Keys.governmentGatewayHost)

  override def signOutExitSurveyUrl(identifier: String): String =
    s"$governmentGatewayHost/bas-gateway/sign-out-without-state?continue=${surveyUrl(identifier)}"
  override lazy val unauthorisedSignOutUrl: String = s"$governmentGatewayHost/bas-gateway/sign-out-without-state?continue=$signInContinueUrl"

  override lazy val addressLookupUrlHost: String = servicesConfig.getString(Keys.addressLookupFrontendHost)

  override def addressLookupService: String = {
    if (features.stubAddressLookup()) {
      host + "/vat-through-software/account/test-only/address-lookup-stub"
    } else {
      servicesConfig.baseUrl(Keys.addressLookupFrontend)
    }
  }

  override def addressLookUpConfirmUrl(id : String): String = s"$addressLookupUrlHost/lookup-address/$id/confirm"

  override lazy val addressLookupCallbackUrl: String =
    signInContinueBaseUrl + servicesConfig.getString(Keys.addressLookupCallback)

  override lazy val agentServicesGovUkGuidance: String = servicesConfig.getString(Keys.govUkSetupAgentServices)

  override lazy val agentAuthoriseForClient: String = servicesConfig.getString(Keys.agentAuthoriseForClient) + "/agent-subscription/start"

  override lazy val bankAccountCoc: String = servicesConfig.baseUrl(Keys.bankAccountCoc)

  override lazy val btaUrl: String = servicesConfig.getString(Keys.businessTaxAccountHost) + "/business-account"
  override lazy val vatSummaryUrl: String = servicesConfig.getString("vat-summary-frontend.host") + "/vat-through-software/vat-overview"

  override lazy val countryCodeJson: JsValue = environment.resourceAsStream("country-codes.json") match {
    case Some(inputStream) => Json.parse(Source.fromInputStream(inputStream, "UTF-8").mkString)
    case _ => throw new Exception("Country codes file not found")
  }

  override lazy val host: String = servicesConfig.getString(Keys.host)

  override lazy val timeoutPeriod: Int = servicesConfig.getInt(Keys.timeoutPeriod)
  override lazy val timeoutCountdown: Int = servicesConfig.getInt(Keys.timeoutCountDown)

  override lazy val agentInvitationsFastTrack: String = servicesConfig.getString(Keys.agentInvitationsFastTrack)

  override lazy val feedbackUrl: String = s"$contactHost/contact/beta-feedback?service=$contactFormServiceIdentifier" +
    s"&backUrl=${SafeRedirectUrl(host + controllers.routes.CustomerCircumstanceDetailsController.show.url).encodedUrl}"

  private lazy val vatCorrespondenceHost = servicesConfig.getString(Keys.vatCorrespondenceFrontendHost)
  override lazy val vatCorrespondenceChangeEmailUrl: String = vatCorrespondenceHost +
    servicesConfig.getString(Keys.vatCorrespondenceChangeEmailUrl)
  override lazy val vatCorrespondenceChangeLandlineNumberUrl: String =  vatCorrespondenceHost +
    servicesConfig.getString(Keys.vatCorrespondenceChangeLandlineNumberUrl)
  override lazy val vatCorrespondenceChangeMobileNumberUrl: String = vatCorrespondenceHost +
    servicesConfig.getString(Keys.vatCorrespondenceChangeMobileNumberUrl)
  override lazy val vatCorrespondenceChangeWebsiteUrl: String = vatCorrespondenceHost +
    servicesConfig.getString(Keys.vatCorrespondenceChangeWebsiteUrl)
  override lazy val vatCorrespondenceFixYourEmail: String = vatCorrespondenceHost +
    servicesConfig.getString(Keys.vatCorrespondenceFixYourEmail)

  private lazy val vatDesignatoryDetailsHost = servicesConfig.getString(Keys.vatDesignatoryDetailsFrontendHost)
  override lazy val vatDesignatoryDetailsTradingNameUrl: String = vatDesignatoryDetailsHost +
    servicesConfig.getString(Keys.vatDesignatoryDetailsNewTradingNameUrl)
  override lazy val vatDesignatoryDetailsBusinessNameUrl: String = vatDesignatoryDetailsHost +
    servicesConfig.getString(Keys.vatDesignatoryDetailsNewBusinessNameUrl)

  override def partyTypes: Seq[String] = getStringSeq(Keys.partyTypesOrgNameRow)

  override val partyTypesNspItmpOrSAMastered: Seq[String] = getStringSeq(Keys.partyTypesNspItmpOrSAMastered)

  override val partyTypesTrusts: Seq[String] = getStringSeq(Keys.partyTypesTrusts)

  override lazy val govUkChangeToBusinessDetails: String = servicesConfig.getString(Keys.changeToBusinessDetailsUrl)

  override lazy val govUkChangeVatRegistrationDetails: String = servicesConfig.getString(Keys.changeVatRegistrationDetails)

  override lazy val govUkSoftwareGuidanceUrl: String = servicesConfig.getString(Keys.softwareGuidanceUrl)
  override lazy val govUkVat484Form: String = servicesConfig.getString(Keys.vat484Form)

  override lazy val vatAgentClientLookupFrontendUrl: String =
    servicesConfig.getString(Keys.vatAgentClientLookupFrontendHost) + servicesConfig.getString(Keys.vatAgentClientLookupFrontendUrl)

  def vatAgentClientLookupHandoff(redirectUrl: String): String =
    vatAgentClientLookupFrontendUrl + s"/client-vat-number?redirectUrl=${SafeRedirectUrl(servicesConfig.getString(Keys.host) + redirectUrl).encodedUrl}"

  override def agentClientLookupUrl: String =
    if (features.stubAgentClientLookup()) {
      testOnly.controllers.routes.StubAgentClientLookupController.show(controllers.routes.CustomerCircumstanceDetailsController.show.url).url
    } else {
      vatAgentClientLookupHandoff(controllers.routes.CustomerCircumstanceDetailsController.show.url)
    }

  override val agentClientLookupAgentAction: String =
    servicesConfig.getString(ConfigKeys.vatAgentClientLookupFrontendHost) +
    servicesConfig.getString(ConfigKeys.vatAgentClientLookupFrontendUrl) +
    servicesConfig.getString(ConfigKeys.vatAgentClientLookupFrontendClientAccount)

  def vatAgentClientLookupUnauthorised(redirectUrl: String): String =
    vatAgentClientLookupFrontendUrl + s"/unauthorised-for-client?redirectUrl=${SafeRedirectUrl(servicesConfig.getString(Keys.host) + redirectUrl).encodedUrl}"

  override def agentClientUnauthorisedUrl: String =
    if (features.stubAgentClientLookup()) {
      testOnly.controllers.routes.StubAgentClientLookupController.unauth(controllers.routes.CustomerCircumstanceDetailsController.show.url).url
    } else {
      vatAgentClientLookupUnauthorised(controllers.routes.CustomerCircumstanceDetailsController.show.url)
    }

  override def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  override val routeToSwitchLanguage: String => Call = (lang: String) => controllers.routes.LanguageController.switchToLanguage(lang)

  override lazy val accessibilityReportUrl: String = servicesConfig.getString(Keys.accessibilityReportUrl)

  private lazy val vatReturnPeriodFrontendHost: String = servicesConfig.getString(ConfigKeys.vatReturnPeriodFrontendHost)
  override lazy val vatReturnPeriodFrontendUrl: String = vatReturnPeriodFrontendHost + servicesConfig.getString(ConfigKeys.vatReturnPeriodFrontendUrl)

  override lazy val btaBaseUrl: String = servicesConfig.baseUrl(Keys.businessTaxAccount)
  override lazy val btaHomeUrl: String =
    servicesConfig.getString(Keys.businessTaxAccountHost) + servicesConfig.getString(Keys.businessTaxAccountUrl)
  override lazy val btaAccountDetails: String =
    servicesConfig.getString(Keys.businessTaxAccountHost) + servicesConfig.getString(Keys.businessTaxAccountDetails)
  override val gtmContainer: String = servicesConfig.getString(Keys.gtmContainer)

}
