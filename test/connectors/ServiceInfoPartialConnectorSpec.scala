/*
 * Copyright 2024 HM Revenue & Customs
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

package connectors

import controllers.ControllerBaseSpec
import models.{NavContent, NavLinks}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.{Future, TimeoutException}

class ServiceInfoPartialConnectorSpec extends ControllerBaseSpec {

  val httpClient: HttpClient = mock[HttpClient]
  val connector = new ServiceInfoPartialConnector(httpClient)(mockConfig)
  def mockNavLinksCall(result: Future[Option[NavContent]]): Any =
    when(httpClient.GET[Option[NavContent]](any(), any(), any())(any(), any(), any())).thenReturn(result)

  val navLinks: NavContent = NavContent(
    NavLinks("Home", "Hafan", "http://localhost:9999/home"),
    NavLinks("Account", "Crfrif", "http://localhost:9999/account"),
    NavLinks("Messages", "Negeseuon", "http://localhost:9999/messages", Some(1)),
    NavLinks("Help", "Cymorth", "http://localhost:9999/help")
  )

  "ServiceInfoPartialConnector" should {

    "generate the correct url" in {
      connector.btaUrl shouldBe "/business-account/partial/nav-links"
    }
  }

  ".getNavLinks" when {

    "a successful response is returned" should {

      "return the NavLinks model" in {
        mockNavLinksCall(Future.successful(Some(navLinks)))
        await(connector.getNavLinks()(hc, ec, req)) shouldBe Some(navLinks)
      }
    }

    "an exception is thrown when attempting to retrieve the BTA nav links" should {

      "return None" in {
        mockNavLinksCall(Future.failed(new TimeoutException("FAILURE")))
        await(connector.getNavLinks()(hc, ec, req)) shouldBe None
      }
    }

  }
}
