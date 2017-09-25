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

package auth

import auth.AuthPredicate.Success
import auth.AuthPredicates.{enrolledPredicate, timeoutPredicate}
import mocks.MockAppConfig
import org.scalatest.EitherValues
import org.scalatest.mockito.MockitoSugar
import play.api.inject.Injector
import play.api.test.FakeRequest
import uk.gov.hmrc.http.SessionKeys.{authToken, lastRequestTimestamp}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import play.api.test.Helpers._

class AuthPredicateSpec extends UnitSpec with WithFakeApplication with MockitoSugar with EitherValues {

  lazy val injector: Injector = fakeApplication.injector
  lazy val mockAppConfig = new MockAppConfig

  val userWithMtdVatEnrolment = User("enrolment")
  val blankUser = User("")

  "Timeout predicate" when {

    "lastRequestTimestamp is not set" should {
      lazy val predicate = timeoutPredicate(FakeRequest())(blankUser)

      "return Success" in {
        predicate.right.value shouldBe Success
      }
    }

    "authToken and lastRequestTimestamp are set" should {
      lazy val request = FakeRequest().withSession(
        authToken -> "",
        lastRequestTimestamp -> ""
      )
      lazy val predicate = timeoutPredicate(request)(blankUser)

      "return Success" in {
        predicate.right.value shouldBe Success
      }
    }

    "lastRequestTimestamp is set and authToken is not" should {
      lazy val request = FakeRequest().withSession(
        lastRequestTimestamp -> ""
      )
      lazy val predicate = timeoutPredicate(request)(blankUser)
      lazy val result = predicate.left.value

      "return 303" in {
        status(result) shouldBe 303
      }

      s"redirect to ${controllers.routes.SessionTimeoutController.timeout().url}" in {
        redirectLocation(result) shouldBe Some(controllers.routes.SessionTimeoutController.timeout().url)
      }
    }
  }

  "Enrolled predicate" when {

    "mtdVatId is not empty" should {
      lazy val predicate = enrolledPredicate(FakeRequest())(userWithMtdVatEnrolment)

      "return Success" in {
        predicate.right.value shouldBe Success
      }
    }

    "mtdVatId is empty" should {
      lazy val predicate = enrolledPredicate(FakeRequest())(blankUser)
      lazy val result = predicate.left.value

      "return 303" in {
        status(result) shouldBe 303
      }

      s"redirect to ${controllers.routes.HelloWorldController.helloWorld().url}" in {
        redirectLocation(result) shouldBe Some(controllers.routes.HelloWorldController.helloWorld().url)
      }
    }
  }
}
