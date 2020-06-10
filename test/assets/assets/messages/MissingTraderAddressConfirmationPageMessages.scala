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

object MissingTraderAddressConfirmationPageMessages extends BaseMessages {
  val title: String = "You have confirmed the business address" + titleSuffixUser
  val titleAgent: String = "You have confirmed the business address" + titleSuffixAgent
  val h1: String = "You have confirmed the business address"
  val p1: String = "Thank you. We will try to deliver the letter again."
}
