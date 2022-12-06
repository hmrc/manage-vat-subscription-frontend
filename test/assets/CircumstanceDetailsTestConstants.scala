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

  val partyType = "4"
  val commsPreference = "DIGITAL"

  val customerInformationJsonMaxOrganisation: JsValue = Json.obj(
    "customerDetails" -> organisationJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsJsonMax,
    "returnPeriod" -> Json.obj(
      "stdReturnPeriod" -> returnPeriodMCJson
    ),
    "partyType" -> Some(partyType),
    "deregistration" -> deregModel,
    "missingTrader" -> true,
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
      "returnPeriod" -> Json.obj(
        "stdReturnPeriod" -> returnPeriodMCJson
      ),
      "tradingName" -> "Pens'n'Dinghy's",
      "organisationName" -> "Stationery'n'Boats"
    ),
    "commsPreference" -> "DIGITAL"
  )

  val customerInformationJsonMaxIndividual: JsValue = Json.obj(
    "customerDetails" -> individualJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsModelMax,
    "returnPeriod" -> Json.obj(
      "stdReturnPeriod" -> returnPeriodMCJson
    ),
    "partyType" -> Some(partyType),
    "deregistration" -> deregModel,
    "missingTrader" -> true,
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
      "returnPeriod" -> Json.obj(
        "stdReturnPeriod" -> returnPeriodMCJson
      )
    )
  )

  val customerInformationJsonMin: JsValue =
    Json.obj(
      "customerDetails" -> customerDetailsJsonMin,
      "ppob" -> ppobJsonMin,
      "missingTrader" -> false
    )


  val customerInformationModelMaxOrganisation: CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    missingTrader = true,
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = true
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMax),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Mar),
      tradingName = Some("Pens'n'Dinghy's"),
      businessName = Some("Stationery'n'Boats")
    )),
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )


  val customerInformationModelMaxOrganisationPending: CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    missingTrader = true,
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = true
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMax),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Feb),
      tradingName = Some("Pens'n'Dinghy's"),
      businessName = Some("Stationery'n'Boats")
    )),
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationModelOrganisationPending: CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = false
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMaxPending),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Mar),
      tradingName = Some("Pens'n'Dinghy's"),
      businessName = Some("Stationery'n'Boats")
    )),
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationModelMaxIndividual: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    missingTrader = true,
    changeIndicators = Some(ChangeIndicators(
      ppob = true,
      bankDetails = true,
      returnPeriod = true,
      deregister = true
    )),
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMax),
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = Some(Mar),
      tradingName = Some("Pens'n'Dinghy's"),
      businessName = Some("Stationery'n'Boats")
    )),
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationNoPendingIndividual: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationNoPendingIndividualMTDfB: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationSomePendingIndividual: CircumstanceDetails = customerInformationNoPendingIndividual.copy(
    pendingChanges = Some(PendingChanges(
      ppob = None,
      bankDetails = Some(bankDetailsModelMax),
      returnPeriod = None,
      tradingName = None,
      businessName = None
    ))
  )

  val customerInformationPendingNoInfo: CircumstanceDetails = customerInformationModelMaxIndividual.copy(
    pendingChanges = Some(PendingChanges(
      ppob = Some(ppobModelMin),
      bankDetails = None,
      returnPeriod = None,
      tradingName = None,
      businessName = None
    ))
  )

  val customerInformationNoPendingIndividualDeregistered: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationNoPendingChangeOfCert: CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some(partyType),
    commsPreference = Some("DIGITAL")
  )

  def customerInformationWithPartyType(partyType: Option[String]): CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = partyType,
    commsPreference = Some("DIGITAL")
  )

  val customerInformationRegisteredIndividual: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None,
    commsPreference = Some("DIGITAL")
  )

  val customerInformationNoPendingOrganisation: CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(deregModel),
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = Some("other"),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationPendingPPOBOrganisation: CircumstanceDetails = CircumstanceDetails(
    customerDetails = organisation,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = None,
    deregistration = Some(deregModel),
    missingTrader = false,
    changeIndicators = Some(
      ChangeIndicators(
        ppob = true,
        bankDetails = false,
        returnPeriod = false,
        deregister = false)
    ),
    pendingChanges = None,
    partyType = Some("other"),
    commsPreference = Some("DIGITAL")
  )

  val customerInformationModelFutureDereg: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = Some(futureDeregModel),
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None,
    commsPreference = Some("DIGITAL")
  )

  val customerInformationModelDeregPending: CircumstanceDetails = CircumstanceDetails(
    customerDetails = individual,
    flatRateScheme = Some(frsModelMax),
    ppob = ppobModelMax,
    bankDetails = Some(bankDetailsModelMax),
    returnPeriod = Some(Mar),
    deregistration = None,
    missingTrader = false,
    changeIndicators = Some(
      ChangeIndicators(
        ppob = false,
        bankDetails = false,
        returnPeriod = false,
        deregister = true)
    ),
    pendingChanges = None,
    partyType = None,
    commsPreference = Some("DIGITAL")
  )

  val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
    customerDetails = customerDetailsMin,
    flatRateScheme = None,
    ppob = ppobModelMin,
    bankDetails = None,
    returnPeriod = None,
    deregistration = None,
    missingTrader = false,
    changeIndicators = None,
    pendingChanges = None,
    partyType = None,
    commsPreference = None
  )

  def customerInformationModelPendingRemoved(field: String): CircumstanceDetails = {
    val pendingPPOB = field match {
      case "email" => Some(ppobModelMax.copy(contactDetails = Some(contactDetailsModelMax.copy(emailAddress = None))))
      case "landline" => Some(ppobModelMax.copy(contactDetails = Some(contactDetailsModelMax.copy(phoneNumber = None))))
      case "mobile" => Some(ppobModelMax.copy(contactDetails = Some(contactDetailsModelMax.copy(mobileNumber = None))))
      case "website" => Some(ppobModelMax.copy(websiteAddress = None))
    }
    customerInformationPendingPPOBOrganisation.copy(
      pendingChanges = Some(PendingChanges(pendingPPOB, None, None, None, None))
    )
  }

  val customerInformationPendingEmailModel: CircumstanceDetails = customerInformationPendingPPOBOrganisation.copy(
    pendingChanges = Some(PendingChanges(
      Some(PPOB(
        ppobAddressModelMax,
        Some(contactDetailsModelMax.copy(emailAddress = Some(emailPending))),
        Some(website)
      )),
      None,
      None,
      None,
      None
    ))
  )

  val customerInformationPendingPPOBModel: CircumstanceDetails = customerInformationPendingPPOBOrganisation.copy(
    pendingChanges = Some(PendingChanges(
      Some(PPOB(
        ppobAddressModelMaxPending,
        Some(contactDetailsModelMax),
        Some(website)
      )),
      None,
      None,
      None,
      None
    ))
  )

  val customerInformationPendingPhoneModel: CircumstanceDetails = customerInformationPendingPPOBOrganisation.copy(
    pendingChanges = Some(PendingChanges(
      Some(PPOB(
        ppobAddressModelMax,
        Some(contactDetailsModelMax.copy(phoneNumber = Some(phoneNumberPending))),
        Some(website)
      )),
      None,
      None,
      None,
      None
    ))
  )

  val customerInformationPendingRemovedPhoneModel: CircumstanceDetails = customerInformationPendingEmailModel.copy(
    pendingChanges = Some(PendingChanges(
      Some(ppobModelMax.copy(
        contactDetails = Some(contactDetailsModelMax.copy(phoneNumber = None))
      )), None, None, None, None
    ))
  )

  val customerInformationPendingMobileModel: CircumstanceDetails = customerInformationPendingPPOBOrganisation.copy(
    pendingChanges = Some(PendingChanges(
      Some(PPOB(
        ppobAddressModelMax,
        Some(contactDetailsModelMax.copy(mobileNumber = Some(mobileNumberPending))),
        Some(website)
      )),
      None,
      None,
      None,
      None
    ))
  )

  val customerInformationPendingWebsiteModel: CircumstanceDetails = customerInformationPendingPPOBOrganisation.copy(
    pendingChanges = Some(PendingChanges(
      Some(PPOB(
        ppobAddressModelMax,
        Some(contactDetailsModelMax),
        Some(websitePending)
      )),
      None,
      None,
      None,
      None
    ))
  )

  val customerInformationPendingReturnPeriodModel: CircumstanceDetails = customerInformationRegisteredIndividual.copy(
    changeIndicators = Some(ChangeIndicators(
      ppob = false, bankDetails = false, returnPeriod = true, deregister = false
    )),
    pendingChanges = Some(PendingChanges(
      None, None, Some(Monthly), None, None
    ))
  )

  val customerInfoPendingTradingNameModel: CircumstanceDetails = customerInformationModelMin.copy(
    pendingChanges = Some(PendingChanges(None, None, None, Some("Party Kitchen"), None))
  )

  val customerInfoPendingBusinessNameModel: CircumstanceDetails = customerInformationModelMin.copy(
    customerDetails = individual.copy(organisationName = Some(orgName)),
    partyType = Some(partyType),
    pendingChanges = Some(PendingChanges(None, None, None, None, Some("Party Kitchen")))
  )

  val overseasCompany: CircumstanceDetails = customerInformationModelMin.copy(customerDetails = overseasOrganisation)

  val customerInformationInsolvent: CircumstanceDetails =
    customerInformationModelMin.copy(customerDetails = customerDetailsInsolvent)
}
