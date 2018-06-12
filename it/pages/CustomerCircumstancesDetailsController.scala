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

package pages

import common.SessionKeys
import helpers.BaseIntegrationSpec
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._
import helpers.IntegrationTestConstants._
import stubs.VatSubscriptionStub

class CustomerCircumstancesDetailsController extends BaseIntegrationSpec {

//  "Calling the .show action" when {
//
//    def target(sessionVrn: Option[String] = None): WSResponse =
//      get("/change-business-details", sessionVrn.fold(Map.empty[String, String])(x => Map(SessionKeys.CLIENT_VRN -> x)))
//
//    "the user is an Agent" when {
//
//      "the Agent is signed up for HMRC-AS-AGENT (authorised)" when {
//
//        "a clients VRN is held in session cookie" when {
//
//          "a success response is received for the Customer Details" should {
//
//            "return 200 OK" in {
//              given.agent.isSignedUpToAgentServices
//              VatSubscriptionStub.getClientDetailsSuccess(clientVRN)(customerCircumstancesDetailsMax)
//              target(Some(clientVRN)).status shouldBe OK
//            }
//          }
//
//          "an error response is received for the Customer Details" should {
//
//            lazy val result = target(Some(clientVRN))
//
//            "return status ISE (500)" in {
//              given.agent.isSignedUpToAgentServices
//              VatSubscriptionStub.getClientDetailsError(clientVRN)
//              result.status shouldBe INTERNAL_SERVER_ERROR
//            }
//
//            "render the ISE page" in {
//              document(result).title shouldBe Messages("global.error.InternalServerError500.title")
//            }
//          }
//        }
//
//        "NO client VRN is held in the session cookie" should {
//
//          lazy val result = target()
//
//          "return redirect status SEE_OTHER (303)" in {
//            given.agent.isSignedUpToAgentServices
//            result.status shouldBe SEE_OTHER
//          }
//
//          "redirect location should be to the Select Client VRN view" in {
//            redirectLocation(result) shouldBe Some(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
//          }
//        }
//      }
//
//      "the Agent is not signed up for HMRC-AS-AGENT (not authorised)" should {
//
//        "return ISE (500)" in {
//          given.agent.isNotSignedUpToAgentServices
//          target(Some(clientVRN)).status shouldBe INTERNAL_SERVER_ERROR
//        }
//      }
//    }
//
//    "the user is a Principle Entity and not an Agent" should {
//
//      lazy val result = target()
//
//      "have a redirect status SEE_OTHER (303)" in {
//        given.user.isAuthenticated
//        result.status shouldBe SEE_OTHER
//      }
//
//      "have the redirect location header set to the Select Client Details page (this bounces them to Customer Details after)" in {
//        redirectLocation(result) shouldBe Some(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
//      }
//    }
//
//    "the user is timed out (not authenticated)" should {
//
//      lazy val result = target(Some(clientVRN))
//
//      "return status 401 - unauthorised" in {
//        given.user.isNotAuthenticated
//        result.status shouldBe UNAUTHORIZED
//      }
//
//      "render the session timeout view" in {
//        document(result).title shouldBe Messages("sessionTimeout.title")
//      }
//    }
//
//    "the user is logged in without an Affinity Group" should {
//
//      lazy val result = target(Some(clientVRN))
//
//      "return status 500 (ISE)" in {
//        given.user.noAffinityGroup
//        result.status shouldBe INTERNAL_SERVER_ERROR
//      }
//
//      "render the Internal Server Error page" in {
//        document(result).title shouldBe Messages("global.error.InternalServerError500.title")
//      }
//    }
//  }
}
