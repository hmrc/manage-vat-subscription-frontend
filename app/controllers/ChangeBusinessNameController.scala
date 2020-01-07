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

import audit.AuditService
import audit.models.HandOffToCOHOAuditModel
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.businessName.ChangeBusinessNameView

import scala.concurrent.ExecutionContext

@Singleton
class ChangeBusinessNameController @Inject()(val authenticate: AuthPredicate,
                                             val customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                             val serviceErrorHandler: ServiceErrorHandler,
                                             val auditService: AuditService,
                                             changeBusinessNameView: ChangeBusinessNameView,
                                             val mcc: MessagesControllerComponents,
                                             implicit val appConfig: AppConfig,
                                             implicit val ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport {


  val show: Action[AnyContent] = authenticate.async {
    implicit user =>
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
        case Right(circumstances) if circumstances.customerDetails.organisationName.isDefined =>
          Ok(changeBusinessNameView(circumstances.customerDetails.organisationName.get))
        case _ => serviceErrorHandler.showInternalServerError
      }
  }


  val handOffToCOHO: Action[AnyContent] = authenticate.async {
    implicit user =>
      customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) map {
        case Right(circumstances) if circumstances.customerDetails.organisationName.isDefined =>
          auditService.extendedAudit(
            HandOffToCOHOAuditModel(user, circumstances.customerDetails.organisationName.get),
            Some(controllers.routes.ChangeBusinessNameController.show().url)
          )
          Redirect(appConfig.govUkCohoNameChangeUrl)
        case _ => serviceErrorHandler.showInternalServerError
      }
  }
}
