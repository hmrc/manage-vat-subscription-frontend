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

package connectors.httpParsers

import assets.CircumstanceDetailsTestConstants._
import models.core.ErrorModel
import play.api.http.Status
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

class CustomerCircumstancesHttpParserSpec extends TestUtil {

  val successBadJson: JsObject = Json.obj("firstName" -> 1)
  val errorModel: ErrorModel = ErrorModel(Status.BAD_REQUEST, "Error Message")
  val parser: CustomerCircumstancesHttpParser = inject[CustomerCircumstancesHttpParser]

  "The CustomerDetailsHttpParser" when {

    "the http response status is OK with valid Json" should {

      "return a CustomerDetailsModel" in {
        parser.CustomerCircumstanceReads.read("", "",
          HttpResponse(Status.OK, customerInformationJsonMaxOrganisation, Map.empty[String, Seq[String]])) shouldBe
          Right(customerInformationModelMaxOrganisation)
      }
    }

    "the http response status is OK with invalid Json" should {

      "return an ErrorModel" in {
        parser.CustomerCircumstanceReads.read("", "",
          HttpResponse(Status.OK, successBadJson, Map.empty[String, Seq[String]])) shouldBe
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR,"Invalid Json"))
      }
    }

    "the http response status is BAD_REQUEST" should {

      "return an ErrorModel" in {
        parser.CustomerCircumstanceReads.read("", "",
          HttpResponse(Status.BAD_REQUEST, "")) shouldBe
          Left(ErrorModel(Status.BAD_REQUEST,"Downstream error returned when retrieving CustomerDetails"))
      }
    }

    "the http response status unexpected" should {

      "return an ErrorModel" in {
        parser.CustomerCircumstanceReads.read("", "",
          HttpResponse(Status.SEE_OTHER, "")) shouldBe
          Left(ErrorModel(Status.SEE_OTHER,"Downstream error returned when retrieving CustomerDetails"))
      }
    }
  }
}
