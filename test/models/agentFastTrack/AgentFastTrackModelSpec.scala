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

package models.agentFastTrack

import assets.BaseTestConstants.vrn
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec

class AgentFastTrackModelSpec extends UnitSpec {

  "AgentFastTrackModel Writes" should {

    "Serialise to JSON correctly" in {
      Json.toJson(AgentFastTrackModel(vrn)) shouldBe Json.obj(
        "service" -> "HMRC-MTD-VAT",
        "clientIdentifierType" -> "vrn",
        "clientIdentifier" -> vrn
      )
    }
  }
}
