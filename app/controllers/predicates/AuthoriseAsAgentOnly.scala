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

package controllers.predicates

import common.EnrolmentKeys
import config.{AppConfig, ServiceErrorHandler}
import javax.inject.{Inject, Singleton}
import models.AgentUser
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import utils.LoggingUtil
import views.html.errors.agent.UnauthorisedView

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthoriseAsAgentOnly @Inject()(enrolmentsAuthService: EnrolmentsAuthService,
                                     val serviceErrorHandler: ServiceErrorHandler,
                                     unauthorisedView: UnauthorisedView,
                                     override val mcc: MessagesControllerComponents,
                                     implicit val appConfig: AppConfig,
                                     implicit val executionContext: ExecutionContext)
  extends AuthBasePredicate(mcc) with I18nSupport with ActionBuilder[AgentUser, AnyContent] with ActionFunction[Request, AgentUser] with LoggingUtil {

  override val parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser
  override def invokeBlock[A](request: Request[A], f: AgentUser[A] => Future[Result]): Future[Result] = {

    implicit val req: Request[A] = request

    enrolmentsAuthService.authorised().retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments) {
      case Some(affinityGroup) ~ allEnrolments => (isAgent(affinityGroup), allEnrolments) match {
        case (true, _) =>
          debug("[AuthoriseAsAgentOnly][invokeBlock] - Is an Agent, checking HMRC-AS-AGENT enrolment")
          checkAgentEnrolment(allEnrolments, f)
        case (_, _) =>
          debug("[AuthoriseAsAgentOnly][invokeBlock] - Is NOT an Agent, redirecting to Customer Details page")
          Future.successful(Redirect(controllers.routes.CustomerCircumstanceDetailsController.show))
        }

      case _ =>
        warnLog("[AuthoriseAsAgentOnly][invokeBlock] - Missing affinity group")
        serviceErrorHandler.showInternalServerError
    } recoverWith {
      case _: NoActiveSession =>
        debug("[AuthoriseAsAgentOnly][invokeBlock] - No Active Session, redirect to GG Sign In")
        Future.successful(Redirect(appConfig.signInUrl))
      case _: AuthorisationException =>
        warnLog("[AuthoriseAsAgentOnly][invokeBlock] - Authorisation Exception, rendering Internal Server Error view")
        serviceErrorHandler.showInternalServerError
    }
  }

  private def checkAgentEnrolment[A](enrolments: Enrolments, f: AgentUser[A] => Future[Result])(implicit request: Request[A]) =
    if (enrolments.enrolments.exists(_.key == EnrolmentKeys.agentEnrolmentId)) {
      debug("[AuthoriseAsAgentOnly][checkAgentEnrolment] - Authenticated as agent")
      f(AgentUser(enrolments))
    }
    else {
      debug(s"[AuthoriseAsAgentOnly][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment. Enrolments: $enrolments")
      warnLog(s"[AuthoriseAsAgentOnly][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment")
      Future.successful(Forbidden(unauthorisedView()))
    }
}
