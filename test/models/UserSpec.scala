/*
 * Copyright 2023 HM Revenue & Customs
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

package models

import assets.BaseTestConstants.{testMtdVatEnrolment, vrn}
import uk.gov.hmrc.auth.core.{Enrolment, Enrolments, InternalError}
import utils.TestUtil

class UserSpec extends TestUtil {

  "The User model .apply" when {

    "there are valid enrolments" should {

      lazy val user = User(Enrolments(Set(testMtdVatEnrolment)))(request)

      "extract the VRN and return a User model" in {

        user shouldBe User(vrn)(request)
      }
    }

    "there are no valid enrolments" should {

      lazy val user = User(Enrolments(Set(Enrolment("").withIdentifier("", ""))))(request)

      "throw an exception" in {
        an[InternalError] should be thrownBy user
      }
    }
  }
}
