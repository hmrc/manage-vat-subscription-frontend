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

package assets

import models.viewModels.AltChangeBusinessNameViewModel

object ViewModelsTestConstants {

  val orgName = "Ancient Antiques Ltd"
  val trustLinkUrl = "/gov-uk/trust-name-change"
  val linkUrl = "mock-govUk-changeBusinessDetails"
  val userP1 = "change_business_name.alternative.user1"
  val userP2 = "change_business_name.alternative.user2"
  val userP2Trust = "change_business_name.alternative.user.charity"
  val agentP1 = "change_business_name.alternative.agent1"
  val agentP2 = "change_business_name.alternative.agent2"
  val agentP2Trust = "change_business_name.alternative.agent.charity"

  val trustBusinessNameViewModel: AltChangeBusinessNameViewModel = AltChangeBusinessNameViewModel(
    linkUrl = trustLinkUrl,
    orgName = orgName,
    p1Message = userP1,
    p2Message = "change_business_name.alternative.user.charity"
  )

  val businessNameViewModel: AltChangeBusinessNameViewModel = AltChangeBusinessNameViewModel(
    linkUrl = linkUrl,
    orgName = orgName,
    p1Message = userP1,
    p2Message = "change_business_name.alternative.user2"
  )

  val businessNameViewModelAgent: AltChangeBusinessNameViewModel = AltChangeBusinessNameViewModel(
    linkUrl = linkUrl,
    orgName = orgName,
    p1Message = agentP1,
    p2Message = "change_business_name.alternative.agent2"
  )

  val trustBusinessNameViewModelAgent: AltChangeBusinessNameViewModel = AltChangeBusinessNameViewModel(
    linkUrl = trustLinkUrl,
    orgName = orgName,
    p1Message = agentP1,
    p2Message = "change_business_name.alternative.agent.charity"
  )
}
