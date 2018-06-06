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
import helpers.WireMockMethods
import models.payments.PaymentRedirectModel
import play.api.http.Status.{OK, BAD_REQUEST}
import play.api.libs.json.Json

object PaymentStub extends WireMockMethods {

  private val paymentsUri: String = "/bank-account-coc/start-journey-of-change-bank-account"

  def postPaymentSuccess(paymentRedirect: PaymentRedirectModel): StubMapping = {
    when(method = POST, uri = paymentsUri)
      .thenReturn(status = OK, body = Json.toJson(paymentRedirect))
  }

  def postPaymentError(): StubMapping = {
    when(method = POST, uri = paymentsUri)
      .thenReturn(status = BAD_REQUEST, body = Json.obj("code" -> "Terry Bell Tings"))
  }

}
