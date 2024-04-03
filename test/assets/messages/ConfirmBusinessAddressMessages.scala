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

object ConfirmBusinessAddressMessages extends BaseMessages {

  val heading = "We’ve had a problem delivering mail to this address"
  val title: String = heading + titleSuffixUser
  val additionalInformation = "We cannot issue any payments on this account until you confirm the address."
  val question = "Is this still the company’s principal place of business?"
  val errorMessage = "Select yes if the business trades from this address most of the time"
  val yes = "Yes"
  val no = "No"
}
