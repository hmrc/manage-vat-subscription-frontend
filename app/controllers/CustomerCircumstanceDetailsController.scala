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
import audit.models.ViewVatSubscriptionAuditModel
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.customerInfo.CustomerCircumstanceDetailsView

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerCircumstanceDetailsController @Inject()(val authenticate: AuthPredicate,
                                                      val customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                                      val serviceErrorHandler: ServiceErrorHandler,
                                                      val auditService: AuditService,
                                                      customerCircumstanceDetailsView: CustomerCircumstanceDetailsView,
                                                      val mcc: MessagesControllerComponents,
                                                      implicit val appConfig: AppConfig,
                                                      implicit val executionContext: ExecutionContext) extends FrontendController(mcc) with I18nSupport {

  val redirect: Action[AnyContent] = authenticate.async {
    implicit user =>
      Future.successful(Redirect(controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix)))
  }

  val show: String => Action[AnyContent] = _ => authenticate.async {
    implicit user =>
      Logger.debug(s"[CustomerCircumstanceDetailsController][show] User: ${user.vrn}")
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
        case Right(circumstances) =>
          auditService.extendedAudit(
            ViewVatSubscriptionAuditModel(user, circumstances),
            Some(controllers.routes.CustomerCircumstanceDetailsController.show(user.redirectSuffix).url)
          )
          Ok(customerCircumstanceDetailsView(circumstances))
            .removingFromSession(SessionKeys.NEW_RETURN_FREQUENCY,SessionKeys.CURRENT_RETURN_FREQUENCY)
        case _ =>
          Logger.debug(s"[CustomerCircumstanceDetailsController][show] Error Returned from Customer Details Service. Rendering ISE.")
          serviceErrorHandler.showInternalServerError
      }
  }
}
