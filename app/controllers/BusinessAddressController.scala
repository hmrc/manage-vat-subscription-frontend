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

package controllers

import audit.AuditService
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.{AuthPredicate, InflightEmailPredicate}
import javax.inject.{Inject, Singleton}
import common.SessionKeys
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.{AddressLookupService, CustomerCircumstanceDetailsService, PPOBService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class BusinessAddressController @Inject()(val messagesApi: MessagesApi,
                                          val authenticate: AuthPredicate,
                                          val inflightEmailCheck: InflightEmailPredicate,
                                          addressLookupService: AddressLookupService,
                                          ppobService: PPOBService,
                                          customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          val auditService: AuditService,
                                          implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = (authenticate andThen inflightEmailCheck).async { implicit user =>
    Future.successful(Ok(views.html.businessAddress.change_address()))
  }

  val initialiseJourney: Action[AnyContent] = (authenticate andThen inflightEmailCheck).async { implicit user =>
    addressLookupService.initialiseJourney map {
      case Right(response) =>
        Redirect(response.redirectUrl)
      case Left(_) => Logger.debug(s"[BusinessAddressController][initialiseJourney] Error Returned from Address Lookup Service, Rendering ISE.")
        serviceErrorHandler.showInternalServerError
    }
  }

  val callback: String => Action[AnyContent] = id => authenticate.async { implicit user =>
    addressLookupService.retrieveAddress(id) flatMap {
      case Right(address) =>
        ppobService.updatePPOB(user, address, id) map {
          case Right(_) =>
            Redirect(controllers.routes.BusinessAddressController.confirmation(user.redirectSuffix))
          case Left(_) => Logger.debug(s"[BusinessAddressController][callback] Error Returned from PPOB Service, Rendering ISE.")
            serviceErrorHandler.showInternalServerError
        }
      case Left(_) => Logger.debug(s"[BusinessAddressController][callback] Error Returned from Address Lookup Service, Rendering ISE.")
        Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }

  val confirmation: String => Action[AnyContent] = _ => authenticate.async { implicit user =>
    if(user.isAgent) {
      val email = user.session.get(SessionKeys.verifiedAgentEmail)
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn).map {
        case Right(details) =>
          val entityName = details.customerDetails.clientName
          Ok(views.html.businessAddress.change_address_confirmation(clientName = entityName, agentEmail = email))
        case Left(_) =>
          Ok(views.html.businessAddress.change_address_confirmation(agentEmail = email))
      }
    } else {
      Future.successful(Ok(views.html.businessAddress.change_address_confirmation()))
    }
  }
}
