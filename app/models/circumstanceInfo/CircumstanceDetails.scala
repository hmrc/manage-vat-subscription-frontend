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

package models.circumstanceInfo

import config.AppConfig
import models.JsonReadUtil
import models.returnFrequency.ReturnPeriod
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, Writes, __}

case class CircumstanceDetails(mandationStatus: MandationStatus,
                               customerDetails: CustomerDetails,
                               flatRateScheme: Option[FlatRateScheme],
                               ppob: PPOB,
                               bankDetails:Option[BankDetails],
                               returnPeriod: Option[ReturnPeriod],
                               deregistration: Option[Deregistration],
                               changeIndicators: Option[ChangeIndicators],
                               pendingChanges: Option[PendingChanges],
                               partyType: Option[String]) {

  val ppobAddress: PPOBAddress = ppob.address
  val landlineNumber: Option[String] = ppob.contactDetails.flatMap(_.phoneNumber)
  val mobileNumber: Option[String] = ppob.contactDetails.flatMap(_.mobileNumber)
  val email: Option[String] = ppob.contactDetails.flatMap(_.emailAddress)
  val website: Option[String] = ppob.websiteAddress
  val pendingPPOBAddress: Option[PPOBAddress] = pendingChanges.flatMap(_.ppob.map(_.address))
  val pendingBankDetails: Option[BankDetails] = pendingChanges.flatMap(_.bankDetails)
  val pendingReturnPeriod: Option[ReturnPeriod] = pendingChanges.flatMap(_.returnPeriod)
  val pendingDeregistration: Boolean = changeIndicators.fold(false)(_.deregister)
  val pendingEmail: Option[String] = pendingChanges.flatMap(_.ppob.flatMap(_.contactDetails.flatMap(_.emailAddress)))
  val pendingMandationStatus: Option[MandationStatus] = pendingChanges.flatMap(_.mandationStatus)
  val pendingLandline: Option[String] = pendingChanges.flatMap(_.ppob.flatMap(_.contactDetails.flatMap(_.phoneNumber)))
  val pendingMobile: Option[String] = pendingChanges.flatMap(_.ppob.flatMap(_.contactDetails.flatMap(_.mobileNumber)))

  val pendingPhoneNumber: Boolean = (pendingLandline, pendingMobile) match {
    case (None, None) => false
    case _ => true
  }

  val pendingWebsite: Option[String] = pendingChanges.flatMap(_.ppob.flatMap(_.websiteAddress))

  val samePPOB: Boolean = pendingPPOBAddress.contains(ppobAddress)
  val sameEmail: Boolean = pendingEmail == email
  val samePhone: Boolean = pendingLandline == landlineNumber
  val sameMobile: Boolean = pendingMobile == mobileNumber
  val sameWebsite: Boolean = pendingWebsite == website

  def validPartyType(implicit appConfig: AppConfig): Boolean = partyType.fold(false){
    party => appConfig.partyTypes.contains(party)
  }

}

object CircumstanceDetails extends JsonReadUtil {

  private val mandationStatusPath = __ \ "mandationStatus"
  private val customerDetailsPath = __ \ "customerDetails"
  private val flatRateSchemePath = __ \ "flatRateScheme"
  private val ppobPath = __ \ "ppob"
  private val bankDetailsPath = __ \ "bankDetails"
  private val returnPeriodPath = __ \ "returnPeriod"
  private val deregistrationPath = __ \ "deregistration"
  private val changeIndicatorsPath = __ \ "changeIndicators"
  private val pendingChangesPath = __ \ "pendingChanges"
  private val partyTypePath = __ \ "partyType"

  implicit val reads: Boolean => Reads[CircumstanceDetails] = isRelease10 => (
    mandationStatusPath.read[MandationStatus] and
      customerDetailsPath.read[CustomerDetails](CustomerDetails.reads(isRelease10)) and
      flatRateSchemePath.readOpt[FlatRateScheme] and
      ppobPath.read[PPOB] and
      bankDetailsPath.readOpt[BankDetails] and
      returnPeriodPath.readOpt[ReturnPeriod] and
      deregistrationPath.readOpt[Deregistration] and
      changeIndicatorsPath.readOpt[ChangeIndicators] and
      pendingChangesPath.readOpt[PendingChanges] and
      partyTypePath.readOpt[String]
    )(CircumstanceDetails.apply _)

  implicit val writes: Boolean => Writes[CircumstanceDetails] = isRelease10 => (
    mandationStatusPath.write[MandationStatus] and
      customerDetailsPath.write[CustomerDetails](CustomerDetails.writes(isRelease10)) and
      flatRateSchemePath.writeNullable[FlatRateScheme] and
      ppobPath.write[PPOB] and
      bankDetailsPath.writeNullable[BankDetails] and
      returnPeriodPath.writeNullable[ReturnPeriod]  and
      deregistrationPath.writeNullable[Deregistration] and
      changeIndicatorsPath.writeNullable[ChangeIndicators] and
      pendingChangesPath.writeNullable[PendingChanges] and
      partyTypePath.writeNullable[String]
    )(unlift(CircumstanceDetails.unapply))
}
