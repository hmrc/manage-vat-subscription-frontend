/*
 * Copyright 2022 HM Revenue & Customs
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

package assets

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle.FULL
import java.util.Locale.UK

import models.circumstanceInfo.Deregistration

object DeregistrationTestConstants {

  def toLongDate(d: LocalDate): String = d.getDayOfMonth + " " + d.getMonth.getDisplayName(FULL, UK) + " " + d.getYear

  def formatDate(d: LocalDate): String = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

  val currentDate: LocalDate = LocalDate.now()
  val futureDate: LocalDate = LocalDate.now().plusDays(1)
  val pastDate: LocalDate = LocalDate.now().minusDays(1)

  val reason = "just coz"

  val deregModel = Deregistration(Some(reason), Some(formatDate(pastDate)), Some(formatDate(pastDate)))
  val futureDeregModel = Deregistration(Some(reason), Some(formatDate(futureDate)), Some(formatDate(futureDate)))

}
