/*
 * Copyright 2020 HM Revenue & Customs
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
import models.customerAddress.AddressLookupOnRampModel
import play.api.http.HeaderNames.LOCATION
import play.api.http.Status.OK
import play.api.libs.json.{JsValue, Json}

object BusinessAddressStub extends WireMockMethods {

  private val subscriptionUri: String => String = vrn => s"/vat-subscription/$vrn/ppob"
  private val addressUri = "/api/confirmed.*"
  private val initUri = "/api/init"
  private val initUriV2 = "/api/v2/init"
  private val fullAddressUri: String => String = vrn => s"/vat-subscription/$vrn/full-information"

  def postInitJourney(status: Int, response: AddressLookupOnRampModel, body: Option[String] = None): StubMapping = {
    when(method = POST, uri = initUri, body = body)
      .thenReturn(status = status, headers = Map(LOCATION -> response.redirectUrl))
  }

  def postInitV2Journey(status: Int, response: AddressLookupOnRampModel, body: Option[String] = None): StubMapping = {
    when(method = POST, uri = initUriV2, body = body)
      .thenReturn(status = status, headers = Map(LOCATION -> response.redirectUrl))
  }

  def getAddress(status: Int, response: JsValue): StubMapping = {
    when(method = GET, uri = addressUri)
      .thenReturn(status = OK, body = response)
  }

  def getFullInformation(status: Int, response: JsValue): StubMapping = {
    when(method = GET, uri = fullAddressUri(VRN))
      .thenReturn(status = OK, body = Json.toJson(response))
  }

  def putSubscription(status: Int, response: JsValue): StubMapping = {
    when(method = PUT, uri = subscriptionUri(VRN))
      .thenReturn(status = status, body = response)
  }
}
