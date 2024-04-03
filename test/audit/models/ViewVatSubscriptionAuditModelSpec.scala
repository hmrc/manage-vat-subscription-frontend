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

package audit.models

import assets.BaseTestConstants._
import assets.CircumstanceDetailsTestConstants._
import play.api.libs.json.Json
import utils.TestUtil

class ViewVatSubscriptionAuditModelSpec extends TestUtil {

  val transactionName = "view-vat-subscription-details"
  val auditType = "GetVatSubscriptionDetails"

  lazy val agentNoPendingIndividual = ViewVatSubscriptionAuditModel(agentUser, customerInformationNoPendingIndividual)
  lazy val agentPendingIndividual = ViewVatSubscriptionAuditModel(agentUser, customerInformationModelMaxIndividual)
  lazy val agentNoPendingOrganisation = ViewVatSubscriptionAuditModel(agentUser, customerInformationNoPendingOrganisation)
  lazy val agentPendingOrganisation = ViewVatSubscriptionAuditModel(agentUser, customerInformationModelMaxOrganisation)

  lazy val userNoPendingIndividual = ViewVatSubscriptionAuditModel(user, customerInformationNoPendingIndividual)
  lazy val userPendingIndividual = ViewVatSubscriptionAuditModel(user, customerInformationModelMaxIndividual)
  lazy val userSomePendingIndividual = ViewVatSubscriptionAuditModel(user, customerInformationSomePendingIndividual)
  lazy val userNoPendingOrganisation = ViewVatSubscriptionAuditModel(user, customerInformationNoPendingOrganisation)
  lazy val userPendingOrganisation = ViewVatSubscriptionAuditModel(user, customerInformationModelMaxOrganisation)
  lazy val userSomePendingOrganisationNoContact = ViewVatSubscriptionAuditModel(user, customerInformationPendingNoInfo)

  lazy val userWithNoPartyType = ViewVatSubscriptionAuditModel(user, customerInformationRegisteredIndividual)

  "The ViewVatSubscriptionAuditModel" should {

    s"Have the correct transaction name of '$transactionName'" in {
      agentNoPendingIndividual.transactionName shouldBe transactionName
    }

    s"Have the correct audit event type of '$auditType'" in {
      agentNoPendingIndividual.auditType shouldBe auditType
    }

    "For an Agent acting on behalf of a Client" when {

      "the Client is an Individual" when {

        "there are no pending changes" should {

          "Have the correct details for the audit event" in {
            agentNoPendingIndividual.detail shouldBe Json.obj(
              "isAgent" -> true,
              "agentReferenceNumber" -> arn,
              "vrn" -> vrn,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationNoPendingIndividual.ppobAddress.line1,
                "line2" -> customerInformationNoPendingIndividual.ppobAddress.line2.get,
                "line3" -> customerInformationNoPendingIndividual.ppobAddress.line3.get,
                "line4" -> customerInformationNoPendingIndividual.ppobAddress.line4.get,
                "line5" -> customerInformationNoPendingIndividual.ppobAddress.line5.get,
                "postCode" -> customerInformationNoPendingIndividual.ppobAddress.postCode.get,
                "countryCode" -> customerInformationNoPendingIndividual.ppobAddress.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationNoPendingIndividual.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationNoPendingIndividual.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> false,
                "repaymentBankDetails" -> false,
                "vatReturnDates" -> false,
                "emailAddress" -> false
              ),
              "partyType" -> partyType
            )
          }
        }

        "there are pending changes" should {

          "Have the correct details for the audit event" in {
            agentPendingIndividual.detail shouldBe Json.obj(
              "isAgent" -> true,
              "agentReferenceNumber" -> arn,
              "vrn" -> vrn,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line1,
                "line2" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line2.get,
                "line3" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line3.get,
                "line4" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line4.get,
                "line5" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line5.get,
                "postCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.postCode.get,
                "countryCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> true,
                "repaymentBankDetails" -> true,
                "vatReturnDates" -> true,
                "emailAddress" -> true
              ),
              "partyType" -> partyType
            )
          }
        }

      }

