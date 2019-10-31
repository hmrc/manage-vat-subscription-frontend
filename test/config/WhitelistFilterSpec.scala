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

import org.jsoup.Jsoup
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{Application, Configuration}
import utils.TestUtil

class WhitelistFilterSpec extends TestUtil {

  val controllerRoute: String = controllers.routes.ChangeBusinessNameController.show().url

  override implicit lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(Configuration(
        "whitelist.enabled" -> true
      )).build()

  "WhitelistFilter" when {

    "supplied with a non-whitelisted IP" should {

      lazy val fakeRequest = FakeRequest("GET", controllerRoute).withHeaders(
        "True-Client-IP" -> "127.0.0.2"
      )

//      Call(fakeRequest.method, fakeRequest.uri)

      lazy val Some(result) = route(app, fakeRequest)

      "return status of 303" in {
        status(result) shouldBe 303
      }

      "redirect to shutter page" in {
        redirectLocation(result) shouldBe Some(mockConfig.shutterPage)
      }
    }

    "supplied with a whitelisted IP" should {

      lazy val fakeRequest = FakeRequest("GET", controllerRoute).withHeaders(
        "True-Client-IP" -> "127.0.0.1"
      )

      lazy val Some(result) = route(app, fakeRequest)

      "return status of 401" in {
        status(result) shouldBe 401
      }

      "return the session timeout page" in {
        Jsoup.parse(bodyOf(result)).select("h1").text() shouldBe "Your session has timed out"
      }
    }
  }
}