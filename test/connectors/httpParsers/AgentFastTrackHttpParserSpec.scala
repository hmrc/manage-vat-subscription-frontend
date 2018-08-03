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

import connectors.httpParsers.AgentFastTrackHttpParser.AgentFastTrackReads
import models.core.ErrorModel
import play.api.http.HeaderNames.LOCATION
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

class AgentFastTrackHttpParserSpec extends TestUtil {

  val errorModel = ErrorModel(Status.BAD_REQUEST, "Error Message")

  "The InitialiseAddressLookupHttpParser" when {

    "the http response status is SEE_OTHER" when {

      "there is a redirect url" should {

        "return the LOCATION header has the redirect path" in {
          AgentFastTrackReads.read("", "", HttpResponse(Status.SEE_OTHER, None, Map(LOCATION -> Seq("redirect/url")))) shouldBe
            Right("redirect/url")
        }
      }
    }

    "the http response status is SEE_OTHER" when {

      "there is no redirect url" should {

        "return an ErrorModel" in {
          AgentFastTrackReads.read("", "", HttpResponse(Status.SEE_OTHER, None)) shouldBe
            Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Response Header did not contain location redirect"))
        }
      }
    }

    "the http response status is not SEE_OTHER" should {

      "return an ErrorModel" in {
        AgentFastTrackReads.read("", "", HttpResponse(Status.BAD_REQUEST, None)) shouldBe
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Downstream error returned from Agent Invitation Service"))
      }
    }
  }
}
