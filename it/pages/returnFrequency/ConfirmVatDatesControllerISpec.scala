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
import helpers.IntegrationTestConstants.clientVRN
import pages.BasePageISpec
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.OK

class ConfirmVatDatesControllerISpec extends BasePageISpec {

  val path = "/confirm-vat-return-dates"

  "Calling ConfirmVatDatesController.show" when {

    def sessionWithReturnFrequency: Option[(String, String)] => Map[String, String] =
      _.fold(Map.empty[String, String])(x =>
        Map(
          SessionKeys.CLIENT_VRN -> x._1,
          SessionKeys.RETURN_FREQUENCY -> x._2
        )
      )

    def show(sessionVrn: String, returnFrequency: String): WSResponse = get(path, sessionWithReturnFrequency(Some(sessionVrn, returnFrequency)))

    "A valid ReturnPeriod is returned" should {

      "render the page" in {
        given.user.isAuthenticated

        When("I call to show the Customer Circumstances page")
        val res = show(clientVRN, Jan)

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
        // TODO - Tuesday's job
      }

    }
  }

}
