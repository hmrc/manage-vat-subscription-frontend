/*
 * Copyright 2022 HM Revenue & Customs
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

trait BaseMessages {

  val clientServiceName = "Manage your VAT account"
  val otherServiceName = "VAT"
  val agentServiceName = "Your client’s VAT details"

  val continue = "Continue"
  val confirm = "Confirm"
  val confirmAndContinue = "Confirm and continue"
  val saveAndContinue = "Save and continue"
  val signOut = "Sign out"
  val finish = "Finish"
  val finishAgent = "Back to client’s details"
  val back = "Back"
  val errorHeading = "There is a problem"

  val breadcrumbBta = "Business tax account"
  val breadcrumbVat = "Your VAT account"
  val breadcrumbBizDeets = "Your business details"
  val titleSuffixUser = " - Manage your VAT account - GOV.UK"
  val titleSuffixOther = " - VAT - GOV.UK"
  val titleSuffixAgent  = " - Your client’s VAT details - GOV.UK"

  val errorTitlePrefix = "Error:"
}
