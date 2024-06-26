/*
 * Copyright 2024 HM Revenue & Customs
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
  val para1 = "Until we accept that request, you cannot make a further change."
  val para2 = "HMRC accepts or rejects changes to VAT accounts within 2 working days."
  val btaLink = "Back to account details"
}
