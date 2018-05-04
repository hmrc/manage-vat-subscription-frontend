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

import assets.messages.ChangeBusinessNamePageMessages
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._

class ChangeBusinessNameControllerSpec extends ControllerBaseSpec {

  object TestChangeBusinessNameController extends ChangeBusinessNameController(messagesApi, mockAuthPredicate, mockAppConfig)

  "Calling the .show action" when {

    "the user is authorised" should {

      lazy val result = TestChangeBusinessNameController.show(fakeRequest)

      "return OK (200)" in {
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      s"have the heading '${ChangeBusinessNamePageMessages.h1}'" in {
        Jsoup.parse(bodyOf(result)).select("h1").text shouldBe ChangeBusinessNamePageMessages.h1
      }
    }

    unauthenticatedCheck(TestChangeBusinessNameController.show)
  }
}
