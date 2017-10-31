
package config

import javax.inject.Inject

import play.api.Configuration
import play.api.i18n.MessagesApi
import play.api.mvc.Request
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.http.FrontendErrorHandler

class ServiceErrorHandler @Inject()(val messagesApi: MessagesApi, val configuration: Configuration, appConfig: AppConfig)
  extends FrontendErrorHandler {

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)(implicit request: Request[_]): Html = {
    views.html.errors.standardError(appConfig, pageTitle, heading, message)
  }

}
