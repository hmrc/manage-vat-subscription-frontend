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

package stubs

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import helpers.WireMockMethods
import models.circumstanceInfo.CircumstanceDetails
import play.api.http.Status.{INTERNAL_SERVER_ERROR, OK}
import play.api.libs.json.Json

object VatSubscriptionStub extends WireMockMethods {

  private val subscriptionUri: String => String = vrn => s"/vat-subscription/$vrn/full-information"

  def getClientDetailsSuccess(vrn: String)(customerDetails: CircumstanceDetails): StubMapping = {
    when(method = GET, uri = subscriptionUri(vrn))
      .thenReturn(status = OK, body = Json.toJson(customerDetails)(CircumstanceDetails.writes))
  }

  def getClientDetailsError(vrn: String): StubMapping = {
    when(method = GET, uri = subscriptionUri(vrn))
      .thenReturn(status = INTERNAL_SERVER_ERROR, body = Json.obj("code" -> "OH NO"))
  }
}
