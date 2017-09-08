/*
 * Copyright 2017 HM Revenue & Customs
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

import javax.inject.{Inject, Singleton}

import play.api.Application
import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector => Auditing}
import uk.gov.hmrc.play.config.{AppName, RunMode, ServicesConfig}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.http.hooks.HttpHook

@Singleton
class FrontendAuditConnector @Inject()(val app: Application) extends Auditing with AppName {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

@Singleton
class WSHttp @Inject()(val app: Application) extends uk.gov.hmrc.play.http.ws.WSHttp with AppName with RunMode {
  override val hooks: Seq[AnyRef with HttpHook] = NoneRequired
}

@Singleton
class FrontendAuthConnector @Inject()(appConfig: AppConfig, val http: WSHttp) extends AuthConnector with ServicesConfig {
  lazy val serviceUrl: String = baseUrl("auth")
}
