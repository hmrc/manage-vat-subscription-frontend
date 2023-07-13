/*
 * Copyright 2023 HM Revenue & Customs
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

import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import testOnly.forms.FeatureSwitchForm
import testOnly.models.FeatureSwitchModel
import testOnly.views.html.FeatureSwitchView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.LoggingUtil

@Singleton
class FeatureSwitchController @Inject()(featureSwitchView: FeatureSwitchView)
                                       (implicit mcc: MessagesControllerComponents,
                                        appConfig: AppConfig)
  extends FrontendController(mcc) with I18nSupport with LoggingUtil {

  val featureSwitch: Action[AnyContent] = Action { implicit request =>
      val form = FeatureSwitchForm.form.fill(
          FeatureSwitchModel(
            stubAgentClientLookup = appConfig.features.stubAgentClientLookup(),
            stubAddressLookup = appConfig.features.stubAddressLookup()
          )
        )
        debug(s"[FeatureSwitchController][featureSwitch] form: $form")
        Ok(featureSwitchView(form))
    }

  val submitFeatureSwitch: Action[AnyContent] = Action { implicit request =>
    FeatureSwitchForm.form.bindFromRequest().fold(
      _ => Redirect(routes.FeatureSwitchController.featureSwitch),
      success = handleSuccess
    )
  }

  def handleSuccess(model: FeatureSwitchModel): Result = {
    appConfig.features.stubAgentClientLookup(model.stubAgentClientLookup)
    appConfig.features.stubAddressLookup(model.stubAddressLookup)
    Redirect(routes.FeatureSwitchController.featureSwitch)
  }
}
