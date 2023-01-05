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

package forms

import forms.MissingTraderForm._
import models.{No, Yes}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.data.FormError

class MissingTraderFormSpec extends AnyWordSpecLike with Matchers {

  "MissingTraderForm" should {

    "successfully parse a Yes" in {
      val res = missingTraderForm.bind(Map(yesNo -> yes))
      res.value should contain(Yes)
    }

    "successfully parse a No" in {
      val res = missingTraderForm.bind(Map(yesNo -> MissingTraderForm.no))
      res.value should contain(No)
    }

    "fail when nothing has been entered" in {
      val res = missingTraderForm.bind(Map.empty[String, String])
      res.errors should contain(FormError(yesNo, missingTraderError))
      res.errors.size shouldBe 1
    }

    "A form built from a valid model" when {

      "'Yes' is selected" should {

        "generate the correct mapping" in {
          val form = missingTraderForm.fill(Yes)
          form.data shouldBe Map(yesNo -> "yes")
        }
      }

      "'No' is selected" should {

        "generate the correct mapping" in {
          val form = missingTraderForm.fill(No)
          form.data shouldBe Map(yesNo -> "no")
        }
      }
    }
  }
}