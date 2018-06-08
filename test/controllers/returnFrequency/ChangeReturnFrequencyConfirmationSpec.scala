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

package controllers.returnFrequency

import assets.CircumstanceDetailsTestConstants._
import assets.messages.{ReturnFrequencyMessages => messages}
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.services.MockCustomerCircumstanceDetailsService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.Helpers._

class ChangeReturnFrequencyConfirmationSpec extends ControllerBaseSpec with MockCustomerCircumstanceDetailsService {

  object TestChangeReturnFrequencyConfirmation extends ChangeReturnFrequencyConfirmation(
    messagesApi,
    mockAuthPredicate,
    app.injector.instanceOf[ServiceErrorHandler],
    mockAppConfig
  )

  "Calling the .show action" when {

    "the user is authorised and a CustomerDetailsModel" should {

      lazy val result = TestChangeReturnFrequencyConfirmation.show(fakeRequest)
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
        status(result) shouldBe Status.OK
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the CustomerDetails Page" in {
        document.title shouldBe messages.title
      }
    }

    unauthenticatedCheck(TestChangeReturnFrequencyConfirmation.show)
  }
}
