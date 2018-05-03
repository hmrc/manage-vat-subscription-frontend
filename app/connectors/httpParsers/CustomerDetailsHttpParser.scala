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

import models.core.ErrorModel
import models.customerInfo.CustomerInformationModel
import play.api.http.Status
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import play.api.Logger


object CustomerDetailsHttpParser extends ResponseHttpParser {

  implicit object CustomerDetailsReads extends HttpReads[HttpGetResult[CustomerInformationModel]] {

    override def read(method: String, url: String, response: HttpResponse): HttpGetResult[CustomerInformationModel] = {

      response.status match {
        case Status.OK => {
          Logger.debug("[CustomerDetailsHttpParser][read]: Status OK")
          response.json.validate[CustomerInformationModel].fold(
            invalid => {
              Logger.debug("[CustomerDetailsHttpParser][read]: Invalid Json")
              Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Invalid Json"))
            },
            valid => Right(valid)
          )
        }
        case Status.BAD_REQUEST =>
          Logger.debug("[CustomerDetailsHttpParser][read]: Bad Request Returned")
          handleErrorResponse(response)
        case _ =>
          Logger.debug("[CustomerDetailsHttpParser][read]: Unexpected Response")
          handleErrorResponse(response)

      }

    }

  }

}
