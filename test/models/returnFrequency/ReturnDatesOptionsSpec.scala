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

package models.returnFrequency

import org.scalatestplus.play.OneAppPerSuite
import uk.gov.hmrc.play.test.UnitSpec
import play.api.i18n.Messages.Implicits._

class ReturnDatesOptionsSpec extends UnitSpec with OneAppPerSuite {

  "Jan().label" should {

    "return the correct message" in {
      Jan().label shouldBe "January, April, July and October"
    }
  }

  "Feb().label" should {

    "return the correct message" in {
      Feb().label shouldBe "February, May, August and November"
    }
  }

  "Mar().label" should {

    "return the correct message" in {
      Mar().label shouldBe "March, June, September and December"
    }
  }

  "All().label" should {

    "return the correct message" in {
      All().label shouldBe "Every month"
    }
  }

}
