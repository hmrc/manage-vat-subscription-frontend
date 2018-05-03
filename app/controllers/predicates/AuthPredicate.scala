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

import config.AppConfig
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
                              val authenticateAsPrinciple: AuthoriseAsPrinciple,
                              val authenticateAsAgent: AuthoriseAsAgent,
                              implicit val appConfig: AppConfig
                             ) extends FrontendController with I18nSupport with ActionBuilder[Request] with ActionFunction[Request,User] {

  private def isAgent(group: AffinityGroup): Boolean = group.toString.contains("Agent")
  private def hasAgentEnrolment(allEnrols: Enrolments): Boolean = allEnrols.enrolments.exists(_.key == "HMRC-AS-AGENT")

  override def invokeBlock[A](request: Request[A], block: User[A] => Future[Result]): Future[Result] = {

    implicit val req = request

    enrolmentsAuthService.authorised().retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments) {
      case Some(affinityGroup) ~ allEnrolments =>
        (isAgent(affinityGroup), hasAgentEnrolment(allEnrolments)) match {
          case (true, true) => {
            Logger.info("[AuthPredicate][invokeBlock] - Authenticating as agent")
            authenticateAsAgent.authorise(request, block)
          }
          case (true, _) => {
            Logger.info(s"[AuthPredicate][invokeBlock] - Agent without HMRC-AS-AGENT enrolment\n\n $allEnrolments")
            Future(InternalServerError)
          }
          case _ => {
            Logger.info("[AuthPredicate][invokeBlock] - Authenticating as principle")
            authenticateAsPrinciple.authorise(request, block)
          }
        }
      case _ => {
        Logger.info("[AuthPredicate][invokeBlock] - Missing affinity group")
        Future(InternalServerError)
      }
    } recover {
      case _: NoActiveSession => Unauthorized(views.html.errors.sessionTimeout())
      case _: AuthorisationException => Forbidden(views.html.errors.unauthorised())
    }
  }
}