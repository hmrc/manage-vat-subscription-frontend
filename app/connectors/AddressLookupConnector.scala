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
import connectors.httpParsers.AddressLookupHttpParser._
import models.core.ErrorModel
import models.customerAddress.{AddressLookupJsonBuilder, AddressLookupOnRampModel, CustomerAddressModel}
import play.api.Logger
import play.api.http.HeaderNames._
import play.api.http.Status
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressLookupConnector @Inject()(val http: HttpClient,
                                       val config: FrontendAppConfig) {

  def initialiseJourney(addressLookupJsonBuilder: AddressLookupJsonBuilder)
                      (implicit hc: HeaderCarrier,ec: ExecutionContext): Future[HttpPostResult[AddressLookupOnRampModel]] = {

    val url = s"${config.addressLookupUrl}/api/init"

    http.POST[AddressLookupJsonBuilder,HttpResponse](url,addressLookupJsonBuilder) map { resp =>
      resp.status match {
        case Status.ACCEPTED =>
          resp.header(LOCATION) match {
            case Some(redirectUrl) => Right(AddressLookupOnRampModel(redirectUrl))
            case _ =>
              Logger.warn(s"[AddressLookupConnector][initialiseJourney]: Response Header did not contain location redirect")
              Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Response Header did not contain location redirect"))
          }
        case status =>
          Logger.warn(s"[AddressLookupConnector][initialiseJourney]: Unexpected Response, Status $status returned")
          Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Downstream error returned from Address Lookup"))
      }
    }
  }

  private[connectors] def getAddressUrl(id: String) = s"${config.addressLookupUrl}/api/confirmed?id=$id"

  def getAddress(id: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpGetResult[CustomerAddressModel]] ={
    http.GET[HttpGetResult[CustomerAddressModel]](getAddressUrl(id))(AddressLookupReads,hc,ec)
  }
}
