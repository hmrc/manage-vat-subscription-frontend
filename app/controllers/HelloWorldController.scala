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

package controllers

import javax.inject.{Inject, Singleton}

import config.AppConfig
import controllers.auth.actions.VatUserAction
import forms.test.MoneyInputForm._
import forms.test.{DateInputForm, MoneyInputForm, TextInputForm}
import models.test.MoneyInputModel
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.auth.core.AuthorisedFunctions
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class HelloWorldController @Inject()(val messagesApi: MessagesApi,
                                     val authFunctions: AuthorisedFunctions,
                                     implicit val appConfig: AppConfig)
  extends FrontendController with VatUserAction with I18nSupport {

  val helloWorld: Action[AnyContent] = VatUserAction {
    implicit request =>
      implicit user =>
        Ok(views.html.helloworld.hello_world())
  }

  val textInput: Action[AnyContent] = Action {
    implicit request =>
      Ok(views.html.test.textInput(TextInputForm.form))
  }

  val moneyInput: Action[AnyContent] = Action.async {
    implicit request =>
      Future.successful(Ok(views.html.test.moneyInput(MoneyInputForm.form)))
  }

  val submitMoney: Action[AnyContent] = Action.async {
    implicit request =>

      def successAction(model: MoneyInputModel): Future[Result] = {
        Future.successful(Redirect(routes.HelloWorldController.moneyInput()))
      }

      def errorAction(form: Form[MoneyInputModel]): Future[Result] = {
        Future.successful(BadRequest(views.html.test.moneyInput(form)))
      }

      form.bindFromRequest.fold(errorAction, successAction)
  }

  val dateInput: Action[AnyContent] = Action.async {
    implicit request =>
      Future.successful(Ok(views.html.test.dateInput(DateInputForm.form)))
  }
}
