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

package helpers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, TestSuite}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.data.Form
import play.api.http.HeaderNames
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.{Application, Environment, Mode}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSRequest, WSResponse}
import stubs.AuthStub
import uk.gov.hmrc.play.test.UnitSpec

trait BaseIntegrationSpec extends UnitSpec with WireMockHelper with GuiceOneServerPerSuite with TestSuite
  with BeforeAndAfterEach with BeforeAndAfterAll {

  val mockHost: String = WireMockHelper.host
  val mockPort: String = WireMockHelper.wmPort.toString
  val appContextRoute: String = "/vat-through-software/account"

  lazy val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
  implicit lazy val messages: Messages = Messages(Lang("en-GB"), messagesApi)

  class PreconditionBuilder {
    implicit val builder: PreconditionBuilder = this

    def user: User = new User()
    def agent: Agent = new Agent()
  }

  def given: PreconditionBuilder = new PreconditionBuilder

  class User()(implicit builder: PreconditionBuilder) {
    def isAuthenticated: PreconditionBuilder = {
      AuthStub.authorised()
      builder
    }

    def isNotAuthenticated: PreconditionBuilder = {
      AuthStub.unauthorisedNotLoggedIn()
      builder
    }

    def isNotEnrolled: PreconditionBuilder = {
      AuthStub.unauthorisedOtherEnrolment()
      builder
    }

    def noAffinityGroup: PreconditionBuilder = {
      AuthStub.authorisedNoAffinityGroup()
      builder
    }
  }

  class Agent()(implicit builder: PreconditionBuilder) {
    def isSignedUpToAgentServices: PreconditionBuilder = {
      AuthStub.agentAuthorised()
      builder
    }

    def isNotSignedUpToAgentServices: PreconditionBuilder = {
      AuthStub.agentUnauthorisedOtherEnrolment()
      builder
    }

    def isUnauthorised: PreconditionBuilder = {
      AuthStub.insufficientEnrolments()
      builder
    }
  }

  def servicesConfig: Map[String, String] = Map(
    "play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck",
    "microservice.services.auth.host" -> mockHost,
    "microservice.services.auth.port" -> mockPort,
    "microservice.services.vat-subscription.host" -> mockHost,
    "microservice.services.vat-subscription.port" -> mockPort
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


  def get(path: String, additionalCookies: Map[String, String] = Map.empty): WSResponse = await(
    buildRequest(path, additionalCookies).get()
  )

  def post(path: String, additionalCookies: Map[String, String] = Map.empty)(body: Map[String, Seq[String]]): WSResponse = await(
    buildRequest(path, additionalCookies).post(body)
  )

  def buildRequest(path: String, additionalCookies: Map[String, String] = Map.empty): WSRequest =
    client.url(s"http://localhost:$port$appContextRoute$path")
      .withHeaders(HeaderNames.COOKIE -> SessionCookieBaker.bakeSessionCookie(additionalCookies), "Csrf-Token" -> "nocheck")
      .withFollowRedirects(false)

  def document(response: WSResponse): Document = Jsoup.parse(response.body)

  def redirectLocation(response: WSResponse): Option[String] = response.header(HeaderNames.LOCATION)

  def toFormData[T](form: Form[T], data: T): Map[String, Seq[String]] =
    form.fill(data).data map { case (k, v) => k -> Seq(v) }

}
