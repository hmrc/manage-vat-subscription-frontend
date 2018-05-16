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

import play.api.i18n.Messages

sealed trait ReturnDateOptions {
  def label(implicit messages: Messages): String
}

case class Jan() extends ReturnDateOptions {
  def label(implicit messages: Messages): String = messages("return_frequency.option1")
}
case class Feb() extends ReturnDateOptions {
  def label(implicit messages: Messages): String = messages("return_frequency.option2")
}
case class Mar() extends ReturnDateOptions {
  def label(implicit messages: Messages): String = messages("return_frequency.option3")
}
case class All() extends ReturnDateOptions {
  def label(implicit messages: Messages): String = messages("return_frequency.option4")
}
