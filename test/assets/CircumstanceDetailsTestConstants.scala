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
    "mandationStatus" -> mandationStatus
  )

  val customerInformationJsonMaxIndividual: JsValue = Json.obj(
    "customerDetails" -> individualJson,
    "flatRateScheme" -> frsJsonMax,
    "ppob" -> ppobJsonMax,
    "bankDetails" -> bankDetailsModelMax,
    "returnPeriod" -> returnPeriodMCJson,
    "mandationStatus" -> mandationStatus
  )

  val customerInformationJsonMin: JsValue =
    Json.obj(
      "customerDetails" -> customerDetailsJsonMin,
      "mandationStatus" -> mandationStatus
    )


  val customerInformationModelMaxOrganisation: CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated,
    organisation,
    Some(frsModelMax),
    Some(ppobModelMax),
    Some(bankDetailsModelMax),
    Some(Mar)
  )

  val customerInformationModelMaxIndividual: CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated,
    individual,
    Some(frsModelMax),
    Some(ppobModelMax),
    Some(bankDetailsModelMax),
    Some(Mar)
  )

  val customerInformationModelMin: CircumstanceDetails = CircumstanceDetails(
    MTDfBMandated, CustomerDetails(None, None, None, None), None, None, None, None
  )

}
