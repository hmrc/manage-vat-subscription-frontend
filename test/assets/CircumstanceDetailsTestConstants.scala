/*
 * Copyright 2020 HM Revenue & Customs
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
import models.contactPreferences.ContactPreference
import models.returnFrequency._
import play.api.libs.json.{JsValue, Json}

object CircumstanceDetailsTestConstants {

  val partyType = "2"
  val commsPreference = "DIGITAL"

  val customerInformationJsonMaxOrganisation: JsValue = Json.obj(
    "customerDetails" -> organisationJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsJsonMax,
    "returnPeriod" -> returnPeriodMCJson,
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
      "returnPeriod" -> returnPeriodMCJson
    ),
    "commsPreference" -> "DIGITAL"
  )

  val customerInformationJsonMaxIndividual: JsValue = Json.obj(
    "customerDetails" -> individualJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsModelMax,
    "returnPeriod" -> returnPeriodMCJson,
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
      "returnPeriod" -> returnPeriodMCJson
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
      returnPeriod = Some(Mar)
    )),
    partyType = Some(partyType),
    commsPreference = Some(ContactPreference("DIGITAL"))
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
      returnPeriod = Some(Feb)
    )),
    partyType = Some(partyType),
    commsPreference = Some(ContactPreference("DIGITAL"))
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
      returnPeriod = Some(Mar)
    )),
    partyType = Some(partyType),
    commsPreference = Some(ContactPreference("DIGITAL"))
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
      returnPeriod = Some(Mar)
    )),
    partyType = Some(partyType),
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
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
    commsPreference = Some(ContactPreference("DIGITAL"))
  )

  val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
    customerDetails = CustomerDetails(
      firstName = None,
      lastName = None,
      organisationName = None,
      tradingName = None,
      welshIndicator = None,
      overseasIndicator = false),
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
      pendingChanges = Some(PendingChanges(pendingPPOB, None, None))
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
      None
    ))
  )

  val customerInformationPendingRemovedPhoneModel: CircumstanceDetails = customerInformationPendingEmailModel.copy(
    pendingChanges = Some(PendingChanges(
      Some(ppobModelMax.copy(
        contactDetails = Some(contactDetailsModelMax.copy(phoneNumber = None))
      )), None, None
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
      None
    ))
  )

  val overseasCompany: CircumstanceDetails = customerInformationModelMin.copy(customerDetails = organisation.copy(overseasIndicator = true))

  /////////////// Release 8 data -- separated for ease of removal

//  val customerInformationModelMaxOrganisationR8: CircumstanceDetails = CircumstanceDetails(
//    customerDetails = organisationR8,
//    flatRateScheme = Some(frsModelMax),
//    ppob = ppobModelMax,
//    bankDetails = Some(bankDetailsModelMax),
//    returnPeriod = Some(Mar),
//    deregistration = Some(deregModel),
//    missingTrader = true,
//    changeIndicators = Some(ChangeIndicators(
//      ppob = true,
//      bankDetails = true,
//      returnPeriod = true,
//      deregister = true
//    )),
//    pendingChanges = Some(PendingChanges(
//      ppob = Some(ppobModelMax),
//      bankDetails = Some(bankDetailsModelMax),
//      returnPeriod = Some(Mar)
//    )),
//    partyType = Some(partyType)
//  )
//
//  val customerInformationModelMinR8: CircumstanceDetails = CircumstanceDetails(
//    customerDetails = CustomerDetails(
//      firstName = None,
//      lastName = None,
//      organisationName = None,
//      tradingName = None,
//      welshIndicator = None,
//      overseasIndicator = false),
//    flatRateScheme = None,
//    ppob = ppobModelMin,
//    bankDetails = None,
//    returnPeriod = None,
//    deregistration = None,
//    missingTrader = false,
//    changeIndicators = None,
//    pendingChanges = None,
//    partyType = None
//  )
//
//  val customerInformationJsonMaxOrganisationR8: JsValue = Json.obj(
//    "customerDetails" -> organisationJsonR8,
//    "flatRateScheme" -> frsJsonMax,
//    "ppob" -> ppobJsonMax,
//    "bankDetails" -> bankDetailsJsonMax,
//    "returnPeriod" -> returnPeriodMCJson,
//    "partyType" -> Some(partyType),
//    "deregistration" -> deregModel,
//    "missingTrader" -> true,
//    "changeIndicators" -> Json.obj(
//      "PPOBDetails" -> true,
//      "bankDetails" -> true,
//      "returnPeriod" -> true,
//      "deregister" -> true
//    ),
//    "pendingChanges" -> Json.obj(
//      "PPOBDetails" -> Json.obj(
//        "address" -> Json.obj(
//          "line1" -> addLine1,
//          "line2" -> addLine2,
//          "line3" -> addLine3,
//          "line4" -> addLine4,
//          "line5" -> addLine5,
//          "postCode" -> postcode,
//          "countryCode" -> countryCode
//        ),
//        "contactDetails" -> Json.obj(
//          "primaryPhoneNumber" -> phoneNumber,
//          "mobileNumber" -> mobileNumber,
//          "faxNumber" -> faxNumber,
//          "emailAddress" -> email,
//          "emailVerified" -> emailVerified
//        ),
//        "websiteAddress" -> website
//      ),
//      "bankDetails" -> Json.obj(
//        "accountHolderName" -> accName,
//        "bankAccountNumber" -> accNum,
//        "sortCode" -> accSort
//      ),
//      "returnPeriod" -> returnPeriodMCJson
//    )
//  )
//
//  val customerInformationJsonMinR8: JsValue =
//    Json.obj(
//      "customerDetails" -> customerDetailsJsonMinR8,
//      "ppob" -> ppobJsonMin,
//      "missingTrader" -> false
//    )
//
//  val customerInformationJsonMinWithTrueOverseas: JsValue =
//    Json.obj(
//      "customerDetails" -> Json.obj(
//        "hasFlatRateScheme" -> false,
//        "overseasIndicator" -> true
//      ),
//      "ppob" -> ppobJsonMin,
//      "missingTrader" -> false
//  )
}
