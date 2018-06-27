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

package assets

import assets.BankDetailsTestConstants._
import assets.CustomerDetailsTestConstants._
import assets.FlatRateSchemeTestConstants._
import assets.PPOBAddressTestConstants._
import assets.ReturnPeriodTestConstants._
import models.circumstanceInfo._
import models.returnFrequency._
import play.api.libs.json.{JsValue, Json}

object CircumstanceDetailsTestConstants {

  val mandationStatus = "MTDfB Mandated"

  val customerInformationJsonMaxOrganisation: JsValue = Json.obj(
    "customerDetails" -> organisationJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsJsonMax,
    "returnPeriod" -> returnPeriodMCJson,
    "mandationStatus" -> mandationStatus,
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
    MTDfBMandated,
    organisation,
    Some(frsModelMax),
    ppobModelMax,
    Some(bankDetailsModelMax),
    Some(Mar),
    Some(PendingChanges(
      Some(ppobModelMax),
      Some(bankDetailsModelMax),
      Some(Mar)
    ))
  )

  val customerInformationModelMaxIndividual: CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated,
    individual,
    Some(frsModelMax),
    ppobModelMax,
    Some(bankDetailsModelMax),
    Some(Mar),
    Some(PendingChanges(
      Some(ppobModelMax),
      Some(bankDetailsModelMax),
      Some(Mar)
    ))
  )

  val customerInformationNoPending: CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated,
    individual,
    Some(frsModelMax),
    ppobModelMax,
    Some(bankDetailsModelMax),
    Some(Mar),
    None
  )

  val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated, CustomerDetails(None, None, None, None), None, ppobModelMin, None, None, None
  )

}
