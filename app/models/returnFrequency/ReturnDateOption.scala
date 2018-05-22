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

sealed trait ReturnDateOption {
  val id: String
}

case object Jan extends ReturnDateOption {
  override val id: String = "January"
}

case object Feb extends ReturnDateOption {
  override val id: String = "February"
}

case object Mar extends ReturnDateOption {
  override val id: String = "March"
}

case object Monthly extends ReturnDateOption {
  override val id: String = "Monthly"
}
