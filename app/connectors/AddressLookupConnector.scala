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
import models.customerAddress.{AddressLookupJsonBuilder, CustomerAddressModel}
import play.api.http.HeaderNames._
import play.api.http.HttpVerbs._
import play.api.http.Status
import play.api.mvc.Call
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressLookupConnector @Inject()(val http: HttpClient,
                                       val config: FrontendAppConfig) {

  def initaliseJourney(addressLookupJsonBuilder: AddressLookupJsonBuilder)
                      (implicit hc: HeaderCarrier,ec: ExecutionContext): Future[Either[ErrorModel, Call]] = {

    val url = s"${config.addressLookupUrl}/api/init"

    http.POST[AddressLookupJsonBuilder,HttpResponse](url,addressLookupJsonBuilder) map { resp =>
      resp.header(LOCATION).map(Call(GET, _)) match {
        case Some(call) => Right(call)
        case _ => Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "Response Header did not contain redirect call"))
      }
    }
  }

  def getAddress(id: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpGetResult[CustomerAddressModel]] ={
    http.GET[HttpGetResult[CustomerAddressModel]](s"${config.addressLookupUrl}/api/confirmed?id=$id")(AddressLookupReads,hc,ec)
  }
}
