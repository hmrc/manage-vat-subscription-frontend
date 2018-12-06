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

package controllers.predicates

import javax.inject.Inject

import config.{AppConfig, ServiceErrorHandler}
import models.User
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{ActionRefiner, Result}
import play.api.mvc.Results.Ok
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class InflightEmailPredicate @Inject()(customerCircumstancesService: CustomerCircumstanceDetailsService,
                                       val serviceErrorHandler: ServiceErrorHandler,
                                       val messagesApi: MessagesApi,
                                       implicit val appConfig: AppConfig,
                                       implicit val ec: ExecutionContext)
  extends ActionRefiner[User, User] with I18nSupport {

  override def refine[A](request: User[A]): Future[Either[Result, User[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))
    implicit val user: User[A] = request

    customerCircumstancesService.getCustomerCircumstanceDetails(user.vrn).map {

      case Right(circumstanceDetails) =>
        val currentEmail = circumstanceDetails.ppob.contactDetails.flatMap(_.emailAddress)
        val pendingEmail = circumstanceDetails.pendingEmail

        (currentEmail, pendingEmail) match {
          case (Some(current), Some(pending)) if current != pending => Left(Ok(views.html.errors.emailChangePending()))
          case _ => Right(user)
        }

      case Left(error) =>
        Logger.warn(s"[InflightEmailPredicate][refine] - " +
          s"The call to the GetCustomerInfo API failed. Error: ${error.message}")
        Left(serviceErrorHandler.showInternalServerError)
    }
  }
}