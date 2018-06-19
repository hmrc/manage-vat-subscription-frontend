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

import helpers.IntegrationTestConstants.VRN
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import helpers.WireMockMethods
import models.core.SubscriptionUpdateResponseModel
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.Json

object ReturnFrequencyStub extends WireMockMethods {

  private val subscriptionUri: String => String = vrn => s"/vat-subscription/$vrn/return-period"

  def postSubscriptionSuccess(response: SubscriptionUpdateResponseModel): StubMapping = {
    when(method = POST, uri = subscriptionUri(VRN))
      .thenReturn(status = OK, body = Json.toJson(response))
  }

  def postSubscriptionError(): StubMapping = {
    when(method = POST, uri = subscriptionUri(VRN))
      .thenReturn(status = BAD_REQUEST, body = Json.obj("code" -> "Terry Bell Tings"))
  }

}
