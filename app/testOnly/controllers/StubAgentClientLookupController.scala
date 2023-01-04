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

package testOnly.controllers

import common.SessionKeys
import config.AppConfig
import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import testOnly.forms.StubAgentClientLookupForm
import testOnly.views.html.{StubAgentClientLookupView, StubAgentClientUnauthView}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

class StubAgentClientLookupController @Inject()(stubAgentClientLookupView: StubAgentClientLookupView,
                                                stubAgentClientUnauthView: StubAgentClientUnauthView,
                                                 implicit val mcc: MessagesControllerComponents,
                                                implicit val appConfig: AppConfig)
  extends FrontendController(mcc) with I18nSupport {

  def show(redirectUrl: String): Action[AnyContent] = Action { implicit request =>
    Ok(stubAgentClientLookupView(StubAgentClientLookupForm.form, redirectUrl))
  }

  def unauth(redirectUrl: String): Action[AnyContent] = Action { implicit request =>
    Ok(stubAgentClientUnauthView(redirectUrl))
      .removingFromSession(SessionKeys.mtdVatvcClientVrn)
  }

  def post: Action[AnyContent] = Action { implicit request =>
    StubAgentClientLookupForm.form.bindFromRequest().fold(
      err => InternalServerError(s"Failed to bind model:\n\nError: $err"),
      success => Redirect(success.redirectUrl)
        .addingToSession(SessionKeys.mtdVatvcClientVrn -> success.vrn, SessionKeys.viewedDDInterrupt -> "true")
    )
  }
}
