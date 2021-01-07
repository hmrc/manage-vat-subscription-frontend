/*
 * Copyright 2021 HM Revenue & Customs
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

package forms.helpers

import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.data.validation.{Constraint, Invalid, Valid}
import utils.TestUtil
import ConstraintHelper._

class ConstraintHelperSpec extends TestUtil {


  case class DummyModel(a: String, b: String, c: String, d: String)

  object DummyForm {

    val dummyConstraint1: Constraint[String] = Constraint("Dummy Constraint 1")(_ => Valid)
    val dummyConstraint2: Constraint[String] = Constraint("Dummy Constraint 2")(_ => Valid)
    val dummyConstraint3: Constraint[String] = Constraint("Dummy Constraint 3")(_ => Invalid("Test Error 1"))
    val dummyConstraint4: Constraint[String] = Constraint("Dummy Constraint 4")(_ => Invalid("Test Error 2"))

    val form: Form[DummyModel] = Form(
      mapping(
        "a" -> text.verifying(dummyConstraint1 andThen dummyConstraint2),
        "b" -> text.verifying(dummyConstraint1 or dummyConstraint3),
        "c" -> text.verifying(dummyConstraint1 andThen dummyConstraint3),
        "d" -> text.verifying(dummyConstraint3 or dummyConstraint2 or dummyConstraint4)
      )(DummyModel.apply)(DummyModel.unapply)
    )
  }

  "The constraint helper" should {

    "Return an error for field C; field A and B pass" in {

      val testForm = DummyForm.form.bind(Map(
        "a" -> "foo",
        "b" -> "bar",
        "c" -> "car",
        "d" -> "dar"
      ))

      testForm.hasErrors shouldBe true
      testForm.error("a") shouldBe None
      testForm.error("b") shouldBe None
      testForm.error("c").map(_.message shouldBe "Test Error 1")
      testForm.error("d").map(_.message shouldBe "Test Error 2")
    }
  }
}
