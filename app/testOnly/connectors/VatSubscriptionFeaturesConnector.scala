/*
 * Copyright 2023 HM Revenue & Customs
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

package testOnly.connectors

import javax.inject.Inject
import testOnly.TestOnlyAppConfig
import testOnly.models.VatSubscriptionFeatureSwitchModel
import uk.gov.hmrc.http.HttpReads.Implicits
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpReads, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

class VatSubscriptionFeaturesConnector @Inject()(val http: HttpClient,
                                                 val appConfig: TestOnlyAppConfig) {

  implicit val implicitsReadRaw: HttpReads[HttpResponse] = Implicits.readRaw
  implicit val implicitsReadFromJson: HttpReads[VatSubscriptionFeatureSwitchModel] = Implicits.readFromJson

  def getFeatures(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[VatSubscriptionFeatureSwitchModel] = {
    lazy val url = s"${appConfig.vatSubscriptionUrl}/vat-subscription/test-only/feature-switch"
    http.GET[VatSubscriptionFeatureSwitchModel](url)
  }

  def postFeatures(vatSubscriptionFeatures: VatSubscriptionFeatureSwitchModel)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    lazy val url = s"${appConfig.vatSubscriptionUrl}/vat-subscription/test-only/feature-switch"
    http.POST[VatSubscriptionFeatureSwitchModel, HttpResponse](url, vatSubscriptionFeatures)
  }

}
