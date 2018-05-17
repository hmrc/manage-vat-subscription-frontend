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

package assets.messages

object ReturnFrequencyMessages {

  val title = "Changes in circumstances"
  val option1 = "January, April, July and October"
  val option2 = "February, May, August and November"
  val option3 = "March, June, September and December"
  val option4 = "Every month"

  object ChoosePage {
    val heading = "Choose the new VAT Return dates"
    val question = "The Vat Return dates are currently"
  }

  object ConfirmPage {
    val title = "Changes in circumstances"
    val heading = "Confirm the new VAT Return dates"
    val changeLink = "Change the VAT Return dates"
    val p2 = "By confirming this change, you agree that the information you have given is complete and correct."
  }

}
