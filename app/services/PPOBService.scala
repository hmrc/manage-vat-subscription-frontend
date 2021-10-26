/*
 * Copyright 2021 HM Revenue & Customs
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

package services

import audit.AuditService
import audit.models.ChangeAddressAuditModel
import common.SessionKeys
import connectors.SubscriptionConnector
import connectors.httpParsers.ResponseHttpParser.HttpPutResult
import javax.inject.{Inject, Singleton}
import models.User
import models.circumstanceInfo.CircumstanceDetails
import models.core.{AddressValidationError, SubscriptionUpdateResponseModel}
import models.customerAddress.AddressModel
import models.updatePPOB.{UpdatePPOB, UpdatePPOBAddress}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PPOBService @Inject()(subscriptionConnector: SubscriptionConnector,
                            val auditService: AuditService) {


  private def buildPPOBUpdateModel(addressModel: AddressModel,
                                   circumstanceDetails: CircumstanceDetails,
                                   transactorOrCapacitorEmail: Option[String]): UpdatePPOB = {

    val updateAddress: UpdatePPOBAddress = UpdatePPOBAddress(
      line1 = addressModel.line1,
      line2 = addressModel.line2,
      line3 = addressModel.line3,
      line4 = addressModel.line4,
      postCode = addressModel.postcode,
      countryCode = addressModel.countryCode
    )

    UpdatePPOB(
      updateAddress,
      circumstanceDetails.ppob.contactDetails,
      circumstanceDetails.ppob.websiteAddress,
      transactorOrCapacitorEmail
    )
  }

  def updatePPOB(user: User[_], address: AddressModel, id: String)
                (implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[Either[models.core.Error, SubscriptionUpdateResponseModel]] = {

    if(validateChars(address)) {
      subscriptionConnector.getCustomerCircumstanceDetails(user.vrn) flatMap {
        case Right(customerDetails) =>
          auditService.extendedAudit(
            ChangeAddressAuditModel(user, customerDetails.ppobAddress, address, customerDetails.partyType),
            Some(controllers.routes.BusinessAddressController.callback(id).url)
          )
          subscriptionConnector.updatePPOB(
            user.vrn,
            buildPPOBUpdateModel(address, customerDetails, user.session.get(SessionKeys.verifiedAgentEmail))
          ) map {
            case Right(success) => Right(success)
            case Left(error) => Left(error)
          }
        case Left(error) => Future.successful(Left(error))
      }
    } else {
      Future.successful(Left(AddressValidationError))
    }
  }

  def validateBusinessAddress(vrn: String)
                             (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpPutResult[SubscriptionUpdateResponseModel]] = {
    subscriptionConnector.validateBusinessAddress(vrn)
  }

  def validateChars(address: AddressModel): Boolean = {
    val addressRegex = "^[A-Za-z0-9 \\-,.&'\\/()!]{1,35}$"
    address.line1.fold(true)(x => x.matches(addressRegex)) &&
      address.line2.fold(true)(x => x.matches(addressRegex)) &&
      address.line3.fold(true)(x => x.matches(addressRegex)) &&
      address.line4.fold(true)(x => x.matches(addressRegex))
  }
}
