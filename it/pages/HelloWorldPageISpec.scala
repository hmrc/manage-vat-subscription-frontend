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

package pages

import helpers.BaseIntegrationSpec
import play.api.http.HeaderNames
import play.api.http.Status._
import uk.gov.hmrc.play.test.UnitSpec

class HelloWorldPageISpec extends UnitSpec with BaseIntegrationSpec {

  "Calling the helloWorld action" when {

    "the user is authenticated" should {

      "return 200 OK" in {

        given.user.isAuthenticated

        val result = await(buildRequest("/hello-world").get())

        result.status shouldBe OK
      }
    }

    "the user is not authorised" should {

      "return 403 (Forbidden)" in {

        given.user.isNotEnrolled

        val result = await(buildRequest("/hello-world").get())

        result.status shouldBe FORBIDDEN
      }
    }

    "the user is not authenticated" should {

      "return 401 (Unauthorised)" in {

        given.user.isNotAuthenticated

        val result = await(buildRequest("/hello-world").get())

        result.status shouldBe UNAUTHORIZED
      }
    }


    "the agent is authenticated" should {

      "return 200 OK" in {

        given.user.isAgentAuthenticated

        val result = await(buildRequest("/hello-world").get())

        result.status shouldBe OK
      }
    }

    "the agent is not authorised" should {

      "return 500 (ISE)" in {

        given.user.isAgentNotEnrolled

        val result = await(buildRequest("/hello-world").get())

        result.status shouldBe INTERNAL_SERVER_ERROR
      }
    }
  }
}
