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

package testOnly.controllers

import config.AppConfig
import controllers.predicates.AuthPredicate
import javax.inject.Inject
import models.customerAddress.AddressModel
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import play.mvc.Http.HeaderNames
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

class StubAddressLookupController @Inject()(val messagesApi: MessagesApi,
                                            val authenticate: AuthPredicate,
                                            implicit val appConfig: AppConfig)
  extends FrontendController with I18nSupport {

  def initialiseJourney(): Action[JsValue] = Action.async(parse.json) { implicit user =>
    Future.successful(
      Accepted(Json.toJson(AddressModel("line1", "line2", None, None, None, "EN")))
        .withHeaders(HeaderNames.LOCATION -> testOnly.controllers.routes.StubAddressLookupController.show().url)
    )
  }

  def show(): Action[AnyContent] = authenticate { implicit user =>
    Ok(testOnly.views.html.stubAddressLookup())
  }

  def callback(id: String): Action[AnyContent] = authenticate { implicit user =>
    Redirect(controllers.routes.BusinessAddressController.confirmation(user.redirectSuffix))
  }

}
