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

package forms

import models.returnFrequency.ReturnDatesModel
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class ChooseDatesFormSpec extends AnyWordSpecLike with Matchers {

  "Binding a form with invalid data" when {

    "the period-option is missing" should {

      val missingOption: Map[String, String] = Map.empty
      val form = chooseDatesForm.datesForm.bind(missingOption)

      "result in a form with errors" in {
        form.hasErrors shouldBe true
      }

      "throw one error" in {
        form.errors.size shouldBe 1
      }

      "have an error with the correct message" in {
        form.errors.head.message shouldBe "chooseDatesForm.frequency.missing"
      }
    }
  }

  "Binding a form with valid data" should {

    val data = Map("period-option" -> "January")
    val form = chooseDatesForm.datesForm.bind(data)

    "result in a form with no errors" in {
      form.hasErrors shouldBe false
    }

    "generate the correct model" in {
      form.value shouldBe Some(ReturnDatesModel("January"))
    }
  }

  "A form built from a valid model" should {
    "generate the correct mapping" in {
      val model = ReturnDatesModel("January")
      val form = chooseDatesForm.datesForm.fill(model)
      form.data shouldBe Map("period-option" -> "January")
    }
  }

}
