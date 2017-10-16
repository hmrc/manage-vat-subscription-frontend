/*
 * Copyright 2017 HM Revenue & Customs
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
import forms.test.TextInputForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.AuthService

import scala.concurrent.Future

@Singleton
class HelloWorldController @Inject()(val messagesApi: MessagesApi,
                                     val authService: AuthService,
                                     implicit val appConfig: AppConfig) extends AuthenticatedController with I18nSupport {

  val helloWorld: Action[AnyContent] = AuthenticatedAction {
    implicit request =>
      implicit user =>
        Future.successful(Ok(views.html.helloworld.hello_world()))
  }

  val textInput: Action[AnyContent] = Action.async {
    implicit request =>
      Future.successful(Ok(views.html.input.textInput(TextInputForm.form)))
  }
}
