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

import common.SessionKeys
import helpers.BaseIntegrationSpec
import helpers.IntegrationTestConstants._
import play.api.http.Status._

class HelloWorldPageISpec extends BaseIntegrationSpec {

  "Calling the helloWorld action" when {

    "the user is authenticated" should {

      "return 200 OK" in {

        given.user.isAuthenticated

        val result = get("/hello-world")

        result.status shouldBe OK
      }
    }

    "the user is not authorised" should {

      "return 403 (Forbidden)" in {

        given.user.isNotEnrolled

        val result = get("/hello-world")

        result.status shouldBe FORBIDDEN
      }
    }

    "the user is not authenticated" should {

      "return 401 (Unauthorised)" in {

        given.user.isNotAuthenticated

        val result = get("/hello-world")

        result.status shouldBe UNAUTHORIZED
      }
    }


    "the agent is authorised" should {

      "return 200 OK" in {

        given.agent.isAgentAuthorised

        val result = get("/hello-world", Map(SessionKeys.CLIENT_VRN -> clientVRN))

        result.status shouldBe OK
      }
    }

    "the agent is not authorised" should {

      "return 500 (ISE)" in {

        given.agent.isAgentNotEnrolledToAsAgent

        val result = get("/hello-world")

        result.status shouldBe INTERNAL_SERVER_ERROR
      }
    }
  }
}
