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

object ChangePendingMessages extends BaseMessages {
  val title: String = "There is already a change pending" + titleSuffixUser
  val heading = "There is already a change pending"
  val p1 = "A recent request was made to change the "
  val ppobChange = "principal place of business."
  val emailChange = "business’s email address."
  val landlineChange = "business’s landline number."
  val mobileChange = "business’s mobile number."
  val websiteChange = "business’s website address."
  val p2 = "Until this is confirmed, you cannot change the:"
  val listItem1 = "principal place of business"
  val listItem2 = "website address"
  val listItem3 = "contact details"
}
