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
import common.{EnrolmentKeys, SessionKeys}
import config.AppConfig
import models.User
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.auth.core.retrieve.{Retrievals, ~}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import services.EnrolmentsAuthService

import scala.concurrent.Future

@Singleton
class AuthoriseAsAgentWithClient @Inject()(enrolmentsAuthService: EnrolmentsAuthService,
                                           implicit val messagesApi: MessagesApi,
                                           implicit val appConfig: AppConfig) extends FrontendController with I18nSupport  {

  private def delegatedAuthRule(vrn: String): Enrolment =
    Enrolment(EnrolmentKeys.vatEnrolmentId)
      .withIdentifier(EnrolmentKeys.vatIdentifierId, vrn)
      .withDelegatedAuthRule(EnrolmentKeys.mtdVatDelegatedAuthRule)

  private val arn: Enrolments => Option[String] = _.getEnrolment(EnrolmentKeys.agentEnrolmentId) flatMap {
    _.getIdentifier(EnrolmentKeys.agentIdentifierId).map(_.value)
  }

  def authorise[A](implicit request: Request[A], f: User[A] => Future[Result]): Future[Result] =
    request.session.get(SessionKeys.CLIENT_VRN) match {
      case Some(vrn) =>
        enrolmentsAuthService.authorised(delegatedAuthRule(vrn)).retrieve(Retrievals.affinityGroup and Retrievals.authorisedEnrolments) {
          case _ ~ authorisedEnrolments =>
            f(User(vrn, active = true, arn(authorisedEnrolments)))
        } recover {
          case _: NoActiveSession => Unauthorized(views.html.errors.sessionTimeout())
          case _: AuthorisationException => Forbidden(views.html.errors.unauthorised())
        }
      case _ => Future.successful(Redirect(controllers.agentClientRelationship.routes.SelectClientVrnController.show()))
    }
}
