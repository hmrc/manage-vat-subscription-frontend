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

import connectors.httpParsers.InitialiseAddressLookupHttpParser.InitialiseAddressLookupReads
import models.core.ErrorModel
import models.customerAddress.AddressLookupOnRampModel
import play.api.http.HeaderNames.LOCATION
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtil

class InitialiseAddressLookupHttpParserSpec extends TestUtil {

  val errorModel = ErrorModel(Status.BAD_REQUEST, "Error Message")

  "The InitialiseAddressLookupHttpParser" when {

    "the http response status is ACCEPTED" when{

      "there is a redirect url" should {

        "return a AddressLookupOnRampModel" in {
          InitialiseAddressLookupReads.read("", "", HttpResponse(Status.ACCEPTED,None,Map(LOCATION -> Seq("redirect/url")))) shouldBe
            Right(AddressLookupOnRampModel("redirect/url"))
        }
      }
    }

    "the http response status is ACCEPTED" when {

      "there is no redirect url" should {

        "return an ErrorModel" in {
          InitialiseAddressLookupReads.read("", "", HttpResponse(Status.ACCEPTED,None)) shouldBe
            Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Response Header did not contain location redirect"))
        }
      }
    }

    "the http response status is not ACCEPTED" should {

      "return an ErrorModel" in {
        InitialiseAddressLookupReads.read("", "", HttpResponse(Status.BAD_REQUEST, None)) shouldBe
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Downstream error returned from Address Lookup"))
      }
    }
  }
}
