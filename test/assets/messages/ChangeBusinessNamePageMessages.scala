/*
 * Copyright 2019 HM Revenue & Customs
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

object ChangeBusinessNamePageMessages {

  val title = "Change the business name"
  val h1 = title

  val p1: String => String = name => s"""The business name is currently $name."""
  val p2 = "You will be taken to Companies House to change it. The business name will automatically update with HMRC too."
  val p3 = "Changing the business name costs:"
  val bullet1 = "£8 to update within 2 working days"
  val bullet2 = "£30 to update on the same day"
  val p4 = "Companies House will send you an email notification, a secure message and a new certificate when the details have changed."

  val link = "Continue to Companies House (opens in a new tab)"

}
