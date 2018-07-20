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

package services

import audit.AuditService
import audit.models.ChangeAddressAuditModel
import javax.inject.{Inject, Singleton}
import connectors.SubscriptionConnector
import models.User
import models.circumstanceInfo.{CircumstanceDetails, PPOBAddress}
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import models.customerAddress.AddressModel
import models.updatePPOB.{UpdatePPOB, UpdatePPOBAddress}
import play.api.mvc.AnyContent
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PPOBService @Inject()(subscriptionConnector: SubscriptionConnector,
                            val auditService: AuditService) {


  private def buildPPOBUpdateModel(addressModel: AddressModel, circumstanceDetails: CircumstanceDetails) = {

    val updateAddress: UpdatePPOBAddress = UpdatePPOBAddress(
      line1 = addressModel.line1,
      line2 = addressModel.line2,
      line3 = addressModel.line3,
      line4 = addressModel.line4,
      postCode = addressModel.postcode,
      nonUkCountryCode = addressModel.countryCode.filterNot(_ == "GB")
    )
    UpdatePPOB(updateAddress, circumstanceDetails.ppob.contactDetails, circumstanceDetails.ppob.websiteAddress)
  }


  def updatePPOB(user: User[_], address: AddressModel, id: String)
                (implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorModel, (SubscriptionUpdateResponseModel, PPOBAddress)]] = {

    subscriptionConnector.getCustomerCircumstanceDetails(user.vrn) flatMap {
      case Right(customerDetails) =>
        subscriptionConnector.updatePPOB(user.vrn, buildPPOBUpdateModel(address, customerDetails)) map {
          case Right(success) =>
            auditService.extendedAudit(
              ChangeAddressAuditModel(user, customerDetails.ppobAddress, address, success.formBundle),
              Some(controllers.routes.BusinessAddressController.callback(id).url)
            )
            Right(success, customerDetails.ppobAddress)
          case Left(error) => Left(error)
        }
      case Left(error) => Future.successful(Left(error))
    }
  }
}
