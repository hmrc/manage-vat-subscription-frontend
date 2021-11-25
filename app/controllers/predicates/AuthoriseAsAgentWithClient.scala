/*
 * Copyright 2021 HM Revenue & Customs
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

import audit.AuditService
import common.{EnrolmentKeys, SessionKeys}
import config.{AppConfig, ServiceErrorHandler}
import javax.inject.{Inject, Singleton}
import models.User
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import utils.LoggerUtil

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthoriseAsAgentWithClient @Inject()(enrolmentsAuthService: EnrolmentsAuthService,
                                           val auditService: AuditService,
                                           val serviceErrorHandler: ServiceErrorHandler,
                                           override val mcc: MessagesControllerComponents,
                                           implicit val appConfig: AppConfig,
                                           implicit val executionContext: ExecutionContext)
  extends AuthBasePredicate(mcc) with I18nSupport with ActionBuilder[User, AnyContent] with ActionFunction[Request, User] with LoggerUtil {

  private def delegatedAuthRule(vrn: String): Enrolment =
    Enrolment(EnrolmentKeys.vatEnrolmentId)
      .withIdentifier(EnrolmentKeys.vatIdentifierId, vrn)
      .withDelegatedAuthRule(EnrolmentKeys.mtdVatDelegatedAuthRule)

  private val arn: Enrolments => String = _.enrolments.collectFirst {
    case Enrolment("HMRC-AS-AGENT", Seq(EnrolmentIdentifier(_, arnValue)), _ , _) => arnValue
  }.getOrElse(throw InternalError("Agent Service Enrolment Missing"))

  override val parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser
  override def invokeBlock[A](request: Request[A], block: User[A] => Future[Result]): Future[Result] = {
    implicit val req: Request[A] = request
    request.session.get(SessionKeys.mtdVatvcClientVrn) match {
      case Some(vrn) =>
        logger.debug(s"[AuthoriseAsAgentWithClient][invokeBlock] - Client VRN from Session: $vrn")
        enrolmentsAuthService.authorised(delegatedAuthRule(vrn)).retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments) {
          case None ~ _ =>
            Future.successful(serviceErrorHandler.showInternalServerError)
          case _ ~ allEnrolments =>
            val user = User(vrn, active = true, Some(arn(allEnrolments)))
            block(user)
        } recover {
          case _: NoActiveSession =>
            logger.debug(s"[AuthoriseAsAgentWithClient][invokeBlock] - Agent does not have an active session, redirect to GG Sign In")
            Redirect(appConfig.signInUrl)
          case _: AuthorisationException =>
            logger.warn(s"[AuthoriseAsAgentWithClient][invokeBlock] - Agent does not have delegated authority for Client")
            Redirect(appConfig.agentClientUnauthorisedUrl)
        }
      case _ =>
        logger.debug(s"[AuthoriseAsAgentWithClient][invokeBlock] - No Client VRN in session, redirecting to Select Client page")
        Future.successful(Redirect(appConfig.agentClientLookupUrl))
    }
  }
}
