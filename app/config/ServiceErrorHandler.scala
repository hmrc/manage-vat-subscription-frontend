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

package config

import javax.inject.Inject
import play.api.i18n.MessagesApi
import play.api.mvc.Results.InternalServerError
import play.api.mvc.{RequestHeader, Result}
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.frontend.http.FrontendErrorHandler
import views.html.errors.StandardError
import scala.concurrent.{ExecutionContext, Future}

class ServiceErrorHandler @Inject()(standardError: StandardError,
                                    val messagesApi: MessagesApi,
                                    implicit val appConfig: AppConfig)(implicit val ec: ExecutionContext) extends FrontendErrorHandler {

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)
                                    (implicit request: RequestHeader): Future[Html] = {
    Future.successful(standardError(pageTitle, heading, message))
  }

  def showInternalServerError(implicit request: RequestHeader): Future[Result] = {
    internalServerErrorTemplate.map(InternalServerError(_))
  }

}
