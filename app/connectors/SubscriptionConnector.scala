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

package connectors

import config.FrontendAppConfig
import connectors.httpParsers.ResponseHttpParser._
import connectors.httpParsers.CustomerCircumstancesHttpParser.CustomerCircumstanceReads
import javax.inject.{Inject, Singleton}
import models.circumstanceInfo.{CircumstanceDetails, CustomerDetails}
import models.customerAddress.AddressModel
import models.core.SubscriptionUpdateResponseModel
import models.returnFrequency.ReturnDateOption
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubscriptionConnector @Inject()(val http: HttpClient,
                                      val config: FrontendAppConfig) {

  private[connectors] def getCustomerDetailsUrl(vrn: String) = s"${config.vatSubscriptionUrl}/vat-subscription/$vrn/customer-details"

  def getCustomerDetails(id: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[HttpGetResult[CircumstanceDetails]] = {
    val url = getCustomerDetailsUrl(id)
    Logger.debug(s"[CustomerDetailsConnector][getCustomerDetails]: Calling getCustomerDetails with URL - $url")
    http.GET(url)(CustomerCircumstanceReads, headerCarrier, ec)
  }

  def updateBusinessAddress(vrn: String, address: AddressModel)
                           (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpPutResult[SubscriptionUpdateResponseModel]] = {
    // TODO: call vat-subscription
    Future.successful(Right(SubscriptionUpdateResponseModel("12345")))
  }

  def updateReturnFrequency(vrn: String, frequency: ReturnDateOption)
                           (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpPutResult[SubscriptionUpdateResponseModel]] = {
    // TODO: call vat-subscription
    Future.successful(Right(SubscriptionUpdateResponseModel("12345")))
  }
}
