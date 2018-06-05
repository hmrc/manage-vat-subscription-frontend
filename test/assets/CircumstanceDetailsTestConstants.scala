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

package assets

object CircumstanceDetailsTestConstants {

  val fullPopulation =
    """
      |{"organisationName":"Ancient Antiques",
      |"flatRateScheme":{"FRSCategory":"001","FRSPercentage":123.12,"limitedCostTrader":true,"startDate":"2001-01-01"},
      |"PPOB":{"address":{"line1":"VAT ADDR 1","line2":"VAT ADDR 2","line3":"VAT ADDR 3","line4":"VAT ADDR 4","postCode":"SW1A 2BQ","countryCode":"ES"}},
      |"bankDetails":{"accountHolderName":"***********************","bankAccountNumber":"****5274","sortCode":"77****"},
      |"returnPeriod":{"stdReturnPeriod":"MM"}}
    """.stripMargin

  val noBanking =
    """
      |{"organisationName":"Ancient Antiques",
      |"flatRateScheme":{"FRSCategory":"001","FRSPercentage":123.12,"limitedCostTrader":true,"startDate":"2001-01-01"},
      |"PPOB":{"address":{"line1":"VAT ADDR 1","line2":"VAT ADDR 2","line3":"VAT ADDR 3","line4":"VAT ADDR 4","postCode":"SW1A 2BQ","countryCode":"ES"}},
      |"returnPeriod":{"stdReturnPeriod":"MM"}}
    """.stripMargin

  val noBusinessName =
    """
      |{"flatRateScheme":{"FRSCategory":"001","FRSPercentage":123.12,"limitedCostTrader":true,"startDate":"2001-01-01"},
      |"PPOB":{"address":{"line1":"VAT ADDR 1","line2":"VAT ADDR 2","line3":"VAT ADDR 3","line4":"VAT ADDR 4","postCode":"SW1A 2BQ","countryCode":"ES"}},
      |"bankDetails":{"accountHolderName":"***********************","bankAccountNumber":"****5274","sortCode":"77****"},
      |"returnPeriod":{"stdReturnPeriod":"MM"}}
    """.stripMargin

  val noPPOB =
    """
      |{"organisationName":"Ancient Antiques",
      |"flatRateScheme":{"FRSCategory":"001","FRSPercentage":123.12,"limitedCostTrader":true,"startDate":"2001-01-01"},
      |"bankDetails":{"accountHolderName":"***********************","bankAccountNumber":"****5274","sortCode":"77****"},
      |"returnPeriod":{"stdReturnPeriod":"MM"}}
    """.stripMargin

  val noFlatRateScheme =
    """
      |{"organisationName":"Ancient Antiques",
      |"PPOB":{"address":{"line1":"VAT ADDR 1","line2":"VAT ADDR 2","line3":"VAT ADDR 3","line4":"VAT ADDR 4","postCode":"SW1A 2BQ","countryCode":"ES"}},
      |"bankDetails":{"accountHolderName":"***********************","bankAccountNumber":"****5274","sortCode":"77****"},
      |"returnPeriod":{"stdReturnPeriod":"MM"}}
    """.stripMargin

  val noReturnPeriod =
    """
      |{"organisationName":"Ancient Antiques",
      |"flatRateScheme":{"FRSCategory":"001","FRSPercentage":123.12,"limitedCostTrader":true,"startDate":"2001-01-01"},
      |"PPOB":{"address":{"line1":"VAT ADDR 1","line2":"VAT ADDR 2","line3":"VAT ADDR 3","line4":"VAT ADDR 4","postCode":"SW1A 2BQ","countryCode":"ES"}},
      |"bankDetails":{"accountHolderName":"***********************","bankAccountNumber":"****5274","sortCode":"77****"}}
    """.stripMargin

}
