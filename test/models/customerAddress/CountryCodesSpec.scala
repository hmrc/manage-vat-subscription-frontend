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

package models.customerAddress

import utils.TestUtil

class CountryCodesSpec extends TestUtil {

  "CountryCodeTest" should {
    "successfully convert a country code to a country" in {
      CountryCodes.getCountry("GB")(mockConfig) should be (Some("United Kingdom"))
    }
    "return None if a country code is not found" in {
      CountryCodes.getCountry("ZZ")(mockConfig) should be (None)
    }
  }
}
