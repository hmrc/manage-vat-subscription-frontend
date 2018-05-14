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

import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthenticationPredicate
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.{AddressLookupService, BusinessAddressService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class BusinessAddressController @Inject()(val messagesApi: MessagesApi,
                                          val authenticate: AuthenticationPredicate,
                                          addressLookupService: AddressLookupService,
                                          businessAddressService: BusinessAddressService,
                                          val serviceErrorHandler: ServiceErrorHandler,
                                          implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  def callback(id: String): Action[AnyContent] = authenticate.async { implicit user =>
    addressLookupService.retrieveAddress(id) flatMap {
      case Right(returnModel) =>
        businessAddressService.updateBusinessAddress(user.vrn, returnModel) map {
          case Right(_) => Ok(views.html.businessAddress.change_address_confirmation())
          case Left(_) => Logger.debug(s"[BusinessAddressController][callback] Error Returned from Business Address Service, Rendering ISE.")
            serviceErrorHandler.showInternalServerError
        }
      case Left(_) => Logger.debug(s"[BusinessAddressController][callback] Error Returned from Address Lookup Service, Rendering ISE.")
        Future.successful(serviceErrorHandler.showInternalServerError)
    }
  }
}
