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

package forms

import models.agentClientRelationship.ClientVrnModel
import uk.gov.hmrc.play.test.UnitSpec

class ClientVrnFormSpec extends UnitSpec {

  "Binding a form with invalid data" when {

    "the VRN is missing" should {

      val missingVrn = Map("vrn" -> "")
      val form = ClientVrnForm.form.bind(missingVrn)

      "result in a form with errors" in {
        form.hasErrors shouldBe true
      }

      "throw one error" in {
        form.errors.size shouldBe 1
      }

      "have an error with the correct message" in {
        form.errors.head.message shouldBe "clientVrnForm.vrn.missing"
      }
    }

    "the VRN is not in the correct format" should {

      val invalidVrn = Map("vrn" -> "dsa")
      val form  = ClientVrnForm.form.bind(invalidVrn)

      "result in a form with errors" in {
        form.hasErrors shouldBe true
      }

      "throw one error" in {
        form.errors.size shouldBe 1
      }

      "have an error with the correct message" in {
        form.errors.head.message shouldBe "clientVrnForm.vrn.invalid"
      }
    }
  }

  "Binding a form with valid data" should {

    val data = Map("vrn" -> "123456789")
    val form = ClientVrnForm.form.bind(data)

    "result in a form with no errors" in {
      form.hasErrors shouldBe false
    }

    "generate the correct model" in {
      form.value shouldBe Some(ClientVrnModel("123456789"))
    }
  }

  "A form built from a valid model" should {
    "generate the correct mapping" in {
      val model = ClientVrnModel("123456789")
      val form = ClientVrnForm.form.fill(model)
      form.data shouldBe Map("vrn" -> "123456789")
    }
  }

}
