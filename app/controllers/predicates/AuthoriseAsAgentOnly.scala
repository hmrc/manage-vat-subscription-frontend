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

package controllers.predicates

import javax.inject.{Inject, Singleton}

import common.EnrolmentKeys
import config.{AppConfig, ServiceErrorHandler}
import models.AgentUser
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.{Retrievals, ~}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class AuthoriseAsAgentOnly @Inject()(enrolmentsAuthService: EnrolmentsAuthService,
                                     val messagesApi: MessagesApi,
                                     val serviceErrorHandler: ServiceErrorHandler,
                                     implicit val appConfig: AppConfig)
  extends FrontendController with AuthBasePredicate with I18nSupport with ActionBuilder[AgentUser] with ActionFunction[Request, AgentUser] {

  override def invokeBlock[A](request: Request[A], f: AgentUser[A] => Future[Result]): Future[Result] = {

    implicit val req = request

    if(appConfig.features.agentAccess()) {
      enrolmentsAuthService.authorised(CredentialStrength(CredentialStrength.strong)).retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments) {
        case Some(affinityGroup) ~ allEnrolments => {
          (isAgent(affinityGroup), allEnrolments) match {
            case (true, _) =>
              Logger.debug("[AuthoriseAsAgentOnly][invokeBlock] - Is an Agent, checking HMRC-AS-AGENT enrolment")
              checkAgentEnrolment(allEnrolments, f)
            case (_, _) =>
              Logger.debug("[AuthoriseAsAgentOnly][invokeBlock] - Is NOT an Agent, redirecting to Customer Details page")
              Future.successful(Redirect(controllers.routes.CustomerCircumstanceDetailsController.show()))
          }
        }
        case _ =>
          Logger.warn("[AuthoriseAsAgentOnly][invokeBlock] - Missing affinity group")
          Future.successful(serviceErrorHandler.showInternalServerError)
      } recover {
        case _: IncorrectCredentialStrength =>
          Logger.debug("[AuthoriseAsAgentOnly][invokeBlock] - Insufficient Credential Strength")
          Redirect(appConfig.signInUrl)
        case _: NoActiveSession =>
          Logger.debug("[AuthoriseAsAgentOnly][invokeBlock] - No Active Session, rendering Session Timeout view")
          Unauthorized(views.html.errors.sessionTimeout())
        case _: AuthorisationException =>
          Logger.warn("[AuthoriseAsAgentOnly][invokeBlock] - Authorisation Exception, rendering Unauthorised view")
          Forbidden(views.html.errors.unauthorised())
      }
    } else {
      Future.successful(Unauthorized(views.html.errors.agent.agent_journey_disabled()))
    }
  }

  private def checkAgentEnrolment[A](enrolments: Enrolments, f: AgentUser[A] => Future[Result])(implicit request: Request[A]) =
    if (enrolments.enrolments.exists(_.key == EnrolmentKeys.agentEnrolmentId)) {
      Logger.debug("[AuthoriseAsAgentOnly][checkAgentEnrolment] - Authenticated as agent")
      f(AgentUser(enrolments))
    }
    else {
      Logger.debug(s"[AuthoriseAsAgentOnly][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment. Enrolments: $enrolments")
      Logger.warn(s"[AuthoriseAsAgentOnly][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment")
      Future.successful(Forbidden(views.html.errors.agent.unauthorised()))
    }
}
