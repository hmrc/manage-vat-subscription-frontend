/*
 * Copyright 2022 HM Revenue & Customs
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

package controllers.missingTrader

import audit.AuditService
import audit.models.MissingTraderAuditModel
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import forms.MissingTraderForm
import javax.inject.Inject
import models.{No, Yes, YesNo}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CustomerCircumstanceDetailsService, PPOBService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.missingTrader.{ConfirmBusinessAddressView, MissingTraderAddressConfirmationView}

import scala.concurrent.{ExecutionContext, Future}

class ConfirmAddressController @Inject()(mcc: MessagesControllerComponents,
                                         authPredicate: AuthPredicate,
                                         customerDetailsService: CustomerCircumstanceDetailsService,
                                         errorHandler: ServiceErrorHandler,
                                         confirmBusinessAddressView: ConfirmBusinessAddressView,
                                         auditService: AuditService,
                                         missingTraderAddressConfirmationView: MissingTraderAddressConfirmationView,
                                         ppobService: PPOBService)
                                        (implicit appConfig: AppConfig,
                                         ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport {

  val form: Form[YesNo] = MissingTraderForm.missingTraderForm

  def show: Action[AnyContent] = authPredicate.async { implicit user =>

    user.session.get(SessionKeys.missingTraderConfirmedAddressKey) match {
      case Some("true") => Future.successful(Ok(missingTraderAddressConfirmationView()))
      case _ => customerDetailsService.getCustomerCircumstanceDetails(user.vrn).map {
        case Right(details) if details.missingTrader =>
          auditService.extendedAudit(MissingTraderAuditModel(user.vrn), Some(routes.ConfirmAddressController.show.url))
          Ok(confirmBusinessAddressView(details.ppobAddress, form))
        case Right(_) if user.isAgent => Redirect(appConfig.agentClientLookupAgentAction)
        case Right(_) => Redirect(appConfig.vatSummaryUrl)
        case Left(_) => errorHandler.showInternalServerError
      }
    }
  }

  def submit: Action[AnyContent] = authPredicate.async { implicit user => {
    form.bindFromRequest().fold(
      error => customerDetailsService.getCustomerCircumstanceDetails(user.vrn).map {
        case Right(details) => BadRequest(confirmBusinessAddressView(details.ppobAddress, error))
        case Left(_) => errorHandler.showInternalServerError
      },
      {
        case Yes =>
          ppobService.validateBusinessAddress(user.vrn).map {
            case Right(_) => Ok(missingTraderAddressConfirmationView()).addingToSession(SessionKeys.missingTraderConfirmedAddressKey -> "true")
            case Left(_) => errorHandler.showInternalServerError
          }
        case No => Future.successful(Redirect(controllers.routes.BusinessAddressController.initialiseJourney))
      }
    )
    }
  }
}
