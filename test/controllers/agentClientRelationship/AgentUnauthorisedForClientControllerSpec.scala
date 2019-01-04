/*
 * Copyright 2019 HM Revenue & Customs
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

import assets.messages.{AgentUnauthorisedForClientPageMessages => Messages}
import audit.mocks.MockAuditingService
import audit.models.AuthenticateAgentAuditModel
import config.ServiceErrorHandler
import controllers.ControllerBaseSpec
import mocks.MockAuth
import org.jsoup.Jsoup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import play.api.http.Status
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import assets.BaseTestConstants._

import scala.concurrent.ExecutionContext

class AgentUnauthorisedForClientControllerSpec extends ControllerBaseSpec with MockAuth with MockAuditingService {

  object TestUnauthorisedForClientController extends AgentUnauthorisedForClientController(
    mockAgentOnlyAuthPredicate,
    app.injector.instanceOf[ServiceErrorHandler],
    mockAuditingService,
    mockConfig,
    messagesApi
  )

  "Calling the .show action" when {

    "A Clients VRN is in session" should {

      lazy val result = TestUnauthorisedForClientController.show(fakeRequestWithClientsVRN)
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 200" in {
        mockAgentAuthorised()
        status(result) shouldBe Status.OK

        val expectedAuditModel = AuthenticateAgentAuditModel(arn, vrn, isAuthorisedForClient = false)

        verify(mockAuditingService)
          .extendedAudit(
            ArgumentMatchers.eq(expectedAuditModel),
            ArgumentMatchers.eq[Option[String]](Some(controllers.agentClientRelationship.routes.ConfirmClientVrnController.show().url))
          )(
            ArgumentMatchers.any[HeaderCarrier],
            ArgumentMatchers.any[ExecutionContext]
          )
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }

      "render the Unauthorised for client Vrn Page" in {
        document.select("h1").text shouldBe Messages.pageHeading
      }
    }

    "A Clients VRN is NOT held in session" should {

      lazy val result = TestUnauthorisedForClientController.show(request)
      lazy val document = Jsoup.parse(bodyOf(result))

      "return 303 (SEE_OTHER)" in {
        mockAgentAuthorised()
        status(result) shouldBe Status.SEE_OTHER
      }

      s"redirect to ${controllers.agentClientRelationship.routes.SelectClientVrnController.show().url}" in {
        redirectLocation(result) shouldBe Some(controllers.agentClientRelationship.routes.SelectClientVrnController.show().url)
      }
    }

    unauthenticatedCheck(TestUnauthorisedForClientController.show)
  }
}
