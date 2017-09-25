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
 * WITHOUT WARRANTIED OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package auth

import auth.AuthPredicate.Success
import auth.AuthPredicates.{timeoutPredicate, timeoutRoute}
import config.AppConfig
import org.scalatest.mockito.MockitoSugar
import play.api.inject.Injector
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.http.SessionKeys.{authToken, lastRequestTimestamp}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

class AuthPredicateSpec extends UnitSpec with WithFakeApplication with MockitoSugar{

  lazy val injector: Injector = fakeApplication.injector
  lazy val mockConfig: AppConfig = injector.instanceOf[AppConfig]

  val userWithMtdVatEnrolment = User("enrolment")
  val blankUser = User("")

  lazy val authorisedRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    authToken -> "",
    lastRequestTimestamp -> ""
  )

  "timeoutPredicate" should {
    "return a Success where the lastRequestTimestamp is not set" in {
      timeoutPredicate(FakeRequest())(blankUser).right.value shouldBe Success
    }

    "return a Success where the authToken is set and the lastRequestTimestamp is set" in {
      timeoutPredicate(authorisedRequest)(blankUser).right.value shouldBe Success
    }

    "return the timeout page where the lastRequestTimestamp is set but the auth token is not" in {
      lazy val request = FakeRequest().withSession(lastRequestTimestamp -> "")
      await(timeoutPredicate(request)(blankUser).left.value) shouldBe timeoutRoute
    }
  }

}
