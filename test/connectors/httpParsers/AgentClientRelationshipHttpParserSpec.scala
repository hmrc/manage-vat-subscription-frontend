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

package connectors.httpParsers

import connectors.httpParsers.AgentClientRelationshipHttpParser.AgentClientRelationshipCheckReads
import models.core.ErrorModel
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

class AgentClientRelationshipHttpParserSpec extends TestUtil {

  val errorModel = ErrorModel(Status.BAD_REQUEST, "Error Message")

  "The AgentClientRelationshipHttpParser" when {

    "the http response status is OK" should {

      "return true" in {
        AgentClientRelationshipCheckReads.read("", "", HttpResponse(Status.OK, None)) shouldBe Right(true)
      }
    }

    "the http response status is NOT_FOUND" should {

      "return an ErrorModel" in {
        AgentClientRelationshipCheckReads.read("", "", HttpResponse(Status.NOT_FOUND, None)) shouldBe Right(false)
      }
    }

    "the http response status is BAD_REQUEST" should {

      "return an ErrorModel" in {
        AgentClientRelationshipCheckReads.read("", "", HttpResponse(Status.BAD_REQUEST, None)) shouldBe
          Left(ErrorModel(Status.BAD_REQUEST, "Downstream error returned when retrieving agent -> client relationship status from agent-client-relationship"))
      }
    }
  }
}
