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

package assets

import assets.BankDetailsTestConstants._
import assets.CustomerDetailsTestConstants._
import assets.FlatRateSchemeTestConstants._
import assets.PPOBAddressTestConstants._
import assets.ReturnPeriodTestConstants._
import assets.DeregistrationTestConstants._
import models.circumstanceInfo._
import models.returnFrequency._
import play.api.libs.json.{JsValue, Json}

object CircumstanceDetailsTestConstants {

  val mandationStatus = "MTDfB Mandated"
  val partyType = "2"

  val customerInformationJsonMaxOrganisation: JsValue = Json.obj(
    "customerDetails" -> organisationJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsJsonMax,
    "returnPeriod" -> returnPeriodMCJson,
    "mandationStatus" -> mandationStatus,
    "partyType" -> Some(partyType),
    "deregistration" -> deregModel,
    "changeIndicators" -> Json.obj(
      "PPOBDetails" -> true,
      "bankDetails" -> true,
      "returnPeriod" -> true,
      "deregister" -> true
    ),
    "pendingChanges" -> Json.obj(
      "PPOBDetails" -> Json.obj(
        "address" -> Json.obj(
          "line1" -> addLine1,
          "line2" -> addLine2,
          "line3" -> addLine3,
          "line4" -> addLine4,
          "line5" -> addLine5,
          "postCode" -> postcode,
          "countryCode" -> countryCode
        ),
        "contactDetails" -> Json.obj(
          "primaryPhoneNumber" -> phoneNumber,
          "mobileNumber" -> mobileNumber,
          "faxNumber" -> faxNumber,
          "emailAddress" -> email,
          "emailVerified" -> emailVerified
        ),
        "websiteAddress" -> website
      ),
      "bankDetails" -> Json.obj(
        "accountHolderName" -> accName,
        "bankAccountNumber" -> accNum,
        "sortCode" -> accSort
      ),
      "returnPeriod" -> returnPeriodMCJson
    )
  )

  val customerInformationJsonMaxIndividual: JsValue = Json.obj(
    "customerDetails" -> individualJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsModelMax,
    "returnPeriod" -> returnPeriodMCJson,
    "mandationStatus" -> mandationStatus,
    "partyType" -> Some(partyType),
    "deregistration" -> deregModel,
    "changeIndicators" -> Json.obj(
      "PPOBDetails" -> true,
      "bankDetails" -> true,
      "returnPeriod" -> true,
      "deregister" -> true
    ),
    "pendingChanges" -> Json.obj(
      "PPOBDetails" -> Json.obj(
        "address" -> Json.obj(
          "line1" -> addLine1,
          "line2" -> addLine2,
          "line3" -> addLine3,
          "line4" -> addLine4,
          "line5" -> addLine5,
          "postCode" -> postcode,
          "countryCode" -> countryCode
        ),
        "contactDetails" -> Json.obj(
          "primaryPhoneNumber" -> phoneNumber,
          "mobileNumber" -> mobileNumber,
          "faxNumber" -> faxNumber,
          "emailAddress" -> email,
          "emailVerified" -> emailVerified
        ),
        "websiteAddress" -> website
      ),
      "bankDetails" -> Json.obj(
        "accountHolderName" -> accName,
        "bankAccountNumber" -> accNum,
        "sortCode" -> accSort
      ),
      "returnPeriod" -> returnPeriodMCJson
    )
  )

  val customerInformationJsonMin: JsValue =
    Json.obj(
      "customerDetails" -> customerDetailsJsonMin,
      "ppob" -> ppobJsonMin,
      "mandationStatus" -> mandationStatus
    )


  val customerInformationModelMaxOrganisation: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = true
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMax),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Mar)
    )),
    partyType = Some(partyType)
  )

  val customerInformationModelMaxOrganisationPending: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = true
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMaxPending),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Mar)
    )),
    partyType = Some(partyType)
  )

  val customerInformationModelMaxIndividual: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = true
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMax),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Mar)
    )),
    partyType = Some(partyType)
  )

  val customerInformationNoPendingIndividual: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some(partyType)
  )

  val customerInformationNoPendingChangeOfCert: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some(partyType)
  )

  def customerInformationWithPartyType(partyType: Option[String]): CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    changeIndicators = None,
    pendingChanges = None,
    partyType = partyType
  )

  val customerInformationRegisteredIndividual: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None
  )

  val customerInformationNoPendingOrganisation: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some("other")
  )

  val customerInformationModelFutureDereg: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(futureDeregModel),
    changeIndicators = None,
    pendingChanges = None,
    partyType = None
  )

  val customerInformationModelDeregPending: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    changeIndicators = Some(
      ChangeIndicators(
        ppob = false,
        bankDetails = false,
        returnPeriod = false,
        deregister = true)
    ),
    pendingChanges = None,
    partyType = None
  )

  val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = CustomerDetails(
      firstName = None,
      lastName = None,
      organisationName = None,
      tradingName = None),
    flatRateScheme = None,
    ppob = ppobModelMin,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None
  )

  val customerInformationPendingEmailModel: CircumstanceDetails = CircumstanceDetails(
    mandationStatus = MTDfBMandated,
    customerDetails = customerDetailsMin,
    flatRateScheme = None,
    ppob = ppobModelMax,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    changeIndicators = Some(
      ChangeIndicators(
        ppob = true,
        bankDetails = false,
        returnPeriod = false,
        deregister = false
      )
    ),
    pendingChanges = Some(PendingChanges(
      Some(PPOB(
        ppobAddressModelMax,
        Some(ContactDetails(
          Some(phoneNumber),
          Some(mobileNumber),
          Some(faxNumber),
          Some(emailPending),
          Some(emailVerified)
        )),
        Some(website)
      )),
      None,
      None
    )),
    partyType = None
  )

}
