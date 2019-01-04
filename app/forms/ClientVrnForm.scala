/*
 * Copyright 2019 HM Revenue & Customs
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

import forms.helpers.ConstraintHelper._
import models.agentClientRelationship.ClientVrnModel
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid}

object ClientVrnForm {

  private val vrnPattern = """^\d{9}$"""
  private val emptyCheck: Constraint[String] = Constraint("Missing VRN")(vrn =>
    if (vrn.nonEmpty) Valid else Invalid("clientVrnForm.vrn.missing")
  )
  private val invalidVrn: Constraint[String] = Constraint("Invalid VRN")(vrn =>
    if (vrn.matches(vrnPattern)) Valid else Invalid("clientVrnForm.vrn.invalid")
  )

  val trimmedVrn: String => String = vrn => vrn.trim

  val form: Form[ClientVrnModel] = Form(
    mapping(
      "vrn" -> text
        .transform(trimmedVrn, trimmedVrn)
        .verifying(emptyCheck andThen invalidVrn)
    )(ClientVrnModel.apply)(ClientVrnModel.unapply)
  )
}
