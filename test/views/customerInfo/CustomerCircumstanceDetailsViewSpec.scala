/*
 * Copyright 2022 HM Revenue & Customs
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

package views.customerInfo

import assets.CircumstanceDetailsTestConstants._
import assets.CustomerDetailsTestConstants.tradingName
import assets.PPOBAddressTestConstants
import assets.PPOBAddressTestConstants.{addLine1, addLine2, postcode, ppobModelMaxEmailUnverified, ppobModelMaxPending}
import assets.messages.{BaseMessages, ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import mocks.services.MockServiceInfoService
import models.circumstanceInfo.PendingChanges
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.ViewBaseSpec
import views.html.customerInfo.CustomerCircumstanceDetailsView
import utils.TestUtil

class CustomerCircumstanceDetailsViewSpec extends ViewBaseSpec with BaseMessages with MockServiceInfoService with TestUtil {
  val getPartialHtmlAgent: Html = Html("")
  val getPartialHtmlNotAgent: Html = Html("""<div id="getPartialTest">dummyHtml</div>""")
  val injectedView: CustomerCircumstanceDetailsView = inject[CustomerCircumstanceDetailsView]

  "Rendering the Customer Details page" when {

    "an Individual" when {

      "with full information" when {

        "with no pending changes" when {

          "registered for VAT" when {

            "the page is rendered" should {

              lazy val view = injectedView(customerInformationNoPendingIndividual, getPartialHtmlNotAgent)(user, messages, mockConfig)
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have the correct document title '${viewMessages.title}'" in {
                document.title shouldBe viewMessages.title
              }

              "have the correct service name" in {
                elementText(".hmrc-header__service-name") shouldBe clientServiceName
              }

              s"have a the correct page heading '${viewMessages.heading}'" in {
                elementText("h1") shouldBe viewMessages.heading
              }

              "display a breadcrumb trail which" in {
                elementText(".govuk-breadcrumbs li:nth-of-type(1)") shouldBe breadcrumbBta
                elementText(".govuk-breadcrumbs li:nth-of-type(2)") shouldBe breadcrumbVat
              }

              "the view loads in the partial" should {
                "display the dummyHtml" in {
                  elementText("#getPartialTest") shouldBe "dummyHtml"
                }
              }

              "has an about header" in {
                elementText("h2.govuk-heading-m") shouldBe viewMessages.aboutHeading
              }

              "display the trading name row" which {

                "has the correct heading" in {
                  elementText("#trading-name-text") shouldBe viewMessages.tradingNameHeading
                }

                "has the 'Not provided' text in place of the trading name" in {
                  elementText("#trading-name") shouldBe "Not provided"
                }

                "has an 'Add' link" which {

                  "has the correct text" in {
                    elementText("#trading-name-status > span:nth-of-type(1)") shouldBe viewMessages.add
                  }

                  "has the correct hidden text" in {
                    elementText("#trading-name-status > .govuk-visually-hidden") shouldBe
                      viewMessages.changeTradingNameHidden("Not provided")
                  }

                  "link to the trading name journey" in {
                    element("#trading-name-status").attr("href") shouldBe mockConfig.vatDesignatoryDetailsTradingNameUrl
                  }
                }
              }

              "have a signup notification banner" in {
                elementText("#govuk-notification-banner-title") shouldBe viewMessages.bannerTitle
              }

              "have a section for business address" which {

                "has the heading" in {
                  elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
                }

                "has the correct address output" in {
                  elementText("#businessAddress") shouldBe s"$addLine1 $addLine2 $postcode"
                }

                "has a change link" which {

                  "has the correct text" in {
                    elementText("#place-of-business-status > span:nth-of-type(1)") shouldBe viewMessages.change
                  }

                  "has the correct hidden text" in {
                    elementText("#place-of-business-status > .govuk-visually-hidden") shouldBe
                      viewMessages.changeBusinessAddressHidden(PPOBAddressTestConstants.addLine1)
                  }

                  "has a link to the business address journey" in {
                    element("#place-of-business-status").attr("href") shouldBe controllers.routes.BusinessAddressController.show.url
                  }
                }
              }


              "have a section for repayment Bank Account details" which {

                "has the heading" in {
                  elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
                }

                "has the correct text" in {
                  elementText("#bank-details") shouldBe s"${viewMessages.accountNumberHeading} " + viewMessages.accountNumberEnding + " " +
                    customerInformationModelMaxIndividual.bankDetails.get.bankAccountNumber.get.substring(4) +
                    s" ${viewMessages.sortcodeHeading} " + viewMessages.sortcodeStarting + " " + customerInformationModelMaxIndividual.bankDetails.get.sortCode.get.substring(0,2)
                }

                "has bold styling for the Account Number heading" in {
                  element("#bank-details span:nth-of-type(1)").hasClass("govuk-!-font-weight-bold")
                  elementText("#bank-details span:nth-of-type(1)") shouldBe viewMessages.accountNumberHeading
                }

                "has bold styling for the Sort Code heading" in {
                  element("#bank-details span:nth-of-type(2)").hasClass("govuk-!-font-weight-bold")
                  elementText("#bank-details span:nth-of-type(2)") shouldBe viewMessages.sortcodeHeading
                }

                "has a change link" which {

                  "has the correct text" in {
                    elementText("#bank-details-status > span:nth-of-type(1)") shouldBe viewMessages.change
                  }

                  "has the correct hidden text" in {
                    elementText("#bank-details-status > .govuk-visually-hidden") shouldBe viewMessages.changeBankDetailsHidden
                  }

                  "has a link to the payments service" in {
                    element("#bank-details-status").attr("href") shouldBe controllers.routes.PaymentsController.sendToPayments.url
                  }
                }
              }

              "have a section for return frequency" which {

                "has a return details header" in {
                  elementText("#return-details-section > h2") shouldBe viewMessages.returnDetailsHeading
                }

                "has the correct heading" in {
                  elementText("#vat-return-dates-text") shouldBe viewMessages.returnFrequencyHeading
                }

                "has the correct current frequency" in {
                  elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
                }

                "has a change link" which {

                  s"has a change link to ${mockConfig.vatReturnPeriodFrontendUrl}" in {
                    element("#vat-return-dates-status").attr("href") shouldBe mockConfig.vatReturnPeriodFrontendUrl
                  }

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#vat-return-dates-status > span:nth-of-type(1)") shouldBe viewMessages.change
                  }
                }
              }

              "have a section for contact details" which {

                "has a contact details header" in {
                  elementText("#contact-details-section > h2") shouldBe viewMessages.contactDetailsHeading
                }

                s"has the wording '${viewMessages.contactDetailsMovedToBTA}' " in {
                  elementText("#contact-details-section > p") shouldBe viewMessages.contactDetailsMovedToBTA
                }

                s"has a link to ${mockConfig.btaAccountDetails}" in {
                  element("#contact-details-section > p > a").attr("href") shouldBe mockConfig.btaAccountDetails
                }

                "does not display the email nudge" in {
                  elementExtinct("#contact-details-section > p.notice")
                }
              }

              "have a section for website address" which {

                "has the heading" in {
                  elementText("#vat-website-address-text") shouldBe viewMessages.websiteAddressHeading
                }

                "has the correct value for the website address" in {
                  elementText("#vat-website-address") shouldBe customerInformationModelMaxIndividual.ppob.websiteAddress.get
                }

                "has a change link" which {

                  "has the correct text" in {
                    elementText("#vat-website-address-status > span:nth-of-type(1)") shouldBe viewMessages.change
                  }

                  "has the correct hidden text" in {
                    elementText("#vat-website-address-status > .govuk-visually-hidden") shouldBe
                      viewMessages.changeWebsiteAddressHidden(PPOBAddressTestConstants.website)
                  }

                  "has a link to the website address journey" in {
                    element("#vat-website-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeWebsiteUrl
                  }
                }
              }

              "not display the 'change another clients details' link" in {
                elementExtinct("#change-client-text")
              }

              "display a progressive disclosure" which {

                "contains help text" which {

                  lazy val progressiveDisclosure = element(".govuk-details")

                  "contains the correct text" in {
                    progressiveDisclosure.select("summary > span").text() shouldEqual viewMessages.changeNotListed
                  }

                  "contains content" which {

                    lazy val helpContent = progressiveDisclosure.select("div")

                    "displays the correct text" in {
                      helpContent.text() shouldEqual viewMessages.helpText
                    }

                    s"has a link to ${mockConfig.govUkChangeVatRegistrationDetails}" in {
                      helpContent.select("a").attr("href") shouldEqual mockConfig.govUkChangeVatRegistrationDetails
                    }
                  }
                }
              }

            }

            "user has a different mandation status than 3" should {

              lazy val view = injectedView(customerInformationNoPendingIndividualMTDfB, getPartialHtmlNotAgent)(user, messages, mockConfig)
              lazy implicit val document: Document = Jsoup.parse(view.body)

              "not display the MTD sign up banner" in {
                elementExtinct(".govuk-notification-banner")
              }
            }

            "the page is rendered for a user without a verified email" should {
              lazy val view = injectedView(
                customerInformationNoPendingIndividual.copy(ppob = ppobModelMaxEmailUnverified),
                getPartialHtmlNotAgent
              )(user, messages, mockConfig)
              lazy implicit val document: Document = Jsoup.parse(view.body)

              "have a section for contact details" which {

                "has a contact details header" in {
                  elementText("#contact-details-section > h2") shouldBe viewMessages.contactDetailsHeading
                }

                "has hidden text supporting the nudge icon" in {
                  elementText(".govuk-warning-text__assistive") shouldBe viewMessages.warning
                }

                s"has the wording '${viewMessages.unverifiedEmailNudge}' " in {
                  elementText(".govuk-warning-text__text") shouldBe s"${viewMessages.warning} ${viewMessages.unverifiedEmailNudge}"
                }

                s"the 'email fix now' link redirects to vat-correspondence fix your email page" in {
                  element(".govuk-warning-text a").attr("href") shouldBe
                    s"${mockConfig.vatCorrespondenceFixYourEmail}"
                }

                s"has the wording '${viewMessages.contactDetailsMovedToBTA}' " in {
                  elementText("#contact-details-section > .govuk-body") shouldBe viewMessages.contactDetailsMovedToBTA
                }

                s"has a link to ${mockConfig.btaAccountDetails}" in {
                  element("#contact-details-section > .govuk-body > a").attr("href") shouldBe mockConfig.btaAccountDetails
                }
              }
            }

            "the page is rendered for a user without a verified email and has an email change pending" should {
              lazy val view = injectedView(
                customerInformationNoPendingIndividual.copy(
                  ppob = ppobModelMaxEmailUnverified,
                  pendingChanges = Some(PendingChanges(Some(ppobModelMaxPending), None, None, None, None))),
                getPartialHtmlNotAgent
              )(user, messages, mockConfig)
              lazy implicit val document: Document = Jsoup.parse(view.body)

              "have a section for contact details" which {

                "has a contact details header" in {
                  elementText("#contact-details-section > h2") shouldBe viewMessages.contactDetailsHeading
                }

                s"has the wording '${viewMessages.contactDetailsMovedToBTA}' " in {
                  elementText("#contact-details-section > p") shouldBe viewMessages.contactDetailsMovedToBTA
                }

                s"has a link to ${mockConfig.btaAccountDetails}" in {
                  element("#contact-details-section > p > a").attr("href") shouldBe mockConfig.btaAccountDetails
                }

                "does not display the email nudge" in {
                  elementExtinct("#contact-details-section > p.notice")
                }
              }
            }
          }
        }

        "with pending changes" when {

          "deregistration is pending" should {

            lazy val view = injectedView(customerInformationModelDeregPending, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have a section for return frequency" which {

              "has the heading" in {
                elementText("#vat-return-dates-text") shouldBe viewMessages.returnFrequencyHeading
              }

              "has the correct value output for the current frequency" in {
                elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
              }

              "has no change link or pending status" in {
                document.select("#vat-return-dates-status").isEmpty shouldBe true
              }
            }
          }

          "PPOB is pending" should {

            lazy val view = injectedView(customerInformationPendingPPOBModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have the correct text" in {
              elementText("#place-of-business-status > span:nth-of-type(1)") shouldBe viewMessages.pending
            }

            "have the correct hidden text" in {
              elementText("#place-of-business-status > .govuk-visually-hidden") shouldBe viewMessages.pendingBusinessAddressHidden
            }
          }

          "website is pending" should {

            lazy val view = injectedView(customerInformationPendingWebsiteModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have the correct text" in {
              elementText("#vat-website-address-status > span:nth-of-type(1)") shouldBe viewMessages.pending
            }

            "have the correct hidden text" in {
              elementText("#vat-website-address-status > .govuk-visually-hidden") shouldBe viewMessages.pendingWebsiteAddressHidden
            }
          }

          "return frequency is pending" should {

            lazy val view = injectedView(customerInformationPendingReturnPeriodModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have the correct text" in {
              elementText("#vat-return-dates-status > span:nth-of-type(1)") shouldBe viewMessages.pending
            }

            "have the correct hidden text" in {
              elementText("#vat-return-dates-status > .govuk-visually-hidden") shouldBe viewMessages.pendingReturnFrequencyHidden
            }

            "display the existing approved return dates" in {
              elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
            }

            "display a panel with information regarding when the new dates will be applied" in {
              elementText(".govuk-inset-text") shouldBe viewMessages.newReturnDatesApplied
            }
          }

          "trading name is pending" should {

            lazy val view = injectedView(customerInfoPendingTradingNameModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have the correct text" in {
              elementText("#trading-name-status > span:nth-of-type(1)") shouldBe viewMessages.pending
            }

            "has the correct hidden text" in {
              elementText("#trading-name-status > .govuk-visually-hidden") shouldBe viewMessages.pendingTradingNameHidden
            }

            "display the inflight trading name" in {
              elementText("#trading-name") shouldBe "Party Kitchen"
            }
          }

          "business name is pending" should {

            lazy val view = injectedView(customerInfoPendingBusinessNameModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have the correct text" in {
              elementText("#business-name-status > span:nth-child(1)") shouldBe viewMessages.pending
            }

            "have the correct hidden text" in {
              elementText("#business-name-status > .govuk-visually-hidden") shouldBe viewMessages.pendingBusinessNameHidden
            }

            "display the inflight business name" in {
              elementText("#business-name") shouldBe "Party Kitchen"
            }
          }

          "the website has been removed" should {

            lazy val view = injectedView(customerInformationModelPendingRemoved("website"), getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            "have the correct text" in {
              elementText("#vat-website-address-status > span:nth-of-type(1)") shouldBe viewMessages.pending
            }

            "have the correct hidden text" in {
              elementText("#vat-website-address-status > .govuk-visually-hidden") shouldBe viewMessages.pendingWebsiteAddressHidden
            }
          }
        }
      }

      "with no bank details" should {
        lazy val view = injectedView(customerInformationModelMin, getPartialHtmlNotAgent)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "display the 'Not provided' text in place of the bank details" in {
          elementText("#bank-details") shouldBe "Not provided"
        }

        "display an 'Add' link for changing the bank details" which {

          "has the correct text" in {
            elementText("#bank-details-status > span:nth-of-type(1)") shouldBe viewMessages.add
          }

          "links to the correspondence details service" in {
            element("#bank-details-status").attr("href") shouldBe controllers.routes.PaymentsController.sendToPayments.url
          }
        }
      }

      "with no website" should {

        lazy val view = injectedView(customerInformationModelMin, getPartialHtmlNotAgent)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "display the 'Not provided' text in place of the website address" in {
          elementText("#vat-website-address") shouldBe "Not provided"
        }

        "display an 'Add' link for changing the website address" which {

          "has the correct text" in {
            elementText("#vat-website-address-status > span:nth-of-type(1)") shouldBe viewMessages.add
          }

          "links to the correspondence details service" in {
            element("#vat-website-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeWebsiteUrl
          }
        }
      }
    }

    "an Organisation" when {

      "the user has a valid party type" when {

        "the user has an organisation name and a trading name" should {

          lazy val view = injectedView(customerInformationWithPartyType(Some("2")), getPartialHtmlNotAgent)(user, messages, mockConfig)
          lazy implicit val document: Document = Jsoup.parse(view.body)

          "have a change details section for the Business Name" which {

            s"has the heading '${viewMessages.organisationNameHeading}'" in {
              elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
            }
          }

          "display the trading name row" which {

            "has the heading" in {
              elementText("#trading-name-text") shouldBe viewMessages.tradingNameHeading
            }

            "has the correct address output" in {
              elementText("#trading-name") shouldBe tradingName
            }

            "has a change link" which {

              "has the correct wording" in {
                elementText("#trading-name-status > span:nth-of-type(1)") shouldBe viewMessages.change
              }

              "has the correct hidden text" in {
                elementText("#trading-name-status > .govuk-visually-hidden") shouldBe viewMessages.changeTradingNameHidden(tradingName)
              }

              "has a link to the trading name journey" in {
                element("#trading-name-status").attr("href") shouldBe mockConfig.vatDesignatoryDetailsTradingNameUrl
              }
            }
          }
        }

        "the user has no organisation name" should {

          lazy val view = injectedView(customerInformationModelMaxIndividual, getPartialHtmlNotAgent)(user, messages, mockConfig)
          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not have a change details section for the Business Name" in {
            document.select("#business-name-text").isEmpty shouldBe true
          }
        }
      }

      "the user has an invalid party type" should {

        lazy val view = injectedView(customerInformationWithPartyType(Some("other")), getPartialHtmlNotAgent)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not have a change details section for the Business Name" in {
          document.select("#business-name-text").isEmpty shouldBe true
        }
      }

      "the user does not have a party type" should {

        lazy val view = injectedView(customerInformationWithPartyType(None), getPartialHtmlNotAgent)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "have no change details section for the Business Name" in {
          document.select("#business-name-text").isEmpty shouldBe true
        }
      }
    }

    "an Agent" when {

        lazy val view = {
          injectedView(customerInformationModelMaxIndividual, getPartialHtmlAgent)(agentUser, messages, mockConfig)
        }

        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not display a breadcrumb trail" in {
          elementExtinct(".govuk-breadcrumbs li:nth-of-type(1)")
          elementExtinct(".govuk-breadcrumbs li:nth-of-type(2)")
          elementExtinct(".govuk-breadcrumbs li:nth-of-type(3)")
        }

        "display the back link" in {
          elementText(".govuk-back-link") shouldBe viewMessages.backText
          element(".govuk-back-link").attr("href") shouldBe
            mockConfig.agentClientLookupAgentAction
        }

        s"have the correct document title '${viewMessages.title}'" in {
          document.title shouldBe viewMessages.agentTitle
        }

        "have the correct service name" in {
          elementText(".hmrc-header__service-name") shouldBe agentServiceName
        }

        s"have a the correct page heading '${viewMessages.agentHeading}'" in {
          elementText("h1") shouldBe viewMessages.agentHeading
        }

        "have a blank field where the 'Change' link would be for email address" which {

          "has the visually hidden class" in {
            element("#vat-email-address-status").hasClass("govuk-visually-hidden") shouldBe true
          }

          "has the correct hidden text" in {
            elementText("#vat-email-address-status") shouldBe viewMessages.changeEmailAddressAgentHidden
          }
        }

    }
  }

  "the user is an overseas business" when {

    lazy val view = injectedView(overseasCompany, getPartialHtmlNotAgent)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "the overseas PPOB change feature is on" should {

      "not display business name" in {
        elementExtinct("#business-name-text")
        elementExtinct("#business-name")
        elementExtinct("#business-name-status")
      }

      "display PPOB" in {
        elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        elementText("#businessAddress") shouldBe addLine1
      }

      "have a link to change the PPOB" in {
        mockConfig.features.allowOverseasChangeOfPPOBEnabled(true)
        elementText("#place-of-business-status > span:nth-of-type(1)") shouldBe viewMessages.change
      }
    }

    "the allow overseas PPOB change feature is off" should {

      lazy val view = {
        mockConfig.features.allowOverseasChangeOfPPOBEnabled(false)
        injectedView(overseasCompany, getPartialHtmlNotAgent)(user, messages, mockConfig)
      }
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not have a link to change the PPOB" in {
        elementExtinct("#place-of-business-status > span:nth-of-type(1)")

      }
    }
  }

}
