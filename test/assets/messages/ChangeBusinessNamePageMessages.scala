/*
 * Copyright 2020 HM Revenue & Customs
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

object ChangeBusinessNamePageMessages extends BaseMessages {

  val title = "Change the business name" + titleSuffixUser
  val heading = "Change the business name"


  val p1: String => String = name => s"The business name is currently $name. You’ll be taken to Companies House to change it."
  val p2 = "Changing the business name costs:"
  val bullet1 = "£8 to update within 2 working days"
  val bullet2 = "£30 to update on the same day"
  val bullet3 = "print out this form (opens in a new tab)"
  val bullet4 = "add your new business name in section 1"
  val bullet5 = "return the form to us with your new Certificate of Incorporation"
  val p3 = "Companies House will send a new Certificate of Incorporation when the details have changed."
  val p4 = "When you have changed your business name at Companies House and received your new Certificate of Incorporation, you will need to:"
  val continueLink = "Continue to Companies House (opens in a new tab)"

}
