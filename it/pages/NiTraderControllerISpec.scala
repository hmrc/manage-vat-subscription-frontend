package pages

import helpers.IntegrationTestConstants.{VRN, customerCircumstancesJsonMax, individual, organisation}
import play.api.http.Status.FORBIDDEN
import play.api.i18n.Messages
import play.api.libs.ws.WSResponse
import play.api.test.Helpers.{INTERNAL_SERVER_ERROR, OK}
import stubs.{ServiceInfoStub, VatSubscriptionStub}

class NiTraderControllerISpec extends BasePageISpec {
  val path = "/change-ni-trading-status"
  def show(sessionVrn: Option[String] = None): WSResponse = get(path, formatSessionVrn(sessionVrn))

  "Calling the NiTraderController.changeNiTradingStatus" when {
    "the user is an Individual" should {
      "render the changeTradingUnderNiProtocol view" in {
        given.user.isAuthenticated

        And("A succesful partial is returned")
        ServiceInfoStub.stubServiceInfoPartial

        And("A successful response with all details is returned for an Individual")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesJsonMax(individual))

        When("I call to show the Customer Circumstances page")
        val res = show()

        Then("Status should be OK")
        res should have(httpStatus(OK))

        And("There should be the correct title")
        res should have(
          elementText("#page-heading")(expectedValue = "Change your Northern Ireland trading status")
        )

      }
    }

    "the user is an Organisation" should {
      "render the changeTradingUnderNiProtocol view" in {
        given.user.isAuthenticated

        And("A succesful partial is returned")
        ServiceInfoStub.stubServiceInfoPartial

        And("A successful response with all details is returned for an Individual")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesJsonMax(organisation))

        When("I call to show the Customer Circumstances page")
        val res = show()

        Then("Status should be OK")
        res should have(httpStatus(OK))

        And("There should be the correct title")
        res should have(
          elementText("#page-heading")(expectedValue = "Change your Northern Ireland trading status")
        )

      }

      "an error response is received" should {

        "Render the Internal Server Error page" in {

          given.user.isAuthenticated

          And("An unsuccessful response is returned")
          VatSubscriptionStub.getClientDetailsError(VRN)

          When("I call to show the Customer Circumstances page")
          val res = show()

          res should have(
            httpStatus(INTERNAL_SERVER_ERROR),
            pageTitle(titleThereIsAProblem + titleSuffixUser)
          )
        }
      }
    }

    "the user is an Agent" should {
      "render the changeTradingUnderNiProtocol view" in {
        given.agent.isSignedUpToAgentServices

        And("A successful response with all the details is returned for an Individual")
        VatSubscriptionStub.getClientDetailsSuccess(VRN)(customerCircumstancesJsonMax(organisation))

        When("I call to show the Customer Circumstances page")
        val res = show(Some(VRN))

        Then("Status should be OK")
        res should have(httpStatus(OK))

        And("There should be the correct title")
        res should have(
          elementText("#page-heading")(expectedValue = "Change your client's Northern Ireland trading status")
        )

      }

      "render an error view when the agent is unauthorised" in {
        given.agent.isNotSignedUpToAgentServices

        When("I call to show the Customer Circumstances page")
        val res = show(Some("999999999"))

        res should have(
          httpStatus(FORBIDDEN),
          pageTitle(Messages("unauthorised.agent.title") + titleSuffixAgent)
        )
      }
    }
  }

}
