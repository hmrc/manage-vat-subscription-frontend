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

object ChangeBusinessNamePageMessages extends BaseMessages {

  val title: String = "Change the business name" + titleSuffixUser
  val titleAgent: String = "Change the business name" + titleSuffixAgent
  val heading = "Change the business name"


  val p1 = "If you have a Company Registration Number (CRN) then this is done through Companies House."
  val noCRN = "If you do not have a CRN or are not registered for Self Assessment"
  val toChangeName = "To change your business name, you'll need to "
  val contactHMRC = "contact HMRC (opens in a new tab)"
  val p2 = "They'll send you a new Certificate of Incorporation when the details have changed."
  val your = "Your"
  val bold1 = " business tax account "
  val p3 = "will show your new business name within 15 days."
  val p4 = "You must tell HMRC of the change to your business name within 30 days or you may need to pay a penalty."
  val p5 = "Find out how to change your business name and how much it costs (opens in a new tab)"

  val altP1: String => String = name => s"The business name is currently $name."
  val altP2: String =
    "You cannot change the business name on your VAT account. You must change your business name using an alternative service."
  val altP2Trust: String =
    "You cannot change the business name on your VAT account. You must change your business name through the Charities Commission."
  val altP2Agent: String =
    "You cannot change your client’s business name from their VAT account. You must change the business name using an alternative service."
  val altP2AgentTrust: String =
    "You cannot change your client’s business name from their VAT account. You must change the business name through the Charities Commission."
  val altContinueLinkText = "Read the guidance on how to change a business name and any other business details (opens in a new tab)."
}
