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

import config.ServiceErrorHandler

import javax.inject.Inject
import models.User
import play.api.mvc.Results.Redirect
import play.api.mvc.{ActionRefiner, Result}
import services.CustomerCircumstanceDetailsService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import utils.Converter.toFutureOfEither
import utils.LoggingUtil

import scala.concurrent.{ExecutionContext, Future}

class InFlightRepaymentBankAccountPredicate @Inject()(customerCircumstancesService: CustomerCircumstanceDetailsService,
                                                      serviceErrorHandler: ServiceErrorHandler)
                                                     (implicit val executionContext: ExecutionContext)
  extends ActionRefiner[User, User] with LoggingUtil {

  override def refine[A](request: User[A]): Future[Either[Result, User[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)
    implicit val user: User[A] = request

    if (!user.isAgent) {
      customerCircumstancesService.getCustomerCircumstanceDetails(user.vrn).map {
        case Right(circumstanceDetails) if circumstanceDetails.changeIndicators.fold(false)(_.bankDetails) =>
          Left(Future.successful(Redirect(controllers.routes.CustomerCircumstanceDetailsController.show)))
        case Right(_) => Right(Future.successful(user))
        case Left(error) =>
          warnLog(s"[InFlightRepaymentBankAccountPredicate][refine] - The call to the GetCustomerInfo API failed. Error: ${error.message}")
          Left(serviceErrorHandler.showInternalServerError)
      }.flatMap(toFutureOfEither)
    }
    else {
      Future(Left(Redirect(controllers.routes.CustomerCircumstanceDetailsController.show)))
    }
  }
}
