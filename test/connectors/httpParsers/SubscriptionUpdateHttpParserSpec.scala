/*
 * Copyright 2020 HM Revenue & Customs
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

import connectors.httpParsers.SubscriptionUpdateHttpParser.SubscriptionUpdateReads
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

class SubscriptionUpdateHttpParserSpec extends TestUtil {

  "SubscriptionUpdateHttpParser" when {

    "http response status is OK with valid json" should {

      val successJson = Json.obj("formBundle" -> "12345")
      val result = SubscriptionUpdateReads.read("", "",
        HttpResponse(Status.OK, successJson, Map.empty[String, Seq[String]]))

      "return SubscriptionUpdateResponseModel" in {
        result shouldBe Right(SubscriptionUpdateResponseModel("12345"))
      }
    }

    "http response status is OK with invalid json" should {

      val invalidJson = Json.obj("invalidKey" -> "12345")
      val result = SubscriptionUpdateReads.read("", "",
        HttpResponse(Status.OK, invalidJson, Map.empty[String, Seq[String]]))

      "return ErrorModel" in {
        result shouldBe Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Invalid Json"))
      }
    }

    "http response status is not OK" should {

      val result = SubscriptionUpdateReads.read("", "",
        HttpResponse(Status.INTERNAL_SERVER_ERROR, ""))

      "return ErrorModel" in {
        result shouldBe Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Downstream error returned when updating Subscription Details"))
      }
    }
  }
}
