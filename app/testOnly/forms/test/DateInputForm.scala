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

package testOnly.forms.test

import models.test.DateInputModel
import play.api.data.Form
import play.api.data.Forms._

import scala.util.{Failure, Success, Try}

object DateInputForm {

  lazy val form: Form[DateInputModel] = Form(
    mapping(
      "dateDay" -> text
        .verifying("Enter a day", day => day != "")
        .transform[Int](stringToInteger, _.toString),
      "dateMonth" -> text
        .verifying("Enter a month",month => month != "")
        .transform[Int](stringToInteger, _.toString),
      "dateYear" ->  text
        .verifying("Enter a year",year => year != "")
        .transform[Int](stringToInteger, _.toString)
    )(DateInputModel.apply)(DateInputModel.unapply)
  )

  val stringToInteger: String => Int = (input) => Try(input.trim.toInt) match {
    case Success(value) => value
    case Failure(_) => 0
  }
}
