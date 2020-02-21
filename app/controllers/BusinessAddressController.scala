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

package controllers

import audit.models.ContactPreferenceAuditModel
import audit.{AuditService, ContactPreferenceAuditKeys}
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.{AuthPredicate, InFlightPPOBPredicate}
import javax.inject.{Inject, Singleton}
import models.User
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import services.{AddressLookupService, ContactPreferenceService, CustomerCircumstanceDetailsService, PPOBService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.businessAddress.{ChangeAddressConfirmationView, ChangeAddressView}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BusinessAddressController @Inject()(val authenticate: AuthPredicate,
                                          val inFlightPPOBCheck: InFlightPPOBPredicate,
                                          addressLookupService: AddressLookupService,
                                          contactPreferenceService: ContactPreferenceService,
                                          ppobService: PPOBService,
                                          customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                          changeAddressView: ChangeAddressView,
                                          changeAddressConfirmationView: ChangeAddressConfirmationView,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          val auditService: AuditService,
                                          val mcc: MessagesControllerComponents,
                                          implicit val appConfig: AppConfig,
                                          implicit val ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport {

  val show: Action[AnyContent] = (authenticate andThen inFlightPPOBCheck).async { implicit user =>
    Future.successful(Ok(changeAddressView()))
  }

  val initialiseJourney: Action[AnyContent] = (authenticate andThen inFlightPPOBCheck).async { implicit user =>
    addressLookupService.initialiseJourney map {
      case Right(response) =>
        Redirect(response.redirectUrl)
      case Left(_) => Logger.warn(s"[BusinessAddressController][initialiseJourney] Error Returned from Address Lookup Service, Rendering ISE.")
        serviceErrorHandler.showInternalServerError
    }
  }

  val callback: String => Action[AnyContent] = id => authenticate.async { implicit user =>
    addressLookupService.retrieveAddress(id) flatMap {
      case Right(address) =>
        ppobService.updatePPOB(user, address, id) map {
          case Right(_) =>
            Redirect(controllers.routes.BusinessAddressController.confirmation(user.redirectSuffix))
          case Left(_) => Logger.warn(s"[BusinessAddressController][callback] Error Returned from PPOB Service, Rendering ISE.")
            serviceErrorHandler.showInternalServerError
        }
      case Left(_) => Logger.warn(s"[BusinessAddressController][callback] Error Returned from Address Lookup Service, Rendering ISE.")
        Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }

  val confirmation: String => Action[AnyContent] = _ => authenticate.async { implicit user =>
    if (user.isAgent) {
      val email = user.session.get(SessionKeys.verifiedAgentEmail)
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn).map {
        case Right(details) =>
          val entityName = details.customerDetails.clientName
          Ok(changeAddressConfirmationView(clientName = entityName, agentEmail = email))
        case Left(_) =>
          Ok(changeAddressConfirmationView(agentEmail = email))
      }
    } else {
      nonAgentConfirmation
    }
  }

  private def nonAgentConfirmation(implicit user: User[AnyContent]): Future[Result] = {
    for {
      cPref <- contactPreferenceService.getContactPreference(user.vrn) map {
        case Right(contact) =>
          auditService.extendedAudit(
            ContactPreferenceAuditModel(user.vrn, contact.preference, ContactPreferenceAuditKeys.changeBusinessAddressAction),
            Some(controllers.routes.ChangeBusinessNameController.show().url)
          )
          Some(contact.preference)
        case _ => None
      }
      verifiedEmail <- customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
        case Right(details) => details.ppob.contactDetails.exists(_.emailVerified contains true)
        case _ => false
      }
    } yield Ok(changeAddressConfirmationView(contactPref = cPref, emailVerified = verifiedEmail))
  }
}
