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

package models.customerInfo

import play.api.libs.json.{Json, Format}

case class CustomerDetailsModel(firstName: Option[String],
                                lastName: Option[String],
                                organisationName: Option[String],
                                tradingName: Option[String]) {

  val isOrg: Boolean = organisationName.isDefined
  val isInd: Boolean = firstName.isDefined || firstName.isDefined
  val userName: Option[String] = (firstName, lastName) match {
    case (Some(fName), Some(sName)) => Some(fName + " " + sName)
    case (Some(fName), _) => Some(fName)
    case (_, Some(sName)) => Some(sName)
    case (_,_) => None
  }
}

object CustomerDetailsModel {
  implicit val format: Format[CustomerDetailsModel] = Json.format[CustomerDetailsModel]
}