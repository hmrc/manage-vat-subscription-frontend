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

import models.customerInfo._
import CustomerDetailsTestConstants._
import play.api.libs.json.Json

object CustomerInformationTestConstants {


  val mtdfbMandatedIndividual = CustomerInformationModel(MTDfBMandated, individual)
  val mtdfbVoluntaryIndividual = CustomerInformationModel(MTDfBVoluntary, individual)
  val nonMtdfbIndividual = CustomerInformationModel(NonMTDfB, individual)
  val nonDigitalIndividual = CustomerInformationModel(NonDigital, individual)

  val mtdfbMandatedOrganisation = CustomerInformationModel(MTDfBMandated, organisation)
  val mtdfbVoluntaryOrganisation = CustomerInformationModel(MTDfBVoluntary, organisation)
  val nonMtdfbOrganisation = CustomerInformationModel(NonMTDfB, organisation)
  val nonDigitalOrganisation = CustomerInformationModel(NonDigital, organisation)

  val customerInformationMax = CustomerInformationModel(MTDfBMandated, customerDetailsMax)
  val customerInformationMin = CustomerInformationModel(MTDfBMandated, customerDetailsMin)

  val mtdfbMandatedIndividualJson = Json.obj(
    "mandationStatus" -> MTDfBMandated.status,
    "customerDetails" -> individualJson
  )

  val customerInformationJsonMax = Json.obj(
    "mandationStatus" -> MTDfBMandated.status,
    "customerDetails" -> customerDetailsJsonMax
  )
  val customerInformationJsonMin = Json.obj(
    "mandationStatus" -> MTDfBMandated.status,
    "customerDetails" -> customerDetailsJsonMin
  )

}
