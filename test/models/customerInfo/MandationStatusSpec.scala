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

package models.customerInfo

import play.api.libs.json.JsString
import uk.gov.hmrc.play.test.UnitSpec


class MandationStatusSpec extends UnitSpec {

  val mandatedKey = "MTDfB Mandated"
  val voluntaryKey = "MTDfB Voluntary"
  val nonMtdfbKey = "Non MTDfB"
  val nonDigitalKey = "Non Digital"

  "MandationStatus" should {

    "Have the correct status for Mandated" in {
      MTDfBMandated.status shouldBe mandatedKey
    }
    "Have the correct status for Voluntary" in {
      MTDfBVoluntary.status shouldBe voluntaryKey
    }
    "Have the correct status for Non-MTDfB" in {
      NonMTDfB.status shouldBe nonMtdfbKey
    }
    "Have the correct status for Non Digital" in {
      NonDigital.status shouldBe nonDigitalKey
    }

    "When deserializing from JSON" should {

      s"Parse '$mandatedKey' into  MTDfBMandated" in {
        MandationStatus.reads.reads(JsString(mandatedKey)).get shouldBe MTDfBMandated
      }
      s"parse '$voluntaryKey' into  MTDfBVoluntary" in {
        MandationStatus.reads.reads(JsString(voluntaryKey)).get shouldBe MTDfBVoluntary
      }
      s"parse '$nonMtdfbKey' into  NonMTDfB" in {
        MandationStatus.reads.reads(JsString(nonMtdfbKey)).get shouldBe NonMTDfB
      }
      s"parse '$nonDigitalKey' into  NonMTDfB" in {
        MandationStatus.reads.reads(JsString(nonDigitalKey)).get shouldBe NonDigital
      }
    }
  }

  "When serializing to JSON" should {

    s"Write MTDfBMandated to '$mandatedKey'" in {
      MandationStatus.writes.writes(MTDfBMandated) shouldBe JsString(mandatedKey)
    }
    s"Write MTDfBVoluntary to '$voluntaryKey'" in {
      MandationStatus.writes.writes(MTDfBVoluntary) shouldBe JsString(voluntaryKey)
    }
    s"Write NonMTDfB to '$nonMtdfbKey'" in {
      MandationStatus.writes.writes(NonMTDfB) shouldBe JsString(nonMtdfbKey)
    }
    s"Write NonDigital to '$nonDigitalKey'" in {
      MandationStatus.writes.writes(NonDigital) shouldBe JsString(nonDigitalKey)
    }
  }

}
