/*
 * Copyright 2021 HM Revenue & Customs
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

object ChangeAddressConfirmationPageMessages extends BaseMessages {
  val title: String = "You have asked to change the business address" + titleSuffixUser
  val titleAgent: String = "You have asked to change the business address" + titleSuffixAgent
  val heading: String = "You have asked to change the business address"
  val h2: String = "What happens next"
  val p2: String = "We will usually update the VAT business address within 2 working days. " +
    "You will need to change the business address for other taxes separately."
  val p1Agent =
    "We’ll send an email to agentEmail@test.com within 2 working days telling you whether we can accept your request."
  val p2Agent: String = "We’ll contact MyCompany Ltd with an update."
  val p2AgentNoClientName: String = "We’ll contact your client with an update."
  val oldChangeClientDetails = "You can change another client’s details."
  val newChangeClientDetails = "Change client"
  val digiPrefEmailVerified: String =
    "We’ll send you an email with an update or you can check your HMRC secure messages."
  val digitalPref: String =
    "We will send you an email with an update, followed by a letter to your principal " +
      "place of business. You can also go to your HMRC secure messages to find out if your request has been accepted."
  val paperPref = "We will send a letter to your principal place of business with an update within 15 working days."
  val contactPrefError = "We will send you an update within 15 working days."
  val updateInformation = "Updates are usually done within 2 working days. However, they can take up to 10 working " +
    "days if we need to carry out additional checks."
  val contactDetails = "Make sure your contact details are up to date."
}
