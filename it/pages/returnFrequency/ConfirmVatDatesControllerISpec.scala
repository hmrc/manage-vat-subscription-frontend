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

package pages.returnFrequency

import assets.ReturnFrequencyIntegrationTestConstants._
import common.SessionKeys
import helpers.IntegrationTestConstants.VRN
import models.core.SubscriptionUpdateResponseModel
import pages.BasePageISpec
import play.api.http.Status.{SEE_OTHER, INTERNAL_SERVER_ERROR}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.OK
import stubs.ReturnFrequencyStub

class ConfirmVatDatesControllerISpec extends BasePageISpec {

  val path = "/confirm-vat-return-dates"
  val sessionWithReturnFrequency: Map[String, String] = Map(SessionKeys.CLIENT_VRN -> VRN, SessionKeys.RETURN_FREQUENCY -> Jan)

  "Calling ConfirmVatDatesController.show" when {

    def show(sessionVrn: String, returnFrequency: String): WSResponse = get(path, sessionWithReturnFrequency)

    "A valid ReturnPeriod is returned" should {

      "render the page" in {
        given.user.isAuthenticated

        When("I call to show the Customer Circumstances page")
        val res = show(VRN, Jan)

        res should have(
          httpStatus(OK),
          elementText("#page-heading")("Confirm the new VAT Return dates"),
          elementText("#p1")("The new VAT Return dates are " + MA)
        )
      }
    }
  }

  "Calling ConfirmVatDatesController.submit" when {

    "A valid ReturnPeriod is returned" should {

      "render the ChangeReturnFrequencyConfirmation page" in {
        given.user.isAuthenticated

        And("I stub a successful response from the Payments service")
        ReturnFrequencyStub.postSubscriptionSuccess(SubscriptionUpdateResponseModel("Good times"))

        When("I initiate a return frequency update journey")
        val res: WSResponse = postJSValueBody(
          "/confirm-vat-return-dates",
          sessionWithReturnFrequency
        )(Json.obj("body" -> "anything"))

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI(controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show().url)
        )
      }
    }

    "An invalid model is posted" should {

      "Render the Internal Server Error page" in {

        given.user.isAuthenticated

        And("I stub an error response from the Payments service")
        ReturnFrequencyStub.postSubscriptionError()

        When("I initiate a return frequency update journey")
        val res = postJSValueBody(
          "/confirm-vat-return-dates",
          sessionWithReturnFrequency
        )(Json.obj("body" -> "anything"))

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }

    }
  }

}
