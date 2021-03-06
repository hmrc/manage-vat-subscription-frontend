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

package testOnly.controllers

import config.AppConfig
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import testOnly.connectors.VatSubscriptionFeaturesConnector
import testOnly.forms.FeatureSwitchForm
import testOnly.models.FeatureSwitchModel
import testOnly.views.html.FeatureSwitchView
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FeatureSwitchController @Inject()(vatSubscriptionFeaturesConnector: VatSubscriptionFeaturesConnector,
                                        featureSwitchView: FeatureSwitchView,
                                        implicit val mcc: MessagesControllerComponents,
                                        implicit val ec: ExecutionContext,
                                        implicit val appConfig: AppConfig)
  extends FrontendController(mcc) with I18nSupport {

  val featureSwitch: Action[AnyContent] = Action.async { implicit request =>

    vatSubscriptionFeaturesConnector.getFeatures.map {
      vatSubFeatures =>
        Logger.debug(s"[FeatureSwitchController][featureSwitch] vatSubFeatures: $vatSubFeatures")
        val form = FeatureSwitchForm.form.fill(
          FeatureSwitchModel(
            vatSubFeatures,
            stubAgentClientLookup = appConfig.features.stubAgentClientLookup(),
            stubAddressLookup = appConfig.features.stubAddressLookup(),
            stubContactPreferences = appConfig.features.stubContactPreferences(),
            allowAgentBankAccountChange = appConfig.features.allowAgentBankAccountChange(),
            showContactNumbersAndWebsite = appConfig.features.showContactNumbersAndWebsite(),
            languageFeatureEnabled = appConfig.features.useLanguageSelector(),
            useOverseasIndicatorEnabled = appConfig.features.useOverseasIndicator(),
            changeClientFeature = appConfig.features.changeClientFeature(),
            emailVerifiedFeature = appConfig.features.emailVerifiedFeature(),
            disableBulkPaper = appConfig.features.disableBulkPaper(),
            missingTraderAddressIntercept = appConfig.features.missingTraderAddressIntercept(),
            contactDetailsMovedToBTA = appConfig.features.contactDetailsMovedToBTA(),
            contactPrefMigrationFeature = appConfig.features.contactPrefMigrationFeature(),
            tradingNameRowEnabled = appConfig.features.tradingNameRowEnabled(),
            organisationNameRowEnabled = appConfig.features.organisationNameRowEnabled()
          )
        )
        Logger.debug(s"[FeatureSwitchController][featureSwitch] form: $form")
        Ok(featureSwitchView(form))
    }
  }

  val submitFeatureSwitch: Action[AnyContent] = Action.async { implicit request =>
    FeatureSwitchForm.form.bindFromRequest().fold(
      _ => Future.successful(Redirect(routes.FeatureSwitchController.featureSwitch())),
      success = handleSuccess
    )
  }

  def handleSuccess(model: FeatureSwitchModel)(implicit hc: HeaderCarrier): Future[Result] = {
    appConfig.features.stubAgentClientLookup(model.stubAgentClientLookup)
    appConfig.features.stubAddressLookup(model.stubAddressLookup)
    appConfig.features.stubContactPreferences(model.stubContactPreferences)
    appConfig.features.allowAgentBankAccountChange(model.allowAgentBankAccountChange)
    appConfig.features.showContactNumbersAndWebsite(model.showContactNumbersAndWebsite)
    appConfig.features.useLanguageSelector(model.languageFeatureEnabled)
    appConfig.features.useOverseasIndicator(model.useOverseasIndicatorEnabled)
    appConfig.features.changeClientFeature(model.changeClientFeature)
    appConfig.features.emailVerifiedFeature(model.emailVerifiedFeature)
    appConfig.features.disableBulkPaper(model.disableBulkPaper)
    appConfig.features.missingTraderAddressIntercept(model.missingTraderAddressIntercept)
    appConfig.features.contactDetailsMovedToBTA(model.contactDetailsMovedToBTA)
    appConfig.features.contactPrefMigrationFeature(model.contactPrefMigrationFeature)
    appConfig.features.tradingNameRowEnabled(model.tradingNameRowEnabled)
    appConfig.features.organisationNameRowEnabled(model.organisationNameRowEnabled)
    vatSubscriptionFeaturesConnector.postFeatures(model.vatSubscriptionFeatures).map {
      response =>
        response.status match {
          case OK => Redirect(routes.FeatureSwitchController.featureSwitch())
          case _ => InternalServerError("Failed to update feature switches in VAT Subscription")
        }
    }
  }
}
