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

object CustomerCircumstanceDetailsPageMessages extends BaseMessages {

  val title: String = "Your business details" + titleSuffixUser
  val heading = "Your business details"
  val agentTitle: String = "Your client’s VAT details" + titleSuffixAgent
  val agentHeading= "Your client’s VAT details"

  val individualNameHeading = "Individual name"
  val organisationNameHeading = "Business name"
  val businessAddressHeading = "Principal place of business"
  val bankDetailsHeading = "Bank account for repayments only"
  val accountNumberHeading = "Account number"
  val emailAddressHeading = "Email address"
  val sortcodeHeading = "Sort code"
  val returnFrequencyHeading = "VAT Return dates"
  val landlineNumberHeading = "Landline Number"
  val mobileNumberHeading = "Mobile Number"
  val websiteAddressHeading = "Website address"

  val change = "Change"
  val deregister = "Deregister"
  val pending = "Pending"
  val howToRegister = "How to register (opens in a new tab)"

  val registrationStatusHeading = "VAT registration"
  val aboutHeading = "About the business"
  val contactDetailsHeading = "Contact details"
  val mtdSectionHeading = "Making Tax Digital"
  val statusText = "Status"
  val registeredStatus = "Registered"
  val deregPending = "Deregistration requested"
  val futureDereg: String => String = date => s"Deregistration set for $date"
  val deregStatus: String => String = date => s"Deregistered on $date"
  val changeBusinessHidden: String => String = name => s"Change the business name from $name"
  val changeBusinessAddressHidden: String => String = address => s"Change the business address from $address"
  val pendingBusinessAddressHidden = "Change to business address is pending"
  val changeBankDetailsHidden = "Change the bank account for repayments"
  val changeReturnFrequencyHidden: String => String = dates => s"Change the VAT Return dates from $dates"
  val changeEmailAddressHidden: String => String = email => s"Change the business email address from $email"
  val changeEmailAddressAgentHidden: String = "You are not permitted to change your client’s email address"
  val pendingBankDetailsHidden = "Change to bank account for repayments is pending"
  val pendingReturnFrequencyHidden = "Change to VAT Return dates is pending"
  val pendingEmailAddressHidden = "Change to business email address is pending"
  val changeMtdStatusHidden: String = "Change the status from opted in"
  val pendingMtdStatusHidden: String = "Change to opt out of making tax digital for business is pending"
  val changeLandlineNumbersHidden: String = "Change the landline number"
  val pendingLandlineNumbersHidden: String = "Change to landline number is pending"
  val changeMobileNumbersHidden: String = "Change the mobile number"
  val pendingMobileNumbersHidden: String = "Change to mobile number is pending"
  val changeWebsiteAddressHidden: String => String = website => s"Change the website address from $website"
  val pendingWebsiteAddressHidden: String = "Change to website address is pending"
  val futureDeregDateText: String => String = date => s"Your deregistration for VAT is set for $date"
  val pastDeregDateText: String => String = date => s"You successfully deregistered for VAT on $date"
  val oldChangeClientDetails = "You can change another client’s details."
  val newChangeClientDetails = "Change client"
  val deregText: String => String = date => s"Deregistered on $date"
  val changeNotListed: String = "The change I want to make is not listed"
  val helpText: String = "You can make any other changes (opens in a new tab) using a form. You must print and complete this form before posting it to HMRC."
  val optOut: String = "Opt out"
  val optedIn: String = "Opted in"
  val optOutRequested: String = "Opt out requested"
}
