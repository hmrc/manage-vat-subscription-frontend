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

package controllers

import audit.AuditService
import audit.models.ViewVatSubscriptionAuditModel
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{CustomerCircumstanceDetailsService, ServiceInfoService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.LoggingUtil
import views.html.customerInfo.CustomerCircumstanceDetailsView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerCircumstanceDetailsController @Inject()(authenticate: AuthPredicate,
                                                      customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                                      serviceErrorHandler: ServiceErrorHandler,
                                                      auditService: AuditService,
                                                      serviceInfoService: ServiceInfoService,
                                                      customerCircumstanceDetailsView: CustomerCircumstanceDetailsView,
                                                      mcc: MessagesControllerComponents)
                                                     (implicit appConfig: AppConfig,
                                                      executionContext: ExecutionContext) extends
  FrontendController(mcc) with I18nSupport with LoggingUtil {

  val show: Action[AnyContent] = authenticate.async {
    implicit user =>
      debug(s"[CustomerCircumstanceDetailsController][show] User: ${user.vrn}")
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) flatMap {
        case Right(circumstances) =>
          auditService.extendedAudit(
            ViewVatSubscriptionAuditModel(user, circumstances),
            Some(controllers.routes.CustomerCircumstanceDetailsController.show.url)
          )
          serviceInfoService.getPartial.map { result =>
            Ok(customerCircumstanceDetailsView(circumstances, result))
              .removingFromSession(SessionKeys.mtdVatvcNewReturnFrequency,SessionKeys.mtdVatvcCurrentReturnFrequency)
          }
        case _ =>
          debug(s"[CustomerCircumstanceDetailsController][show] Error Returned from Customer Details Service. Rendering ISE.")
          serviceErrorHandler.showInternalServerError
      }
  }
}
