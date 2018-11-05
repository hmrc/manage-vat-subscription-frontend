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

package controllers

import play.api.mvc.Result
import play.api.test.Helpers._

import scala.concurrent.Future

class SignOutControllerSpec extends ControllerBaseSpec {


  object TestSignOutController extends SignOutController(messagesApi, mockConfig)

  "navigating to signout page" when {

    "show Exit Survey is true" should {
      "return 303 and navigate to the survey url" in {
        lazy val result: Future[Result] = TestSignOutController.signOut(showExitSurvey = true)(request)

        status(result) shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(mockConfig.signOutExitSurveyUrl)
      }
    }

    "show Exit Survey is false" should {
      "return 303 and navigate to sign out url" in {
        lazy val result: Future[Result] = TestSignOutController.signOut(showExitSurvey = false)(request)

        status(result) shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(mockConfig.unauthorisedSignOutUrl)
      }
    }

    "timeout is true" should {
      "return 303 and navigate to timeout url" in {
        lazy val result: Future[Result] = TestSignOutController.signOut(showExitSurvey = false, timeout = true)(request)

        status(result) shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(mockConfig.signOutTimeoutUrl)
      }
    }
  }

  "navigating to sign-out" should {

    "return 401 unauthorised and render session timeout view" in {
      lazy val result: Future[Result] = TestSignOutController.timeout(request)

      status(result) shouldBe UNAUTHORIZED
    }
  }
}
