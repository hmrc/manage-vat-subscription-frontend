/*
 * Copyright 2023 HM Revenue & Customs
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

object CustomerCircumstanceDetailsPageMessages extends BaseMessages {

  val title: String = "Your business details" + titleSuffixUser
  val heading = "Your business details"
  val agentTitle: String = "Your client’s VAT details" + titleSuffixAgent
  val agentHeading= "Your client’s business details"

  val individualNameHeading = "Individual name"
  val organisationNameHeading = "Business name"
  val businessAddressHeading = "Principal place of business"
  val tradingNameHeading = "Trading name"
  val bankDetailsHeading = "Bank account for repayments only"
  val accountNumberHeading = "Account number"
  val accountNumberEnding = "ending with"
  val emailAddressHeading = "Email address"
  val sortcodeHeading = "Sort code"
  val sortcodeStarting = "starting with"
  val returnFrequencyHeading = "VAT Return dates"
  val landlineNumberHeading = "Landline number"
  val mobileNumberHeading = "Mobile number"
  val websiteAddressHeading = "Website address"

  val bannerTitle = "Important"

  val add = "Add"
  val change = "Change"
  val pending = "Pending"

  val unverifiedEmailNudge =  "Your email address test@test.com is not working. Fix this now"
  val warning = "Warning"
  val contactDetailsMovedToBTA = "You can update contact preferences and details for VAT and other taxes in your account details."

  val aboutHeading = "Business details"
  val contactDetailsHeading = "Contact details"
  val returnDetailsHeading = "Return details"
  val changeBusinessHidden: String => String = name => s"Change the business name from $name"
  val changeBusinessAddressHidden: String => String = address => s"Change the business address from $address"
  val pendingBusinessAddressHidden = "Change to business address is pending"
  val changeTradingNameHidden: String => String = name => s"Change the trading name from $name"
  val changeBankDetailsHidden = "Change the bank account for repayments"
  val changeReturnFrequencyHidden: String => String = dates => s"Change the VAT Return dates from $dates"
  val changeEmailAddressHidden: String => String = email => s"Change the business email address from $email"
  val changeEmailAddressAgentHidden: String = "You are not permitted to change your client’s email address"
  val pendingBankDetailsHidden = "Change to bank account for repayments is pending"
  val pendingReturnFrequencyHidden = "Change to VAT Return dates is pending"
  val pendingEmailAddressHidden = "Change to business email address is pending"
  val pendingTradingNameHidden = "Change to trading name is pending"
  val pendingBusinessNameHidden = "Change to business name is pending"
  val changeLandlineNumbersHidden: String = "Change the landline number from 01234 567890"
  val pendingLandlineNumbersHidden: String = "Change to the landline number is pending"
  val changeMobileNumbersHidden: String = "Change the mobile number from 07700 123456"
  val pendingMobileNumbersHidden: String = "Change to the mobile number is pending"
  val changeWebsiteAddressHidden: String => String = website => s"Change the website address from $website"
  val pendingWebsiteAddressHidden: String = "Change to website address is pending"
  val futureDeregDateText: String => String = date => s"Your VAT cancellation date is set for $date"
  val pastDeregDateText: String => String = date => s"You successfully cancelled your VAT registration on $date"
  val oldChangeClientDetails = "You can change another client’s details."
  val newChangeClientDetails = "Change client"
  val deregText: String => String = date => s"VAT registration cancelled on on $date"
  val changeNotListed: String = "The change I want to make is not listed"
  val helpText: String = "You can make any other changes (opens in a new tab) using a form. " +
    "You must print and complete this form before posting it to HMRC."
  val backText: String = "Back"

  val newReturnDatesApplied: String = "You have asked to change the VAT Return dates. " +
    "The new dates will be shown when the change has been applied to this account."

  val niTraderStatusHeading = "Trading under Northern Ireland Protocol"
  val niTraderStatusLink = "Find out your Northern Ireland trading status (opens in new tab)"
  val niTraderStatusLinkAgent = "Find out your client’s Northern Ireland trading status (opens in new tab)"
  val niTraderStatusBodytext = "If your Northern Ireland trading status has changed you need to tell HMRC."
  val niTraderStatusBodytextAgent = "If your client’s Northern Ireland trading status has changed you need to tell HMRC."
}
