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

import config.AppConfig
import controllers.predicates.AuthPredicate
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.LoggingUtil
import views.html.customerInfo.changeTradingUnderNiProtocol

import javax.inject.Inject
import scala.concurrent.Future

class NiTraderController @Inject()(authenticate: AuthPredicate,
                                   changeTradingUnderNiProtocol: changeTradingUnderNiProtocol,
                                   mcc: MessagesControllerComponents)
                                  (implicit appConfig: AppConfig) extends
  FrontendController(mcc) with I18nSupport with LoggingUtil  {

  def changeNiTradingStatus: Action[AnyContent] = authenticate.async { implicit request =>
    infoLog("[NiTraderController][changeNiTradingStatus] - loading the Change your Northern Ireland trading status page")
    Future.successful(Ok(changeTradingUnderNiProtocol()))
  }

}
