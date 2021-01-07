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

package assets.messages

object ChangeAddressPageMessages extends BaseMessages {
  val title: String = "Change where the business does most of its work" + titleSuffixUser
  val heading: String = "Change where the business does most of its work"
  val p1: String = "This is also known as the principal place of business."
  val p2: String = "If this is in different locations, use the address where it keeps its business records. This could be a home address."
  val bulletHeader: String = "You cannot use:"
  val bullet1: String = "the address of a third-party accountant or tax agent"
  val bullet2: String = "a PO box address"
  val bullet3: String = "a ‘care of’ address"
}
