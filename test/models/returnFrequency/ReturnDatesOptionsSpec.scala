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
      Jan.id shouldBe "January"
    }
  }

  "Feb().label" should {

    "return the correct message" in {
      Feb.id shouldBe "February"
    }
  }

  "Mar().label" should {

    "return the correct message" in {
      Mar.id shouldBe "March"
    }
  }

  "All().label" should {

    "return the correct message" in {
      Monthly.id shouldBe "Monthly"
    }
  }

}
