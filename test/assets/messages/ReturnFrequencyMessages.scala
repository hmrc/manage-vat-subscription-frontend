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
  val option1Jan = "January, April, July and October"
  val option2Feb = "February, May, August and November"
  val option3Mar = "March, June, September and December"
  val option4Monthly = "Every month"

  object ChoosePage {
    val heading = "Choose the new VAT Return dates"
    val question = "The VAT Return dates are currently"
    val error = "Select the new VAT Return dates"
  }

  object ConfirmPage {
    val heading = "Confirm the new VAT Return dates"
    val newDates = "The new VAT Return dates are"
    val changeLink = "Change the VAT Return dates"
    val p2 = "By confirming this change, you agree that the information you have given is complete and correct."
  }

  object ReceivedPage {
    val heading = "We have received the new VAT Return dates"
    val h2 = "What happens next"
    val p1 = "We will usually let you know if you can change these details within 2 working days."
    val p2 = "If this change is accepted:"
    val bullet1 = "we will send a confirmation letter to your business address"
    val bullet2 = "the business must submit this period's return before following the new VAT Return dates"
    val changeClientDetails = "You can change another client’s details."
  }
}
