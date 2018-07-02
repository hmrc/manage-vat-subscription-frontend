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

package stubs

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import helpers.IntegrationTestConstants.VRN
import helpers.WireMockMethods
import models.core.SubscriptionUpdateResponseModel
import models.customerAddress.AddressModel
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.Json

object BusinessAddressStub extends WireMockMethods {

  private val subscriptionUri: String => String = vrn => s"/vat-subscription/$vrn/ppob"
  private val addressUri: String => String = vrn => s"/api/confirmed?id=$vrn"

  def putSubscriptionSuccess(response: SubscriptionUpdateResponseModel): StubMapping = {
    when(method = PUT, uri = subscriptionUri(VRN))
      .thenReturn(status = OK, body = Json.toJson(response))
  }

  def getAddress(response: AddressModel): StubMapping = {
    when(method = GET, uri = addressUri(VRN))
      .thenReturn(status = OK, body = Json.toJson(response))
  }

  def putSubscriptionError(): StubMapping = {
    when(method = PUT, uri = subscriptionUri(VRN))
      .thenReturn(status = BAD_REQUEST, body = Json.obj("code" -> "Terry Bell Tings"))
  }

}
