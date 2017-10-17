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

package forms.test

import models.test.MoneyInputModel
import play.api.data.Form
import play.api.data.Forms._


import scala.util.{Failure, Success, Try}

object MoneyInputForm {

  val stringToBigDecimal: String => BigDecimal = (input) => Try(BigDecimal(input.trim)) match {
    case Success(value) => value
    case Failure(_) => BigDecimal(0)
  }

  val bigDecimalToString: BigDecimal => String = (input) => input.scale match {
    case 1 => input.setScale(2).toString()
    case _ => input.toString
  }

  val mandatoryCheck: String => Boolean = input => input.trim != ""

  val form: Form[MoneyInputModel] = Form(
    mapping(
      "moneyInput" -> text
        .verifying("Not there", mandatoryCheck)
        .transform(stringToBigDecimal, bigDecimalToString)
    )(MoneyInputModel.apply)(MoneyInputModel.unapply)
  )
}
