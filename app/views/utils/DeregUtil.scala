/*
 * Copyright 2023 HM Revenue & Customs
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

import models._
import models.circumstanceInfo.CircumstanceDetails
import utils.ImplicitDateFormatter._

object DeregUtil {

  def findDeregStatus(circs: CircumstanceDetails): DeregStatus = {
    if(circs.changeIndicators.exists(_.deregister)){
      PendingDereg
    } else {
      circs.deregistration.fold[DeregStatus](Registered)(
        _.cancellationDate.fold[DeregStatus](Registered)(
          cd => futureOrPast(cd.toLocalDate)))
    }
  }

  def futureOrPast(date: LocalDate): DeregStatus = {
    if(date.isAfter(currentDate)){
      FutureDereg(date)
    } else {
      PastDereg(date)
    }
  }

}
