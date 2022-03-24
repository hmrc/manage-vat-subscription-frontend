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

package connectors

import config.AppConfig
import connectors.httpParsers.CustomerCircumstancesHttpParser
import connectors.httpParsers.ResponseHttpParser._
import connectors.httpParsers.SubscriptionUpdateHttpParser.SubscriptionUpdateReads
import javax.inject.{Inject, Singleton}
import models.circumstanceInfo.CircumstanceDetails
import models.core.SubscriptionUpdateResponseModel
import models.updatePPOB.{UpdatePPOB, UpdatePPOBAddress}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpClient
import utils.LoggerUtil

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubscriptionConnector @Inject()(val http: HttpClient,
                                      val config: AppConfig) extends LoggerUtil {

  private[connectors] def getCustomerDetailsUrl(vrn: String) = s"${config.vatSubscriptionUrl}/vat-subscription/$vrn/full-information"

  private[connectors] def updateBusinessAddressUrl(vrn: String) = s"${config.vatSubscriptionUrl}/vat-subscription/$vrn/ppob"

  private[connectors] def updateReturnPeriod(vrn: String) = s"${config.vatSubscriptionUrl}/vat-subscription/$vrn/return-period"

  def getCustomerCircumstanceDetails(id: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[HttpGetResult[CircumstanceDetails]] = {
    val url = getCustomerDetailsUrl(id)
    logger.debug(s"[CustomerDetailsConnector][getCustomerDetails]: Calling getCustomerDetails with URL - $url")
    http.GET(url)(CustomerCircumstancesHttpParser.CustomerCircumstanceReads, headerCarrier, ec)
  }

  def updatePPOB(vrn: String, ppob: UpdatePPOB)
                (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpPutResult[SubscriptionUpdateResponseModel]] = {
    http.PUT[UpdatePPOB, HttpPostResult[SubscriptionUpdateResponseModel]](updateBusinessAddressUrl(vrn), ppob)
  }

  def validateBusinessAddress(vrn: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpPutResult[SubscriptionUpdateResponseModel]] = {
    updatePPOB(vrn, UpdatePPOB(UpdatePPOBAddress(None, None, None, None, None, None), None, None, None))
  }
}
