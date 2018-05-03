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

import connectors.httpParsers.CustomerDetailsHttpParser.CustomerDetailsReads
import models.core.ErrorModel
import models.customerInfo.{CustomerDetailsModel, CustomerInformationModel, MTDfBMandated}
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

class CustomerDetailsHttpParserSpec extends TestUtil {


  val successJson = Some(Json.obj(
    "mandationStatus" -> "MTDfB Mandated",
    "customerDetails" -> Json.obj(
      "organisationName" -> "Ancient Antiques",
      "firstName" -> "Fred",
      "lastName" -> "Flintstone",
      "tradingName" -> "a"
    )
  ))
  val successModel = CustomerInformationModel(MTDfBMandated, CustomerDetailsModel(Some("Fred"), Some("Flintstone"), Some("Ancient Antiques"), Some("a")))

  val successBadJson = Some(Json.parse("{}"))
  val errorModel = ErrorModel(Status.BAD_REQUEST, "Error Message")

  "The CustomerDetailsHttpParser" when {

    "the http response status is OK with valid Json" should {

      "return a CustomerInformationModel" in {
        CustomerDetailsReads.read("", "", HttpResponse(Status.OK, successJson)) shouldBe Right(successModel)
      }

    }

    "the http response status is OK with invalid Json" should {

      "return an ErrorModel" in {
        CustomerDetailsReads.read("", "", HttpResponse(Status.OK, successBadJson)) shouldBe
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR,"Invalid Json"))
      }

    }

    "the http response status is BAD_REQUEST" should {

      "return an ErrorModel" in {
        CustomerDetailsReads.read("", "", HttpResponse(Status.BAD_REQUEST, None)) shouldBe
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR,"Invalid Json"))
      }

    }

    "the http response status unexpected" should {

      "return an ErrorModel" in {
        CustomerDetailsReads.read("", "", HttpResponse(Status.SEE_OTHER, None)) shouldBe
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR,"Invalid Json"))
      }

    }
  }



}
