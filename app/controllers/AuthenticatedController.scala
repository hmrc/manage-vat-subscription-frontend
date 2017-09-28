/*
 * Copyright 2017 HM Revenue & Customs
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

import javax.inject.{Inject, Singleton}

import auth.AuthPredicate.{AuthPredicate, Success}
import auth.AuthPredicates.predicates
import auth.User
import play.api.mvc.{Action, AnyContent, Request, Result}
import services.AuthService
import uk.gov.hmrc.auth.core.NoActiveSession
import uk.gov.hmrc.auth.core.retrieve.Retrievals._
import uk.gov.hmrc.play.frontend.controller.FrontendController
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthenticatedController @Inject()(authService: AuthService) extends FrontendController {

  type ActionBody = Request[AnyContent] => User => Future[Result]
  type AuthenticatedAction = ActionBody => Action[AnyContent]

  val async: AuthenticatedAction = asyncAction(predicates)

  def asyncAction(predicate: AuthPredicate)(action: ActionBody): Action[AnyContent] = {

    Action.async { implicit request =>

      authService.authorised().retrieve(allEnrolments) { enrolments =>

        val user = User(enrolments)

        predicate.apply(request)(user) match {
          case Right(Success) => action(request)(user)
          case Left(failure) => failure
        }
      }.recoverWith {
        case _: NoActiveSession => Future.successful(Redirect(controllers.routes.SessionTimeoutController.timeout()))
      }
    }
  }
}
