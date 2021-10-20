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

import common.{EnrolmentKeys, SessionKeys}
import config.{AppConfig, ServiceErrorHandler}
import javax.inject.{Inject, Singleton}
import models.User
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.{CustomerCircumstanceDetailsService, EnrolmentsAuthService}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import utils.LoggerUtil
import views.html.errors.NotSignedUpView
import views.html.errors.agent.UnauthorisedView

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthPredicate @Inject()(override val messagesApi: MessagesApi,
                              enrolmentsAuthService: EnrolmentsAuthService,
                              customerCircumstanceDetailsService: CustomerCircumstanceDetailsService,
                              val authenticateAsAgentWithClient: AuthoriseAsAgentWithClient,
                              val serviceErrorHandler: ServiceErrorHandler,
                              agentUnauthorisedView: UnauthorisedView,
                              notSignedUpView: NotSignedUpView,
                              override val mcc: MessagesControllerComponents)
                             (implicit val appConfig: AppConfig,
                              implicit val executionContext: ExecutionContext)
  extends AuthBasePredicate(mcc) with I18nSupport with ActionBuilder[User, AnyContent] with ActionFunction[Request, User] with LoggerUtil {

  override val parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser
  override def invokeBlock[A](request: Request[A], block: User[A] => Future[Result]): Future[Result] = {

    implicit val req: Request[A] = request
    enrolmentsAuthService.authorised().retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments) {
      case Some(affinityGroup) ~ allEnrolments =>
        (isAgent(affinityGroup), allEnrolments) match {
          case (true, enrolments) =>
            checkAgentEnrolment(enrolments, block)
          case (_, enrolments) =>
            checkVatEnrolment(enrolments, block)
        }
      case _ =>
        logger.warn("[AuthPredicate][invokeBlock] - Missing affinity group")
        Future.successful(serviceErrorHandler.showInternalServerError)
    } recover {
      case _: NoActiveSession =>
        logger.debug("[AuthPredicate][invokeBlock] - No active session, redirect to GG sign in")
        Redirect(appConfig.signInUrl)
      case _: AuthorisationException =>
        logger.warn("[AuthPredicate][invokeBlock] - Unauthorised exception, rendering Internal Server error page")
        serviceErrorHandler.showInternalServerError
    }
  }


  private[AuthPredicate] def checkAgentEnrolment[A](enrolments: Enrolments, block: User[A] => Future[Result])
                                                   (implicit request: Request[A]) =
    if (enrolments.enrolments.exists(_.key == EnrolmentKeys.agentEnrolmentId)) {
      logger.debug("[AuthPredicate][checkAgentEnrolment] - Authenticating as agent")
      authenticateAsAgentWithClient.invokeBlock(request, block)
    }
    else {
      logger.debug(s"[AuthPredicate][checkAgentEnrolment] - Agent without HMRC-AS-AGENT enrolment. Enrolments: $enrolments")
      Future.successful(Forbidden(agentUnauthorisedView()))
    }

  private[AuthPredicate] def checkVatEnrolment[A](enrolments: Enrolments, block: User[A] => Future[Result])
                                                 (implicit request: Request[A]) =
    if (enrolments.enrolments.exists(_.key == EnrolmentKeys.vatEnrolmentId)) {
      val user = User(enrolments)
      request.session.get(SessionKeys.insolventWithoutAccessKey) match {
        case Some("true") => Future.successful(Forbidden(notSignedUpView()))
        case Some("false") => block(user)
        case _ => customerCircumstanceDetailsService.getCustomerCircumstanceDetails(user.vrn).flatMap {
          case Right(details) if details.customerDetails.isInsolventWithoutAccess =>
            logger.debug("[AuthPredicate][checkVatEnrolment] - User is insolvent and not continuing to trade")
            Future.successful(Forbidden(notSignedUpView()).addingToSession(SessionKeys.insolventWithoutAccessKey -> "true"))
          case Right(_) =>
            logger.debug("[AuthPredicate][checkVatEnrolment] - Authenticated as principle")
            block(user).map(result => result.addingToSession(SessionKeys.insolventWithoutAccessKey -> "false"))
          case _ =>
            logger.warn("[AuthPredicate][checkVatEnrolment] - Failure obtaining insolvency status from Customer Info API")
            Future.successful(serviceErrorHandler.showInternalServerError(user))
        }
      }
    }
    else {
      logger.debug(s"[AuthPredicate][checkVatEnrolment] - Individual without HMRC-MTD-VAT enrolment. $enrolments")
      Future.successful(Forbidden(notSignedUpView()))
    }
}