      "the Client is an Organisation" when {

        "there are no pending changes" should {

          "Have the correct details for the audit event" in {
            agentNoPendingOrganisation.detail shouldBe Json.obj(
              "isAgent" -> true,
              "agentReferenceNumber" -> arn,
              "vrn" -> vrn,
              "businessName" -> customerInformationNoPendingOrganisation.customerDetails.organisationName.get,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationNoPendingOrganisation.ppobAddress.line1,
                "line2" -> customerInformationNoPendingOrganisation.ppobAddress.line2.get,
                "line3" -> customerInformationNoPendingOrganisation.ppobAddress.line3.get,
                "line4" -> customerInformationNoPendingOrganisation.ppobAddress.line4.get,
                "line5" -> customerInformationNoPendingOrganisation.ppobAddress.line5.get,
                "postCode" -> customerInformationNoPendingOrganisation.ppobAddress.postCode.get,
                "countryCode" -> customerInformationNoPendingOrganisation.ppobAddress.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationNoPendingOrganisation.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationNoPendingOrganisation.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> false,
                "repaymentBankDetails" -> false,
                "vatReturnDates" -> false,
                "emailAddress" -> false
              ),
              "partyType" -> "other"
            )
          }
        }

        "there are pending changes" should {

          "Have the correct details for the audit event" in {
            agentPendingOrganisation.detail shouldBe Json.obj(
              "isAgent" -> true,
              "agentReferenceNumber" -> arn,
              "vrn" -> vrn,
              "businessName" -> customerInformationModelMaxOrganisation.customerDetails.organisationName.get,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line1,
                "line2" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line2.get,
                "line3" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line3.get,
                "line4" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line4.get,
                "line5" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line5.get,
                "postCode" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.postCode.get,
                "countryCode" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationModelMaxOrganisation.pendingBankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationModelMaxOrganisation.pendingBankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> true,
                "repaymentBankDetails" -> true,
                "vatReturnDates" -> true,
                "emailAddress" -> true
              ),
              "partyType" -> partyType
            )
          }
        }
      }
    }

    "For an Individual (Principal User)" when {

      "the User is an Individual" when {

        "there are no pending changes" should {

          "Have the correct details for the audit event" in {
            userNoPendingIndividual.detail shouldBe Json.obj(
              "isAgent" -> false,
              "vrn" -> vrn,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationNoPendingIndividual.ppobAddress.line1,
                "line2" -> customerInformationNoPendingIndividual.ppobAddress.line2.get,
                "line3" -> customerInformationNoPendingIndividual.ppobAddress.line3.get,
                "line4" -> customerInformationNoPendingIndividual.ppobAddress.line4.get,
                "line5" -> customerInformationNoPendingIndividual.ppobAddress.line5.get,
                "postCode" -> customerInformationNoPendingIndividual.ppobAddress.postCode.get,
                "countryCode" -> customerInformationNoPendingIndividual.ppobAddress.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationNoPendingIndividual.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationNoPendingIndividual.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> false,
                "repaymentBankDetails" -> false,
                "vatReturnDates" -> false,
                "emailAddress" -> false
              ),
              "partyType" -> partyType
            )
          }
        }

        "there are pending changes" should {

          "Have the correct details for the audit event" in {
            userPendingIndividual.detail shouldBe Json.obj(
              "isAgent" -> false,
              "vrn" -> vrn,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line1,
                "line2" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line2.get,
                "line3" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line3.get,
                "line4" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line4.get,
                "line5" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line5.get,
                "postCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.postCode.get,
                "countryCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> true,
                "repaymentBankDetails" -> true,
                "vatReturnDates" -> true,
                "emailAddress" -> true
              ),
              "partyType" -> partyType
            )
          }

          "have the correct details for the audit event when some changes are pending but not all" in {
            userSomePendingIndividual.detail shouldBe Json.obj(
              "isAgent" -> false,
              "vrn" -> vrn,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line1,
                "line2" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line2.get,
                "line3" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line3.get,
                "line4" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line4.get,
                "line5" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line5.get,
                "postCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.postCode.get,
                "countryCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> false,
                "repaymentBankDetails" -> true,
                "vatReturnDates" -> false,
                "emailAddress" -> false
              ),
              "partyType" -> partyType
            )
          }

          "have the correct details for the audit event when there is a pending PPOB with no contact details" in {
            userSomePendingOrganisationNoContact.detail shouldBe Json.obj(
              "isAgent" -> false,
              "vrn" -> vrn,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.line1,
                "countryCode" -> customerInformationModelMaxIndividual.pendingChanges.get.ppob.get.address.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationModelMaxIndividual.pendingChanges.get.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> true,
                "repaymentBankDetails" -> false,
                "vatReturnDates" -> false,
                "emailAddress" -> false
              ),
              "partyType" -> partyType
            )
          }
        }
      }

      "the User is an Organisation" when {

        "there are no pending changes" should {

          "Have the correct details for the audit event" in {
            userNoPendingOrganisation.detail shouldBe Json.obj(
              "isAgent" -> false,
              "vrn" -> vrn,
              "businessName" -> customerInformationNoPendingOrganisation.customerDetails.organisationName.get,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationNoPendingOrganisation.ppobAddress.line1,
                "line2" -> customerInformationNoPendingOrganisation.ppobAddress.line2.get,
                "line3" -> customerInformationNoPendingOrganisation.ppobAddress.line3.get,
                "line4" -> customerInformationNoPendingOrganisation.ppobAddress.line4.get,
                "line5" -> customerInformationNoPendingOrganisation.ppobAddress.line5.get,
                "postCode" -> customerInformationNoPendingOrganisation.ppobAddress.postCode.get,
                "countryCode" -> customerInformationNoPendingOrganisation.ppobAddress.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationNoPendingOrganisation.bankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationNoPendingOrganisation.bankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> false,
                "repaymentBankDetails" -> false,
                "vatReturnDates" -> false,
                "emailAddress" -> false
              ),
              "partyType" -> "other"
            )
          }
        }

        "there are pending changes" should {

          "Have the correct details for the audit event" in {
            userPendingOrganisation.detail shouldBe Json.obj(
              "isAgent" -> false,
              "vrn" -> vrn,
              "businessName" -> customerInformationModelMaxOrganisation.customerDetails.organisationName.get,
              "businessAddress" -> Json.obj(
                "line1" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line1,
                "line2" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line2.get,
                "line3" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line3.get,
                "line4" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line4.get,
                "line5" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.line5.get,
                "postCode" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.postCode.get,
                "countryCode" -> customerInformationModelMaxOrganisation.pendingPPOBAddress.get.countryCode
              ),
              "repaymentBankDetails" -> Json.obj(
                "accountNumber" -> customerInformationModelMaxOrganisation.pendingBankDetails.get.bankAccountNumber.get,
                "sortCode" -> customerInformationModelMaxOrganisation.pendingBankDetails.get.sortCode.get
              ),
              "vatReturnFrequency" -> "March, June, September and December",
              "email" -> "test@test.com",
              "inFlightChanges" -> Json.obj(
                "businessAddress" -> true,
                "repaymentBankDetails" -> true,
                "vatReturnDates" -> true,
                "emailAddress" -> true
              ),
              "partyType" -> partyType
            )
          }
        }
      }

      "the User has no partyType" should {

        "Have the correct details for the audit event" in {
          userWithNoPartyType.detail shouldBe Json.obj(
            "isAgent" -> false,
            "vrn" -> vrn,
            "businessAddress" -> Json.obj(
              "line1" -> customerInformationRegisteredIndividual.ppobAddress.line1,
              "line2" -> customerInformationRegisteredIndividual.ppobAddress.line2.get,
              "line3" -> customerInformationRegisteredIndividual.ppobAddress.line3.get,
              "line4" -> customerInformationRegisteredIndividual.ppobAddress.line4.get,
              "line5" -> customerInformationRegisteredIndividual.ppobAddress.line5.get,
              "postCode" -> customerInformationRegisteredIndividual.ppobAddress.postCode.get,
              "countryCode" -> customerInformationRegisteredIndividual.ppobAddress.countryCode
            ),
            "repaymentBankDetails" -> Json.obj(
              "accountNumber" -> customerInformationRegisteredIndividual.bankDetails.get.bankAccountNumber.get,
              "sortCode" -> customerInformationRegisteredIndividual.bankDetails.get.sortCode.get
            ),
            "vatReturnFrequency" -> "March, June, September and December",
            "email" -> "test@test.com",
            "inFlightChanges" -> Json.obj(
              "businessAddress" -> false,
              "repaymentBankDetails" -> false,
              "vatReturnDates" -> false,
              "emailAddress" -> false
            )
          )
        }
      }
    }
  }
}
