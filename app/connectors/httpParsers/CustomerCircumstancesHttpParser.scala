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

package connectors.httpParsers

import connectors.httpParsers.ResponseHttpParser.HttpResult
import models.circumstanceInfo.CircumstanceDetails
import models.core.ErrorModel
import play.api.http.Status
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import utils.LoggingUtil

object CustomerCircumstancesHttpParser extends LoggingUtil {

  implicit object CustomerCircumstanceReads extends HttpReads[HttpResult[CircumstanceDetails]] {

    val expectedErrorStatuses: Seq[Int] = Seq(Status.NOT_FOUND)

    override def read(method: String, url: String, response: HttpResponse): HttpResult[CircumstanceDetails] = {
      implicit val res: HttpResponse = response
      response.status match {
        case Status.OK =>
          debug("[CustomerCircumstancesHttpParser][read]: Status OK")
          response.json.validate[CircumstanceDetails](CircumstanceDetails.reads).fold(
            invalid => {
              debug(s"[CustomerCircumstancesHttpParser][read]: Invalid Json - $invalid")
              warnLogRes(s"[CustomerCircumstancesHttpParser][read]: Invalid Json returned")
              Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Invalid Json"))
            },
            valid => Right(valid)
          )

        case status =>
          if(!expectedErrorStatuses.contains(status)) {
            warnLogRes(s"[CustomerCircumstancesHttpParser][read]: Unexpected Response, Status $status returned")
          }
          Left(ErrorModel(status,"Downstream error returned when retrieving CustomerDetails"))
      }
    }
  }
}
