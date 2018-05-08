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

import java.util.Base64
import javax.inject.{Inject, Singleton}

import config.features.Features
import play.api.Mode.Mode
import play.api.mvc.Call
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.binders.ContinueUrl
import uk.gov.hmrc.play.config.ServicesConfig
import config.{ConfigKeys => Keys}


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
  val surveyUrl: String
  val signOutUrl: String
  val unauthorisedSignOutUrl: String
}

@Singleton
class FrontendAppConfig @Inject()(val runModeConfiguration: Configuration, environment: Environment) extends ServicesConfig with AppConfig {

  override protected def mode: Mode = environment.mode

  private lazy val contactHost: String = getString(Keys.contactFrontendService)
  private lazy val contactFormServiceIdentifier: String = "VATVC"

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

  private lazy val signInContinueBaseUrl: String = getString(Keys.signInContinueBaseUrl)
  private lazy val signInContinueUrl: String = ContinueUrl(signInContinueBaseUrl + controllers.routes.HelloWorldController.helloWorld().url).encodedUrl
  private lazy val signInOrigin = getString("appName")
  override lazy val signInUrl: String = s"$signInBaseUrl?continue=$signInContinueUrl&origin=$signInOrigin"

  lazy val vatSubscriptionUrl:String = baseUrl("vat-subscription")

  override lazy val govUkCohoNameChangeUrl: String = getString(Keys.govUkCohoNameChangeUrl)

  override val features = new Features(runModeConfiguration)


  private lazy val surveyBaseUrl = getString(Keys.surveyHost) + getString(Keys.surveyUrl)
  override lazy val surveyUrl = s"$surveyBaseUrl/?origin=$contactFormServiceIdentifier"

  private lazy val governmentGatewayHost: String = getString(Keys.governmentGatewayHost)

  override lazy val signOutUrl = s"$governmentGatewayHost/gg/sign-out?continue=$surveyUrl"
  override lazy val unauthorisedSignOutUrl: String = s"$governmentGatewayHost/gg/sign-out?continue=$signInContinueUrl"


}