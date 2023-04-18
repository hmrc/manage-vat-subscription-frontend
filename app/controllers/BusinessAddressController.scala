/*
 * Copyright 2023 HM Revenue & Customs
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

package controllers

import audit.models.{ChangeAddressStartAuditModel, ContactPreferenceAuditModel}
import audit.{AuditService, ContactPreferenceAuditKeys}
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.{AuthPredicate, InFlightPPOBPredicate}

import javax.inject.{Inject, Singleton}
import models.User
import models.core.AddressValidationError
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import services.{AddressLookupService, CustomerCircumstanceDetailsService, PPOBService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.LoggerUtil
import views.html.businessAddress.{ChangeAddressConfirmationView, ChangeAddressView}
import views.html.errors.PPOBAddressFailureView

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BusinessAddressController @Inject()(authenticate: AuthPredicate,
                                          inFlightPPOBCheck: InFlightPPOBPredicate,
                                          addressLookupService: AddressLookupService,
                                          ppobService: PPOBService,
                                          customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                          changeAddressView: ChangeAddressView,
                                          changeAddressConfirmationView: ChangeAddressConfirmationView,
                                          ppobAddressFailureView: PPOBAddressFailureView,
                                          serviceErrorHandler: ServiceErrorHandler,
                                          auditService: AuditService,
                                          mcc: MessagesControllerComponents)
                                         (implicit appConfig: AppConfig,
                                          ec: ExecutionContext) extends
  FrontendController(mcc) with I18nSupport with LoggerUtil {

  val show: Action[AnyContent] = (authenticate andThen inFlightPPOBCheck) { implicit user =>
    auditService.extendedAudit(ChangeAddressStartAuditModel(user), Some(routes.BusinessAddressController.show.url))
    Ok(changeAddressView())
  }

  val initialiseJourney: Action[AnyContent] = (authenticate andThen inFlightPPOBCheck).async { implicit user =>
    addressLookupService.initialiseJourney map {
      case Right(response) =>
        Redirect(response.redirectUrl)
      case Left(_) =>
        logger.warn("[BusinessAddressController][initialiseJourney] " +
          "Error Returned from Address Lookup Service, Rendering ISE.")
        serviceErrorHandler.showInternalServerError
    }
  }

  val callback: String => Action[AnyContent] = id => (authenticate andThen inFlightPPOBCheck).async { implicit user =>
    addressLookupService.retrieveAddress(id) flatMap {
      case Right(address) =>
        ppobService.updatePPOB(user, address, id) map {
          case Right(_) =>
            Redirect(routes.BusinessAddressController.confirmation)
              .addingToSession(SessionKeys.inFlightContactDetailsChangeKey -> "true")
          case Left(AddressValidationError) =>
            BadRequest(ppobAddressFailureView(id))
          case Left(_) =>
            logger.warn("[BusinessAddressController][callback] Error Returned from PPOB Service, Rendering ISE.")
            serviceErrorHandler.showInternalServerError
        }
      case Left(_) =>
        logger.warn("[BusinessAddressController][callback] Error Returned from Address Lookup Service, Rendering ISE.")
        Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }

  val confirmation: Action[AnyContent] = authenticate.async { implicit user =>
    if (user.isAgent) {
      val email = user.session.get(SessionKeys.mtdVatvcVerifiedAgentEmail)
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn).map {
        case Right(details) =>
          val entityName = details.customerDetails.clientName
          Ok(changeAddressConfirmationView(clientName = entityName, agentEmail = email))
        case Left(_) =>
          Ok(changeAddressConfirmationView(agentEmail = email))
      }
    } else {
      contactPrefRenderView
    }
  }

  private def contactPrefRenderView(implicit user: User[AnyContent]): Future[Result] =
    customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
      case Right(details) =>
        details.commsPreference match {
          case Some(contactPreference) =>
            auditService.extendedAudit(
              ContactPreferenceAuditModel(user.vrn, contactPreference, ContactPreferenceAuditKeys.changeBusinessAddressAction),
              Some(routes.ChangeBusinessNameController.show.url)
            )
            Ok(changeAddressConfirmationView(
              contactPref = Some(contactPreference),
              emailVerified = details.ppob.contactDetails.exists(_.emailVerified contains true)
            ))
          case None => Ok(changeAddressConfirmationView())
        }
      case Left (_) => Ok(changeAddressConfirmationView())
    }
}
