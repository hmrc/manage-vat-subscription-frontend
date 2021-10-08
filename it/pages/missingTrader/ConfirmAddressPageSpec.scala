/*
 * Copyright 2021 HM Revenue & Customs
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

package pages.missingTrader

import forms.MissingTraderForm
import helpers.IntegrationTestConstants.{VRN, customerCircumstancesDetailsMax, individual}
import pages.BasePageISpec
import play.api.http.Status
import play.api.libs.ws.WSResponse
import stubs.VatSubscriptionStub

class ConfirmAddressPageSpec extends BasePageISpec {

  "Calling the .show action" when {

    def show: WSResponse = get("/missing-trader")

    "the user is a missing trader" should {

      "render the Confirm Business Address view" in {

        given.user.isAuthenticated

        And("a valid Customer Information model is returned which indicates the user is a missing trader")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(individual))

        val res = show

        res should have(
          httpStatus(Status.OK),
          pageTitle("We’ve had a problem delivering mail to this address - Manage your VAT account - GOV.UK")
        )
      }
    }
  }

  "Calling the .submit action" when {

    def submit: WSResponse = post("/missing-trader")(Map(MissingTraderForm.yesNo -> Seq()))

    "there are errors in the form" should {

      "render the Confirm Business Address view with errors" in {

        given.user.isAuthenticated

        And("the user submits the form with errors")

        And("a valid Customer Information model is returned which indicates the user is a missing trader")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesDetailsMax(individual))

        val res = submit

        res should have(
          httpStatus(Status.BAD_REQUEST),
          pageTitle("Error: We’ve had a problem delivering mail to this address - Manage your VAT account - GOV.UK")
        )
      }
    }
  }
}
