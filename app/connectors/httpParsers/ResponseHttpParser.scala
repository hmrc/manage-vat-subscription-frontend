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
import play.api.Logger
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse

import scala.util.{Failure, Success, Try}

trait ResponseHttpParser {

  type HttpGetResult[T] = Either[ErrorModel, T]

  protected def handleErrorResponse(httpResponse: HttpResponse): Either[ErrorModel, Nothing] = {

    Left(Try(Json.parse(httpResponse.body)) match {
      case Success(json) => {
        Logger.debug("[ResponseHttpParser][handleErrorResponse]: Success")
        json.as[ErrorModel]
      }
      case Failure(_) => {
        Logger.debug("[ResponseHttpParser][handleErrorResponse]: Failure")
        ErrorModel(Status.INTERNAL_SERVER_ERROR,"Invalid Json")
      }
    })


  }

}
