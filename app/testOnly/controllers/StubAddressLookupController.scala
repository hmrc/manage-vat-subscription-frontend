/*
 * Copyright 2024 HM Revenue & Customs
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

package testOnly.controllers

import config.AppConfig
import controllers.predicates.AuthPredicate

import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.mvc.Http.HeaderNames
import testOnly.views.html.StubAddressLookupView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.ExecutionContext

class StubAddressLookupController @Inject()(val authenticate: AuthPredicate,
                                            stubAddressLookupView: StubAddressLookupView,
                                            implicit val mcc:MessagesControllerComponents,
                                            implicit val ec: ExecutionContext,
                                            implicit val appConfig: AppConfig)
  extends FrontendController(mcc) with I18nSupport {

  def initialiseJourney: Action[JsValue] = Action(parse.json) { _ =>
    Accepted.withHeaders(HeaderNames.LOCATION -> testOnly.controllers.routes.StubAddressLookupController.show().url)
  }

  def show(): Action[AnyContent] = authenticate { implicit user =>
    Ok(stubAddressLookupView())
  }

  def getAddress(id: String): Action[AnyContent] = Action { _ =>
    id match {
      case "overseas" =>
        Ok(Json.obj(
          "lines" -> Json.arr("Strada Falsa 1", "Rome"),
          "country" -> Json.obj(
            "code" -> "IT",
            "name" -> "Italy"
          )
        ))
      case "uk" =>
        Ok(Json.obj(
          "lines" -> Json.arr("1 Fake Street", "Telford"),
          "postcode" -> "TF1 1AA",
          "country" -> Json.obj(
            "code" -> "GB",
            "name" -> "United Kingdom"
          )
        ))
      case "invalid" =>
        Ok(Json.obj(
          "lines" -> Json.arr("Falsche Straße 1", "Berlin"),
          "country" -> Json.obj(
            "code" -> "DE",
            "name" -> "Germany"
          )
        ))
      case _ => NotFound
    }
  }
}