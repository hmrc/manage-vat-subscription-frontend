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

import config.AppConfig
import connectors.AddressLookupConnector
import javax.inject.{Inject, Singleton}
import models.customerAddress.{AddressLookupJsonBuilder, AddressLookupOnRampModel, AddressModel}
import models.core.ErrorModel
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressLookupService @Inject()(addressLookupConnector: AddressLookupConnector,
                                     appConfig: AppConfig) {

  def retrieveAddress(id: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorModel, AddressModel]] = {
    addressLookupConnector.getAddress(id)
  }

  def initialiseJourney(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorModel, AddressLookupOnRampModel]] = {
    val addressLookupJsonBuilder: AddressLookupJsonBuilder = AddressLookupJsonBuilder(appConfig.addressLookupCallbackUrl)
    addressLookupConnector.initialiseJourney(addressLookupJsonBuilder)
  }
}