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

object CustomerCircumstanceDetailsPageMessages {

  val title = "Change of business details"
  val h1 = title
  val subheading = "Your VAT details"
  val agentSubheading = "Your client's VAT details"

  val individualNameHeading = "Individual name"
  val organisationNameHeading = "Business name"
  val businessAddressHeading = "Business address"
  val bankDetailsHeading = "Bank account for repayments only"
  val accountNumberHeading = "Account number"
  val sortcodeHeading = "Sort code"
  val returnFrequencyHeading = "VAT Return dates"

  val change = "Change"
  val changeBusinessHidden: String => String = name => s"Change the business name from $name"
  val changeBusinessAddressHidden: String => String = address => s"Change the business address from $address"
  val changeBankDetailsHidden = "Change the bank account for repayments"
  val changeReturnFrequencyHidden: String => String = dates => s"Change the VAT Return dates from $dates"

  val changeClientDetails = "You can change another client’s details."

}
