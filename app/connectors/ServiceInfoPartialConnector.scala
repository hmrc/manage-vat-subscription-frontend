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

import config.AppConfig
import models.NavContent
import play.api.mvc.Request
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import uk.gov.hmrc.http.HttpReads.Implicits._
import utils.LoggingUtil

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ServiceInfoPartialConnector @Inject()(http: HttpClient)
                                           (implicit config: AppConfig) extends LoggingUtil {

  lazy val btaUrl: String = config.btaBaseUrl + "/business-account/partial/nav-links"

  def getNavLinks()(implicit hc: HeaderCarrier, ec: ExecutionContext, request: Request[_]): Future[Option[NavContent]] = {
    http.GET[Option[NavContent]](btaUrl)
      .recover {
        case e =>
          warnLog(s"[ServiceInfoPartialConnector][getNavLinks] - Unexpected error: ${e.getMessage}")
          None
      }
  }
}
