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

package assets.messages

object ChangeBusinessNamePageMessages {

  val title = "Change of business name"
  val h1 = title

  val p1: String => String = name => s"""You can change the business name from $name."""
  val tradingNameMessage = "You do not need to tell us if the business will use a different trading name."
  val p2 = "We will update the business name with HMRC and Companies House. This can take up to xx working days."

}
