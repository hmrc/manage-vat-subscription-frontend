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

package controllers.agentClientRelationship

import assets.messages.{ConfirmClientVrnPageMessages => Messages}
import assets.CircumstanceDetailsTestConstants._
import common.SessionKeys
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.MockAuth
import mocks.services.MockCustomerCircumstanceDetailsService
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ConfirmClientVrnControllerSpec extends ControllerBaseSpec with MockAuth with MockCustomerCircumstanceDetailsService {

  object TestConfirmClientVrnControllerSpec extends ConfirmClientVrnController(
    messagesApi,
    mockAuthAsAgentWithClient,
    mockCustomerDetailsService,
    app.injector.instanceOf[ServiceErrorHandler],
    mockConfig
  )

  "Calling the .show action" when {

    "the user is an Agent" when {

      "the Agent is authorised and signed up to HMRC-AS-AGENT" when {

        "a Clients VRN is held in Session and details are successfully retrieved" should {

          lazy val result = TestConfirmClientVrnControllerSpec.show(fakeRequestWithClientsVRN)
          lazy val document = Jsoup.parse(bodyOf(result))

          "return 200" in {
            mockConfig.features.agentAccess(true)
            mockAgentAuthorised()
            mockCustomerDetailsSuccess(customerInformationModelMaxOrganisation)
            status(result) shouldBe Status.OK
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }

          "render the Confirm Client Vrn Page" in {
            document.select("h1").text shouldBe Messages.heading
          }
        }

        "a Clients VRN is held in Session and NO details are retrieved" should {

          lazy val result = TestConfirmClientVrnControllerSpec.show(fakeRequestWithClientsVRN)
          lazy val document = Jsoup.parse(bodyOf(result))

          "return 200" in {
            mockConfig.features.agentAccess(true)
            mockAgentAuthorised()
            mockCustomerDetailsError()
            status(result) shouldBe Status.INTERNAL_SERVER_ERROR
          }

          "return HTML" in {
            contentType(result) shouldBe Some("text/html")
            charset(result) shouldBe Some("utf-8")
          }
        }
      }
    }

    "the user is not authenticated" should {

      "return 401 (Unauthorised)" in {
        mockMissingBearerToken()
        val result = TestConfirmClientVrnControllerSpec.show(fakeRequestWithClientsVRN)
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }

  "Calling the .changeClient action" when {

    "the user is an Agent" when {

      "the Agent is authorised and signed up to HMRC-AS-AGENT" when {

        "a Clients VRN is held in Session" should {

          lazy val result = TestConfirmClientVrnControllerSpec.changeClient(fakeRequestWithVrnAndReturnFreq)
          lazy val document = Jsoup.parse(bodyOf(result))

          "return status redirect SEE_OTHER (303)" in {
            mockConfig.features.agentAccess(true)
            mockAgentAuthorised()
            status(result) shouldBe Status.SEE_OTHER
          }

          "redirect to the Select Your Client show action" in {
            redirectLocation(result) shouldBe Some(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
          }

          "have removed the Clients VRN from session" in {
            session(result).get(SessionKeys.CLIENT_VRN) shouldBe None
          }

          "have removed the ReturnFrequency from session" in {
            session(result).get(SessionKeys.RETURN_FREQUENCY) shouldBe None
          }
        }
      }
    }

    "the user is not authenticated" should {

      "return 401 (Unauthorised)" in {
        mockConfig.features.agentAccess(true)
        mockMissingBearerToken()
        val result = TestConfirmClientVrnControllerSpec.changeClient(fakeRequestWithClientsVRN)
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }

}
