/*
 * Copyright 2024 HM Revenue & Customs
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

package models.viewModels

import config.AppConfig
import models.User
import models.circumstanceInfo.CircumstanceDetails

case class AltChangeBusinessNameViewModel(orgName: String,
                                          linkUrl: String,
                                          p1Message: String,
                                          p2Message: String)

object AltChangeBusinessNameViewModel {

  def trustBusinessNameViewModel(circumstances: CircumstanceDetails)
                                (implicit appConfig: AppConfig,
                                 user: User[_]): AltChangeBusinessNameViewModel = {
    val p1message: String = {
      if(user.isAgent) "change_business_name.alternative.agent1" else "change_business_name.alternative.user1"
    }
    val p2message: String = {
      if(user.isAgent) "change_business_name.alternative.agent.charity" else "change_business_name.alternative.user.charity"
    }

    AltChangeBusinessNameViewModel(
      circumstances.customerDetails.organisationName.get,
      appConfig.govUkTrustNameChangeUrl,
      p1message,
      p2message
    )
  }

  def businessNameViewModel(circumstances: CircumstanceDetails)
                           (implicit appConfig: AppConfig,
                            user: User[_]): AltChangeBusinessNameViewModel = {
    val p1message: String = {
      if(user.isAgent) "change_business_name.alternative.agent1" else "change_business_name.alternative.user1"
    }
    val p2message: String = {
      if(user.isAgent) "change_business_name.alternative.agent2" else "change_business_name.alternative.user2"
    }

    AltChangeBusinessNameViewModel(
      circumstances.customerDetails.organisationName.get,
      appConfig.govUkChangeToBusinessDetails,
      p1message,
      p2message
    )
  }
}
