/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers.returnFrequency

import audit.AuditService
import audit.models.UpdateReturnFrequencyAuditModel
import cats.data.EitherT
import cats.instances.future._
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.{AuthPredicate, InFlightReturnFrequencyPredicate}
import javax.inject.{Inject, Singleton}
import models.User
import models.returnFrequency._
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{CustomerCircumstanceDetailsService, ReturnFrequencyService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.returnFrequency.ConfirmDatesView

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ConfirmVatDatesController @Inject()(val authenticate: AuthPredicate,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          returnFrequencyService: ReturnFrequencyService,
                                          val customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                          val auditService: AuditService,
                                          val pendingReturnFrequency: InFlightReturnFrequencyPredicate,
                                          confirmDatesView: ConfirmDatesView,
                                          val mcc: MessagesControllerComponents,
                                          implicit val appConfig: AppConfig,
                                          implicit val ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport {

  val show: Action[AnyContent] = (authenticate andThen pendingReturnFrequency) { implicit user =>
    user.session.get(SessionKeys.NEW_RETURN_FREQUENCY).fold {
      Logger.info("[ConfirmVatDatesController][show] No NEW_RETURN_FREQUENCY found in session. Redirecting to Choose Dates page")
      Redirect(controllers.returnFrequency.routes.ChooseDatesController.show().url)
    } { newReturnFrequency =>
      ReturnPeriod(newReturnFrequency) match {
        case Some(newFrequency) => Ok(confirmDatesView(newFrequency))
        case None => serviceErrorHandler.showInternalServerError
      }
    }
  }

  val submit: Action[AnyContent] = authenticate.async { implicit user =>

    (user.session.get(SessionKeys.CURRENT_RETURN_FREQUENCY), user.session.get(SessionKeys.NEW_RETURN_FREQUENCY)) match {
      case (Some(currentFrequency), Some(newFrequency)) =>
        updateReturnFrequency(ReturnPeriod(currentFrequency), ReturnPeriod(newFrequency))
      case (_, _) =>
        Logger.info("[ConfirmVatDatesController][submit] No NEW_RETURN_FREQUENCY and/or CURRENT_RETURN_FREQUENCY found in session." +
          "Redirecting to Choose Dates page")
        Future.successful(Redirect(controllers.returnFrequency.routes.ChooseDatesController.show().url))
    }
  }

  private def updateReturnFrequency(currentReturnPeriod: Option[ReturnPeriod],
                                    newReturnPeriod: Option[ReturnPeriod])(implicit user: User[AnyContent]): Future[Result] = {
    (currentReturnPeriod, newReturnPeriod) match {
      case (Some(currentPeriod), Some(newPeriod)) =>
        val circumstanceDetails = for {
          customerDetails <- EitherT(customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn))
          _ <- EitherT(returnFrequencyService.updateReturnFrequency(user.vrn, newPeriod))
        } yield customerDetails

        circumstanceDetails.value.map {
          case Right(details) =>
            auditService.extendedAudit(
              UpdateReturnFrequencyAuditModel(user, currentPeriod, newPeriod, details.partyType),
              Some(controllers.returnFrequency.routes.ConfirmVatDatesController.submit().url)
            )
            Redirect(controllers.returnFrequency.routes.ChangeReturnFrequencyConfirmation.show(if (user.isAgent) "agent" else "non-agent"))
              .removingFromSession(SessionKeys.NEW_RETURN_FREQUENCY, SessionKeys.CURRENT_RETURN_FREQUENCY)
          case _ => serviceErrorHandler.showInternalServerError
        }

      case _ =>
        Logger.warn("[ConfirmVatDatesController][updateReturnFrequency] CURRENT_RETURN_FREQUENCY and/or NEW_RETURN_FREQUENCY session keys are not valid")
        Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }
}
