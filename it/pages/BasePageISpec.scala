/*
 * Copyright 2020 HM Revenue & Customs
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

package pages

import assets.BaseITConstants.internalServerErrorTitle
import common.SessionKeys
import helpers.BaseIntegrationSpec
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.{INTERNAL_SERVER_ERROR, SEE_OTHER}

trait BasePageISpec extends BaseIntegrationSpec {

  def formatSessionVrn: Option[String] => Map[String, String] = _.fold(Map.empty[String, String])(x => Map(SessionKeys.CLIENT_VRN -> x))

  def postAuthenticationTests(path: String, sessionVrn: Option[String] = None)(formData: Map[String, Seq[String]]): Unit =
    authenticationTests(path, post(path, formatSessionVrn(sessionVrn))(formData))

  def getAuthenticationTests(path: String, sessionVrn: Option[String] = None): Unit =
    authenticationTests(path, get(path, formatSessionVrn(sessionVrn)))

  private def authenticationTests(path: String, method: => WSResponse): Unit = {

    "the user is timed out (not authenticated)" should {

      "redirect to GG Sign In" in {

        given.user.isNotAuthenticated

        When(s"I call the path '$path'")
        val res = method

        res should have(
          httpStatus(SEE_OTHER),
          redirectURI(appConfig.signInUrl)
        )
      }
    }

    "the user is logged in without an Affinity Group" should {

      "Render the Internal Server Error view" in {

        given.user.noAffinityGroup

        When(s"I call the path '$path'")
        val res = method

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR),
          pageTitle(internalServerErrorTitle)
        )
      }
    }
  }
}
