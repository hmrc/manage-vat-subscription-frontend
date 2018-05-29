/*
 * Copyright 2018 HM Revenue & Customs
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

package controllers.agentClientRelationship

import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthoriseAsAgentOnly
import forms.ClientVrnForm
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class SelectClientVrnController @Inject()(val messagesApi: MessagesApi,
                                          val authenticate: AuthoriseAsAgentOnly,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async {
    implicit user =>
      Future.successful(Ok(views.html.agentClientRelationship.select_client_vrn(ClientVrnForm.form)))
  }

  val submit: Action[AnyContent] = authenticate.async {

    implicit user =>
      ClientVrnForm.form.bindFromRequest().fold(
        error => {
          Logger.debug(s"[SelectClientVrnController][submit] Error")
          Future.successful(BadRequest(views.html.agentClientRelationship.select_client_vrn(error)))
        },
        data => { // success path
          Logger.debug(s"[SelectClientVrnController][submit] Success")
          Future.successful(Redirect(controllers.agentClientRelationship.routes.ConfirmClientVrnController.show())
            .addingToSession(SessionKeys.CLIENT_VRN -> data.vrn))
        }
      )
  }
}
