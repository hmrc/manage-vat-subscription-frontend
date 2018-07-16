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

import javax.inject.{Inject, Singleton}

import audit.AuditService
import audit.models.AuthenticateAgentAuditModel
import common.SessionKeys
import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthoriseAsAgentOnly
import forms.ClientVrnForm
import models.User
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class AgentUnauthorisedForClientController @Inject()(val authenticate: AuthoriseAsAgentOnly,
                                                     val serviceErrorHandler: ServiceErrorHandler,
                                                     val auditService: AuditService,
                                                     implicit val appConfig: AppConfig,
                                                     implicit val messagesApi: MessagesApi) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async {
    implicit agent =>
      agent.session.get(SessionKeys.CLIENT_VRN) match {
        case Some(vrn) => {
          auditService.extendedAudit(
            AuthenticateAgentAuditModel(agent.arn, vrn, isAuthorisedForClient = false),
            Some(controllers.agentClientRelationship.routes.ConfirmClientVrnController.show().url)
          )
          Future.successful(Ok(views.html.errors.agent.notAuthorisedForClient()))
        }
        case _ =>
          Future.successful(Redirect(controllers.agentClientRelationship.routes.SelectClientVrnController.show()))
      }
  }
}
