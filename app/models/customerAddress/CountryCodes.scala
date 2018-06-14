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

package models.customerAddress

import config.AppConfig
import play.api.libs.json.{Format, Json}

object CountryCodes {

  case class Country(country: String, countryCode: String)

  object Country {
    implicit val format: Format[Country] = Json.format[Country]
  }

  private def countryCodesMap(implicit appConfig: AppConfig): Map[String, String] =
    appConfig.countryCodeJson.as[List[Country]].map(country => (country.countryCode, country.country)).toMap

  def getCountry(countryCode: String)(implicit appConfig: AppConfig): Option[String] = countryCodesMap.get(countryCode)

}