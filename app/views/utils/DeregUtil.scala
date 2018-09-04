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

package views.utils

import java.time.LocalDate

import play.api.i18n.Messages
import models._
import models.circumstanceInfo.CircumstanceDetails
import play.twirl.api.Html
import utils.ImplicitDateFormatter._

object DeregUtil {

  def findDeregStatus(circs: CircumstanceDetails): DeregStatus = {
    if(circs.changeIndicators.exists(_.deregister)){
      PendingDereg
    } else if(circs.deregistration.map(_.cancellationDate).isDefined) {
      futureOrPast(circs.deregistration.get.cancellationDate.get.toLocalDate)
    } else {
      Registered
    }
  }

  def futureOrPast(date: LocalDate): DeregStatus = {
    if(date.isAfter(currentDate)){
      FutureDereg(date)
    } else {
      PastDereg(date)
    }
  }


  def deregMessage(status: DeregStatus)(implicit messages: Messages): String = {
    status match {
      case f:FutureDereg => messages("customer_details.registrationStatus.futureDereg", f.date.toLongDate)
      case d:PastDereg => messages("customer_details.registrationStatus.deregistered", d.date.toLongDate)
      case PendingDereg => messages("customer_details.registrationStatus.pending")
      case Registered => messages("customer_details.registrationStatus.registered")
    }
  }

  def deregChange(status: DeregStatus)(implicit messages: Messages, appConfig: config.AppConfig): Html = {
    status match {
      case Registered => Html(
        s"""
          |<a id="registration-status-link"
          |  href=${appConfig.deregisterForVat}
          |  aria-label="${messages("customer_details.registration.deregister.hidden")}">
          |  ${messages("customer_details.deregister")}
          |</a>
        """.stripMargin)
      case PendingDereg => Html(s"""<strong id="registration-status-link" class="bold">${messages("customer_details.pending")}</strong>""")
      case _ => Html(
        s"""
          |<a id="registration-status-link"
          |  href="https://www.gov.uk/vat-registration/how-to-register">
          |  ${messages("customer_details.howToRegister")}
          |</a>
        """.stripMargin)
    }
  }
}
