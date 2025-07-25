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


import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate

import javax.inject.{Inject, Singleton}
import models.User
import models.circumstanceInfo.CircumstanceDetails
import models.viewModels.AltChangeBusinessNameViewModel._
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.LoggingUtil
import views.html.businessName.{AltChangeBusinessNameView, ChangeBusinessNameView}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChangeBusinessNameController @Inject()(val authenticate: AuthPredicate,
                                             val customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                                             val serviceErrorHandler: ServiceErrorHandler,
                                             changeBusinessNameView: ChangeBusinessNameView,
                                             altChangeBusinessNameView: AltChangeBusinessNameView,
                                             val mcc: MessagesControllerComponents,
                                             implicit val appConfig: AppConfig,
                                             implicit val ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport with LoggingUtil {

  val show: Action[AnyContent] = authenticate.async { implicit user =>
    customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn) flatMap {
      case Right(circumstances) =>
        val baseAccess: Boolean = circumstances.customerDetails.organisationName.isDefined &&
                                  !circumstances.customerDetails.overseasIndicator &&
                                  circumstances.validPartyType

        (baseAccess, circumstances.customerDetails.nameIsReadOnly) match {
          case (true, Some(false)) =>
            Future.successful(Redirect(appConfig.vatDesignatoryDetailsBusinessNameUrl))
          case (true, Some(true)) if circumstances.nspItmpPartyType||circumstances.trustPartyType =>
            Future.successful(renderAltChangeBusinessView(circumstances))
          case (true, _) =>
            Future.successful(Ok(changeBusinessNameView(circumstances.customerDetails.organisationName.get)))
          case _ =>
            Future.successful(Redirect(controllers.routes.CustomerCircumstanceDetailsController.show))
        }
      case _ =>
        errorLog("[ChangeBusinessNameController][show] - failed to retrieve customer circumstances details")
        serviceErrorHandler.showInternalServerError
    }
  }

  def renderAltChangeBusinessView(circumstances: CircumstanceDetails)(implicit user: User[_],
                                                                      appConfig: AppConfig): Result = {
    if(circumstances.trustPartyType) {
      Ok(altChangeBusinessNameView(trustBusinessNameViewModel(circumstances)))
    } else {
      Ok(altChangeBusinessNameView(businessNameViewModel(circumstances)))
    }
  }
}
