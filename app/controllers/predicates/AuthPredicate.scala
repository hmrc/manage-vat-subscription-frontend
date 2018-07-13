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

import common.EnrolmentKeys
import config.{AppConfig, ServiceErrorHandler}
import javax.inject.{Inject, Singleton}
import models.User
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{ActionBuilder, ActionFunction, Request, Result}
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core.retrieve.{Retrievals, ~}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthPredicate @Inject()(enrolmentsAuthService: EnrolmentsAuthService,
                              val messagesApi: MessagesApi,
                              val authenticateAsAgentWithClient: AuthoriseAsAgentWithClient,
                              val serviceErrorHandler: ServiceErrorHandler,
                              implicit val appConfig: AppConfig
                             ) extends FrontendController with AuthBasePredicate with I18nSupport with ActionBuilder[User] with ActionFunction[Request, User] {

  override def invokeBlock[A](request: Request[A], block: User[A] => Future[Result]): Future[Result] = {

    implicit val req: Request[A] = request
    enrolmentsAuthService.authorised().retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments) {
      case Some(affinityGroup) ~ allEnrolments =>
        (isAgent(affinityGroup), allEnrolments) match {
          case (true, enrolments) =>
            if (appConfig.features.agentAccess()) {
              checkAgentEnrolment(enrolments, block)(req)
            } else {
              Future.successful(Unauthorized(views.html.errors.agent.unauthorised()))
            }
          case (_, enrolments) => checkVatEnrolment(enrolments, block)
        }
      case _ =>
        Logger.warn("[AuthPredicate][invokeBlock] - Missing affinity group")
        Future.successful(serviceErrorHandler.showInternalServerError)
    } recover {
      case _: NoActiveSession =>
        Logger.debug("[AuthPredicate][invokeBlock] - No active session, rendering Session Timeout view")
        Unauthorized(views.html.errors.sessionTimeout())
      case _: AuthorisationException =>
        Logger.warn("[AuthPredicate][invokeBlock] - Unauthorised exception, rendering Unauthorised view")
        Forbidden(views.html.errors.unauthorised())
    }
  }


  private[AuthPredicate] def checkAgentEnrolment[A](enrolments: Enrolments, block: User[A] => Future[Result])(implicit request: Request[A]) =
    if (enrolments.enrolments.exists(_.key == EnrolmentKeys.agentEnrolmentId)) {
      Logger.debug("[AuthPredicate][checkAgentEnrolment] - Authenticating as agent")
      authenticateAsAgentWithClient.invokeBlock(request, block)
    }
    else {
      Logger.debug(s"[AuthPredicate][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment. Enrolments: $enrolments")
      Logger.warn(s"[AuthPredicate][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment.")
      Future.successful(Forbidden(views.html.errors.agent.unauthorised()))
    }

  private[AuthPredicate] def checkVatEnrolment[A](enrolments: Enrolments, block: User[A] => Future[Result])(implicit request: Request[A]) =
    if (enrolments.enrolments.exists(_.key == EnrolmentKeys.vatEnrolmentId)) {
      Logger.debug("[AuthPredicate][checkVatEnrolment] - Authenticated as principle")
      block(User(enrolments))
    }
    else {
      Logger.debug(s"[AuthPredicate][checkVatEnrolment] - Individual without HMRC-MTD-VAT enrolment. $enrolments")
      Future.successful(Forbidden(views.html.errors.not_signed_up()))
    }
}

