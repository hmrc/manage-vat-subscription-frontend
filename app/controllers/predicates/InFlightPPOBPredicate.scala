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

package controllers.predicates

import config.{AppConfig, ServiceErrorHandler}
import javax.inject.Inject
import models.User
import models.circumstanceInfo.{CircumstanceDetails, PendingChanges}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results.Conflict
import play.api.mvc.{ActionRefiner, MessagesControllerComponents, Result}
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import views.html.errors.ChangePendingView

import scala.concurrent.{ExecutionContext, Future}

class InFlightPPOBPredicate @Inject()(customerCircumstancesService: CustomerCircumstanceDetailsService,
                                      val serviceErrorHandler: ServiceErrorHandler,
                                      changePendingView: ChangePendingView,
                                      val mcc: MessagesControllerComponents,
                                      implicit val appConfig: AppConfig,
                                      implicit val executionContext: ExecutionContext)
  extends ActionRefiner[User, User] with I18nSupport {

  override def messagesApi: MessagesApi = mcc.messagesApi

  override def refine[A](request: User[A]): Future[Either[Result, User[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))
    implicit val user: User[A] = request

    customerCircumstancesService.getCustomerCircumstanceDetails(user.vrn).map {
      case Right(circumstanceDetails) =>

        circumstanceDetails.pendingChanges match {
          case Some(changes) if changes.ppob.isDefined => comparePendingAndCurrent(changes, circumstanceDetails)
          case _ => Right(user)
        }

      case Left(error) => Logger.warn(s"[InflightPPOBPredicate][refine] - " +
        s"The call to the GetCustomerInfo API failed. Error: ${error.message}")
        Left(serviceErrorHandler.showInternalServerError)
    }
  }

  def comparePendingAndCurrent[A](pendingChanges: PendingChanges, circumstanceDetails: CircumstanceDetails)(implicit user: User[A]): Either[Result, User[A]] = {

      (circumstanceDetails.samePPOB, circumstanceDetails.sameEmail, circumstanceDetails.samePhone,
        circumstanceDetails.sameMobile, circumstanceDetails.sameWebsite) match {
        case (false, _, _, _, _) => Left(Conflict(changePendingView("changePending.ppob")))
        case (_, false, _, _, _) => Left(Conflict(changePendingView("changePending.email")))
        case (_, _, false, _, _) => Left(Conflict(changePendingView("changePending.landline")))
        case (_, _, _, false, _) => Left(Conflict(changePendingView("changePending.mobile")))
        case (_, _, _, _, false) => Left(Conflict(changePendingView("changePending.website")))
        case _ => Right(user)
      }
  }
}
