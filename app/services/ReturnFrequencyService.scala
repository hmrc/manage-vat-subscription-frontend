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

import connectors.SubscriptionConnector
import javax.inject.{Inject, Singleton}
import models.returnFrequency.ReturnPeriod
import models.core.{ErrorModel, SubscriptionUpdateResponseModel}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReturnFrequencyService @Inject()(subscriptionConnector: SubscriptionConnector) {

  def updateReturnFrequency(vrn: String, frequency: ReturnPeriod)
                           (implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorModel, SubscriptionUpdateResponseModel]] = {
    subscriptionConnector.updateReturnFrequency(vrn, frequency)
  }
}
