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

package controllers

import helpers.BaseIntegrationSpec
import play.api.test.FakeRequest
import play.api.http.Status._
import uk.gov.hmrc.play.test.UnitSpec

class HelloWorldControllerISpec extends UnitSpec with BaseIntegrationSpec {

  "Calling the helloWorld action" when {

    "the user is authenticated" should {

      "return 200 OK" in {

        given.user.isAuthenticated

        val controller = app.injector.instanceOf[HelloWorldController]
        val result = controller.helloWorld(FakeRequest())

        await(result).header.status shouldBe OK
      }
    }

    "the user is not authenticated" should {

      "return 303 SEE OTHER" in {

        given.user.isNotAuthenticated

        val controller = app.injector.instanceOf[HelloWorldController]
        val result = controller.helloWorld(FakeRequest())

        await(result).header.status shouldBe SEE_OTHER
      }
    }
  }
}
