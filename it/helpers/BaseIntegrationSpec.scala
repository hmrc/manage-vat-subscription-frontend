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

package helpers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, TestSuite}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.{Application, Environment, Mode}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSRequest, WSResponse}
import stubs.AuthStub

trait BaseIntegrationSpec extends WireMockHelper with GuiceOneServerPerSuite with TestSuite
  with BeforeAndAfterEach with BeforeAndAfterAll {

  val mockHost: String = WireMockHelper.host
  val mockPort: String = WireMockHelper.wmPort.toString
  val appContextRoute: String = "/manage-your-vat-account"

  class PreconditionBuilder {
    implicit val builder: PreconditionBuilder = this

    def user: User = new User()
  }

  def given: PreconditionBuilder = new PreconditionBuilder

  class User()(implicit builder: PreconditionBuilder) {
    def isAuthenticated: PreconditionBuilder = {
      AuthStub.stubAuthSuccess()
      builder
    }

    def isNotAuthenticated: PreconditionBuilder = {
      AuthStub.stubUnauthorised()
      builder
    }
  }

  def servicesConfig: Map[String, String] = Map(
    "microservice.services.auth.host" -> mockHost,
    "microservice.services.auth.port" -> mockPort
  )

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .in(Environment.simple(mode = Mode.Dev))
    .configure(servicesConfig)
    .build()

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWireMock()
  }

  override def afterAll(): Unit = {
    stopWireMock()
    super.afterAll()
  }

  def buildRequest(path: String): WSRequest = client.url(s"http://localhost:$port$appContextRoute$path").withFollowRedirects(false)

  def document(response: WSResponse): Document = Jsoup.parse(response.body)

}
