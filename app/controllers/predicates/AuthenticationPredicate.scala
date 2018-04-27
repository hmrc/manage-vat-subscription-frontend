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
import config.AppConfig
import models.User
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.auth.core.authorise.EmptyPredicate
import uk.gov.hmrc.auth.core.retrieve.Retrievals
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import services.EnrolmentsAuthService

import scala.concurrent.Future

@Singleton
class AuthenticationPredicate @Inject()(enrolmentsAuthService: EnrolmentsAuthService, implicit val messagesApi: MessagesApi, implicit val appConfig: AppConfig)
  extends FrontendController with I18nSupport with ActionBuilder[Request] with ActionFunction[Request,User] {


  override def invokeBlock[A](request: Request[A], f: (User[A]) => Future[Result]): Future[Result] = {

    implicit val req = request

    val predicate = if (appConfig.features.simpleAuth()) EmptyPredicate else Enrolment(EnrolmentKeys.vatEnrolmentId)

    enrolmentsAuthService.authorised(predicate).retrieve(Retrievals.authorisedEnrolments) {
      enrolments => {
        val user = if (appConfig.features.simpleAuth()) User("123456789") else User(enrolments)
        f(user)
      }
    } recoverWith {
      case _: NoActiveSession => Future.successful(Unauthorized(views.html.errors.sessionTimeout()))
      case _: AuthorisationException => Future.successful(Forbidden(views.html.errors.unauthorised()))
    }
  }


}