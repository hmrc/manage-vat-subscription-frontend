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

import javax.inject.{Inject, Singleton}

import config.FrontendAppConfig
import connectors.httpParsers.CustomerDetailsHttpParser._
import models.customerInfo.CustomerInformationModel
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CustomerDetailsConnector @Inject()(val http: HttpClient,
                                         val config: FrontendAppConfig) {

  private[connectors] def getCustomerDetailsUrl(vatNumber: String) = s"${config.vatSubscriptionUrl}/vat-subscription/$vatNumber/customer-details"

  def getCustomerDetails(id: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[HttpGetResult[CustomerInformationModel]] = {

    val url = getCustomerDetailsUrl(id)

    http.GET(url)(CustomerDetailsReads, headerCarrier, ec).map {
      case customerDetails@Right(_) => {
        Logger.debug("[CustomerDetailsConnector][getCustomerDetails]: Right")
        customerDetails
      }
      case error@Left(_) => {
        Logger.debug("[CustomerDetailsConnector][getCustomerDetails]: Left")
        error
      }
    }

  }
}