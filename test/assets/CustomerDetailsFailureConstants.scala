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

/**
  * Test constants when calls to DSES CustomerDetails fail
  */
object CustomerDetailsFailureConstants {

  val notFound =
    """
      |{"code": "NOT_FOUND", "reason": "The back end has indicated that No subscription can be found."}
    """.stripMargin

  val invalidVRN =
    """
      |{"code": "INVALID VRN", "reason": "Submission has not passed validation. Invalid idType/idValue."}
    """.stripMargin

  val serviceUnavailable =
    """
      |{"code": "SERVICE_UNAVAILABLE", "reason": "Dependent systems are currently not responding"}
    """.stripMargin

  val serverError =
    """
      |{"code": "SERVER_ERROR", "reason": "DES is currently experiencing problems that require live service intervention."}
      |
    """.stripMargin

}
