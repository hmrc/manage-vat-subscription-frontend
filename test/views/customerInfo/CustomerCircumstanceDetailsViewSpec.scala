/*
 * Copyright 2020 HM Revenue & Customs
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
import assets.CustomerDetailsTestConstants.individual
import assets.PPOBAddressTestConstants
import assets.PPOBAddressTestConstants.{ppobModelMax, ppobModelMaxEmailUnverified, ppobModelMaxPending}
import assets.messages.{BaseMessages, ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import mocks.services.MockServiceInfoService
import models.circumstanceInfo.{CircumstanceDetails, PendingChanges}
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
                elementText(".header__menu__proposition-name") shouldBe clientServiceName
              }

              s"have a the correct page heading '${viewMessages.heading}'" in {
                elementText("h1") shouldBe viewMessages.heading
              }

              "display a breadcrumb trail which" in {
                elementText(".breadcrumbs li:nth-of-type(1)") shouldBe breadcrumbBta
                elementText(".breadcrumbs li:nth-of-type(2)") shouldBe breadcrumbVat
                elementText(".breadcrumbs li:nth-of-type(3)") shouldBe breadcrumbBizDeets

                element("#breadcrumb-bta").attr("href") shouldBe "ye olde bta url"
                element("#breadcrumb-vat").attr("href") shouldBe "/vat-summary"
              }

              "the view loads in the partial" should {
                "display the dummyHtml" in {
                  elementText("#getPartialTest") shouldBe "dummyHtml"
                }
              }

              "has an about header" in {
                elementText("#content > article > div:nth-child(2) > h2") shouldBe viewMessages.aboutHeading
              }

              "have a section for business address" which {

                "has the heading" in {
                  elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
                }

                "has the correct address output" in {
                  elementText("#businessAddress li:nth-child(1)") shouldBe customerInformationModelMaxIndividual.ppob.address.line1
                  elementText("#businessAddress li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.ppob.address.line2.get
                  elementText("#businessAddress li:nth-child(3)") shouldBe customerInformationModelMaxIndividual.ppob.address.postCode.get
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#place-of-business-status") shouldBe viewMessages.change
                  }

                  s"has the correct aria label text '${viewMessages.changeBusinessAddressHidden(PPOBAddressTestConstants.addLine1)}'" in {
                    element("#place-of-business-status").attr("aria-label") shouldBe viewMessages.changeBusinessAddressHidden(PPOBAddressTestConstants.addLine1)
                  }

                  s"has a link to ${controllers.routes.BusinessAddressController.show().url}" in {
                    element("#place-of-business-status").attr("href") shouldBe controllers.routes.BusinessAddressController.show().url
                  }
                }
              }


              "have a section for repayment Bank Account details" which {

                "has the heading" in {
                  elementText("#bank-details-text") shouldBe viewMessages.bankDetailsHeading
                }

                "has a the correct Account Number" which {

                  "has the correct heading for the Account Number" in {
                    elementText("#bank-details li:nth-child(1)") shouldBe viewMessages.accountNumberHeading
                  }

                  "has the correct value for the account number" in {
                    elementText("#bank-details li:nth-child(2)") shouldBe customerInformationModelMaxIndividual.bankDetails.get.bankAccountNumber.get
                  }
                }

                "has a the correct Sort Code" which {

                  "has the correct heading for the Sort Code" in {
                    elementText("#bank-details li:nth-child(3)") shouldBe viewMessages.sortcodeHeading
                  }

                  "has the correct value for the account number" in {
                    elementText("#bank-details li:nth-child(4)") shouldBe customerInformationModelMaxIndividual.bankDetails.get.sortCode.get
                  }
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#bank-details-status") shouldBe viewMessages.change
                  }

                  s"has the correct aria label text '${viewMessages.changeBankDetailsHidden}'" in {
                    element("#bank-details-status").attr("aria-label") shouldBe viewMessages.changeBankDetailsHidden
                  }

                  s"has a link to ${controllers.routes.PaymentsController.sendToPayments().url}" in {
                    element("#bank-details-status").attr("href") shouldBe controllers.routes.PaymentsController.sendToPayments().url
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
                    elementText("#vat-return-dates-status") shouldBe viewMessages.change
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

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#vat-website-address-status") shouldBe viewMessages.change
                  }

                  s"has the correct aria label text '${viewMessages.changeWebsiteAddressHidden(PPOBAddressTestConstants.website)}'" in {
                    element("#vat-website-address-status").attr("aria-label") shouldBe
                      viewMessages.changeWebsiteAddressHidden(PPOBAddressTestConstants.website)
                  }

                  s"has a link to ${mockConfig.vatCorrespondenceChangeEmailUrl}" in {
                    element("#vat-website-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeWebsiteUrl
                  }
                }
              }

              "not display the 'change another clients details' link" in {
                elementExtinct("#change-client-text")
              }

              "display a progressive disclosure" which {

                "contains help text" which {

                  lazy val progressiveDisclosure = element("details")

                  "contains the correct text" in {
                    progressiveDisclosure.select("summary").text() shouldEqual viewMessages.changeNotListed
                  }

                  "contains content" which {

                    lazy val helpContent = progressiveDisclosure.select("div > p")

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
                  elementText(".icon-important") shouldBe viewMessages.warning
                }

                s"has the wording '${viewMessages.unverifiedEmailNudge}' " in {
                  elementText("#contact-details-section > p:nth-of-type(1) > strong") shouldBe viewMessages.unverifiedEmailNudge
                }

                s"the 'resend email' link redirects to ${controllers.routes.CustomerCircumstanceDetailsController.sendEmailVerification()}" in {
                  element("#contact-details-section > p.notice > strong > a").attr("href") shouldBe
                    s"${controllers.routes.CustomerCircumstanceDetailsController.sendEmailVerification()}"
                }

                s"has the wording '${viewMessages.contactDetailsMovedToBTA}' " in {
                  elementText("#contact-details-section > p:nth-of-type(2)") shouldBe viewMessages.contactDetailsMovedToBTA
                }

                s"has a link to ${mockConfig.btaAccountDetails}" in {
                  element("#contact-details-section > p > a").attr("href") shouldBe mockConfig.btaAccountDetails
                }
              }
            }

            "the page is rendered for a user without a verified email and has an email change pending" should {
              lazy val view = injectedView(
                customerInformationNoPendingIndividual.copy(
                  ppob = ppobModelMaxEmailUnverified,
                  pendingChanges = Some(PendingChanges(Some(ppobModelMaxPending), None, None, None))),
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

            "the tradingNameRowEnabled feature is enabled" should {

              "display the trading name row" which {

                lazy val view = {
                  mockConfig.features.tradingNameRowEnabled(true)
                  injectedView(customerInformationNoPendingIndividual, getPartialHtmlNotAgent)(user, messages, mockConfig)
                }
                lazy implicit val document: Document = Jsoup.parse(view.body)

                "has the heading" in {
                  elementText("#trading-name-text") shouldBe viewMessages.tradingNameHeading
                }

                "has the correct address output" in {
                  elementText("#trading-name") shouldBe "Not provided"
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.add}'" in {
                    elementText("#trading-name-status") shouldBe viewMessages.add
                  }

                  s"has the correct aria label text '${viewMessages.changeTradingNameHidden}'" in {
                    element("#trading-name-status").attr("aria-label") shouldBe viewMessages.changeTradingNameHidden
                  }

                  s"has a link to ${mockConfig.vatDesignatoryDetailsTradingNameUrl}" in {
                    element("#trading-name-status").attr("href") shouldBe mockConfig.vatDesignatoryDetailsTradingNameUrl
                  }
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

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#place-of-business-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
              element("#place-of-business-status").attr("aria-label") shouldBe
                viewMessages.pendingBusinessAddressHidden
            }
          }

          "website is pending" should {

            lazy val view = injectedView(customerInformationPendingWebsiteModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-website-address-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingWebsiteAddressHidden}'" in {
              element("#vat-website-address-status").attr("aria-label") shouldBe
                viewMessages.pendingWebsiteAddressHidden
            }
          }

          "return frequency is pending" should {

            lazy val view = injectedView(customerInformationPendingReturnPeriodModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-return-dates-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingReturnFrequencyHidden}'" in {
              element("#vat-return-dates-status").attr("aria-label") shouldBe viewMessages.pendingReturnFrequencyHidden
            }

            "display the existing approved return dates" in {
              elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
            }

            "display a panel with information regarding when the new dates will be applied" in {
              elementText(".panel-border-wide") shouldBe viewMessages.newReturnDatesApplied
            }
          }

          "the website has been removed" should {

            lazy val view = injectedView(customerInformationModelPendingRemoved("website"), getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-website-address-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingWebsiteAddressHidden}'" in {
              element("#vat-website-address-status").attr("aria-label") shouldBe
                viewMessages.pendingWebsiteAddressHidden
            }
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
            elementText("#vat-website-address-status") shouldBe "Add"
          }

          "links to the correspondence details service" in {
            element("#vat-website-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeWebsiteUrl
          }
        }
      }
    }

    "an Organisation" when {

      "with a valid party type" when {

        "with an organisation name" should {

          "have a change details section for the Business Name" which {

            lazy val view = injectedView(customerInformationWithPartyType(Some("2")), getPartialHtmlNotAgent)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"has the heading '${viewMessages.organisationNameHeading}'" in {
              elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
            }
          }
        }

        "with no organisation name" should {

          val model: CircumstanceDetails = CircumstanceDetails(
            customerDetails = individual,
            flatRateScheme = None,
            ppob = ppobModelMax,
            bankDetails = None,
            returnPeriod = None,
            deregistration = None,
            missingTrader = false,
            changeIndicators = None,
            pendingChanges = None,
            partyType = Some(mockConfig.partyTypes.head),
            commsPreference = None
          )

          lazy val view = injectedView(model, getPartialHtmlNotAgent)(user, messages, mockConfig)
          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not have a change details section for the Business Name" in {
            document.select("#business-name-text").isEmpty shouldBe true
          }
        }
      }

      "without a valid party type" should {

        lazy val view = injectedView(customerInformationWithPartyType(Some("other")), getPartialHtmlNotAgent)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not have a change details section for the Business Name" in {
          document.select("#business-name-text").isEmpty shouldBe true
        }
      }

      "without a party type" should {

        lazy val view = injectedView(customerInformationWithPartyType(None), getPartialHtmlNotAgent)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "have no change details section for the Business Name" in {
          document.select("#business-name-text").isEmpty shouldBe true
        }
      }
    }

    "an Agent" when {

      "changeClient feature switch is on" when {

        "the allowAgentBankAccountChange feature is set to true" should {

          lazy val view = {
            mockConfig.features.changeClientFeature(true)
            mockConfig.features.allowAgentBankAccountChange(true)
            injectedView(customerInformationModelMaxIndividual, getPartialHtmlAgent)(agentUser, messages, mockConfig)
          }

          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not display a breadcrumb trail" in {
            elementExtinct(".breadcrumbs li:nth-of-type(1)")
            elementExtinct(".breadcrumbs li:nth-of-type(2)")
            elementExtinct(".breadcrumbs li:nth-of-type(3)")
          }

          "display the back link" in {
            elementText(".link-back") shouldBe viewMessages.backText
            element(".link-back").attr("href") shouldBe
              mockConfig.agentClientLookupAgentAction
          }

          s"have the correct document title '${viewMessages.title}'" in {
            document.title shouldBe viewMessages.agentTitle
          }

          "have the correct service name" in {
            elementText(".header__menu__proposition-name") shouldBe agentServiceName
          }

          s"have a the correct page heading '${viewMessages.agentHeading}'" in {
            elementText("h1") shouldBe viewMessages.agentHeading
          }

          "have a blank field where the 'Change' link would be for email address" which {

            "displays no text" in {
              elementText("#vat-email-address-status") shouldBe ""
            }

            s"has the correct aria-label text '${viewMessages.changeEmailAddressAgentHidden}'" in {
              element("#vat-email-address-status").attr("aria-label") shouldBe viewMessages.changeEmailAddressAgentHidden
            }
          }
        }

        "the allowAgentBankAccountChange feature is set to false" should {

          lazy val view = {
            mockConfig.features.changeClientFeature(true)
            mockConfig.features.allowAgentBankAccountChange(false)
            injectedView(customerInformationModelMaxIndividual, getPartialHtmlAgent)(agentUser, messages, mockConfig)
          }

          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not display the Change Bank Account details row" in {
            elementExtinct("#bank-details-text")
          }
        }
      }

      "changeClient feature switch is off" should {

        lazy val view = {
          mockConfig.features.changeClientFeature(false)
          injectedView(customerInformationModelMaxIndividual, getPartialHtmlAgent)(agentUser, messages, mockConfig)
        }

        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not display the Finish button" in {
          val finishSelector = "#finish-button"
          elementExtinct(finishSelector)
        }
      }

    }
  }

  "the showPhoneNumbersAndWebsite feature switch is disabled" should {

    lazy val view = {
      mockConfig.features.showContactNumbersAndWebsite(false)
      injectedView(customerInformationModelMaxIndividual, getPartialHtmlNotAgent)(user, messages, mockConfig)
    }

    lazy implicit val document: Document = Jsoup.parse(view.body)

    "the phone numbers section is hidden" in {
      elementExtinct("#vat-phone-numbers")
    }

    "the website section is hidden" in {
      elementExtinct("#vat-website-address")
    }
  }

  "the user is an overseas business" should {

    lazy val view = injectedView(overseasCompany, getPartialHtmlNotAgent)(user, messages, mockConfig)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "not display business name" in {
      elementExtinct("#business-name-text")
      elementExtinct("#business-name")
      elementExtinct("#business-name-status")
    }

    "display PPOB" in {
      elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
      elementText("#businessAddress > ol > li:nth-child(1)") shouldBe "Add Line 1"
    }

    "not give the option to change PPOB" in {
      elementExtinct("#place-of-business-status")
    }
  }

  "the contactDetailsMovedToBTA is disabled" when {

    "an Individual" when {

      "with full information" when {

        "with no pending changes" when {

          "registered for VAT" when {

            "the page is rendered" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationModelMaxIndividual, getPartialHtmlNotAgent)(user, messages, mockConfig)
              }

              lazy implicit val document: Document = Jsoup.parse(view.body)

              "have a section for contact details" which {

                "has a contact details header" in {
                  elementText("#contact-details-section > h2") shouldBe viewMessages.contactDetailsHeading
                }
              }

              "have a section for email address" which {

                "has the heading" in {
                  elementText("#vat-email-address-text") shouldBe viewMessages.emailAddressHeading
                }

                "has the correct value for the email address" in {
                  elementText("#vat-email-address") shouldBe customerInformationModelMaxIndividual.ppob.contactDetails.get.emailAddress.get
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#vat-email-address-status") shouldBe viewMessages.change
                  }

                  s"has the correct aria label text '${viewMessages.changeEmailAddressHidden(PPOBAddressTestConstants.email)}'" in {
                    element("#vat-email-address-status").attr("aria-label") shouldBe
                      viewMessages.changeEmailAddressHidden(PPOBAddressTestConstants.email)
                  }

                  s"has a link to ${mockConfig.vatCorrespondenceChangeEmailUrl}" in {
                    element("#vat-email-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeEmailUrl
                  }
                }
              }

              "have a section for landline number" which {

                "has the heading" in {
                  elementText("#vat-landline-number-text") shouldBe viewMessages.landlineNumberHeading
                }

                "has the correct value for the phone numbers" in {
                  elementText("#vat-landline-number") shouldBe
                    s"${customerInformationModelMaxIndividual.ppob.contactDetails.get.phoneNumber.get}"
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#vat-landline-number-status") shouldBe viewMessages.change
                  }

                  s"has the correct aria label text '${viewMessages.changeLandlineNumbersHidden}'" in {
                    element("#vat-landline-number-status").attr("aria-label") shouldBe
                      viewMessages.changeLandlineNumbersHidden
                  }

                  s"has a link to ${mockConfig.vatCorrespondenceChangeLandlineNumberUrl}" in {
                    element("#vat-landline-number-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeLandlineNumberUrl
                  }
                }
              }

              "have a section for mobile numbers" which {

                "has the heading" in {
                  elementText("#vat-mobile-number-text") shouldBe viewMessages.mobileNumberHeading
                }

                "has the correct value for the phone numbers" in {
                  elementText("#vat-mobile-number") shouldBe
                    s"${customerInformationModelMaxIndividual.ppob.contactDetails.get.mobileNumber.get}"
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#vat-mobile-number-status") shouldBe viewMessages.change
                  }

                  s"has the correct aria label text '${viewMessages.changeMobileNumbersHidden}'" in {
                    element("#vat-mobile-number-status").attr("aria-label") shouldBe
                      viewMessages.changeMobileNumbersHidden
                  }

                  s"has a link to ${mockConfig.vatCorrespondenceChangeMobileNumberUrl}" in {
                    element("#vat-mobile-number-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeMobileNumberUrl
                  }
                }
              }

            }

          }

        }

        "with pending changes" when {

          "with pending email" when {

            "the email has been changed" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationPendingEmailModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
              }
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have link text of '${viewMessages.pending}'" in {
                elementText("#vat-email-address-status") shouldBe viewMessages.pending
              }

              s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
                element("#vat-email-address-status").attr("aria-label") shouldBe
                  viewMessages.pendingEmailAddressHidden
              }

            }

            "the email has been removed" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationModelPendingRemoved("email"), getPartialHtmlNotAgent)(user, messages, mockConfig)
              }
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have link text of '${viewMessages.pending}'" in {
                elementText("#vat-email-address-status") shouldBe viewMessages.pending
              }

              s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
                element("#vat-email-address-status").attr("aria-label") shouldBe
                  viewMessages.pendingEmailAddressHidden
              }
            }

          }

          "with pending Landline" when {

            "the landline has been changed" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationPendingPhoneModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
              }
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have link text of '${viewMessages.pending}'" in {
                elementText("#vat-landline-number-status") shouldBe viewMessages.pending
              }

              s"have the correct aria label text '${viewMessages.pendingLandlineNumbersHidden}'" in {
                element("#vat-landline-number-status").attr("aria-label") shouldBe
                  viewMessages.pendingLandlineNumbersHidden
              }

            }

            "the landline has been removed" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationModelPendingRemoved("landline"), getPartialHtmlNotAgent)(user, messages, mockConfig)
              }
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have link text of '${viewMessages.pending}'" in {
                elementText("#vat-landline-number-status") shouldBe viewMessages.pending
              }

              s"have the correct aria label text '${viewMessages.pendingLandlineNumbersHidden}'" in {
                element("#vat-landline-number-status").attr("aria-label") shouldBe
                  viewMessages.pendingLandlineNumbersHidden
              }

            }

          }

          "with pending Mobile" when {

            "the mobile has been changed" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationPendingMobileModel, getPartialHtmlNotAgent)(user, messages, mockConfig)
              }
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have link text of '${viewMessages.pending}'" in {
                elementText("#vat-mobile-number-status") shouldBe viewMessages.pending
              }

              s"have the correct aria label text '${viewMessages.pendingMobileNumbersHidden}'" in {
                element("#vat-mobile-number-status").attr("aria-label") shouldBe
                  viewMessages.pendingMobileNumbersHidden
              }
            }

            "the mobile has been removed" should {

              lazy val view = {
                mockConfig.features.contactDetailsMovedToBTA(false)
                injectedView(customerInformationModelPendingRemoved("mobile"), getPartialHtmlNotAgent)(user, messages, mockConfig)
              }
              lazy implicit val document: Document = Jsoup.parse(view.body)

              s"have link text of '${viewMessages.pending}'" in {
                elementText("#vat-mobile-number-status") shouldBe viewMessages.pending
              }

              s"have the correct aria label text '${viewMessages.pendingMobileNumbersHidden}'" in {
                element("#vat-mobile-number-status").attr("aria-label") shouldBe
                  viewMessages.pendingMobileNumbersHidden
              }

            }

          }

        }

      }

      "with no email address, landline number, mobile number or website" should {

        lazy val view = {
          mockConfig.features.contactDetailsMovedToBTA(false)
          injectedView(customerInformationModelMin, getPartialHtmlNotAgent)(user, messages, mockConfig)
        }
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "display the 'Not provided' text in place of the email address" in {
          elementText("#vat-email-address") shouldBe "Not provided"
        }

        "display an 'Add' link for changing the email address" which {

          "has the correct text" in {
            elementText("#vat-email-address-status") shouldBe "Add"
          }

          "links to the correspondence details service" in {
            element("#vat-email-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeEmailUrl
          }
        }

        "display the 'Not provided' text in place of the landline number" in {
          elementText("#vat-landline-number") shouldBe "Not provided"
        }

        "display the 'Not provided' text in place of the mobile number" in {
          elementText("#vat-mobile-number") shouldBe "Not provided"
        }

        "display an 'Add' link for changing the landline number" which {

          "has the correct text" in {
            elementText("#vat-landline-number-status") shouldBe "Add"
          }

          "links to the correspondence details service" in {
            element("#vat-landline-number-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeLandlineNumberUrl
          }
        }

        "display an 'Add' link for changing the mobile number" which {

          "has the correct text" in {
            elementText("#vat-mobile-number-status") shouldBe "Add"
          }

          "links to the correspondence details service" in {
            element("#vat-mobile-number-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeMobileNumberUrl
          }
        }

        "display the 'Not provided' text in place of the website address" in {
          elementText("#vat-website-address") shouldBe "Not provided"
        }

        "display an 'Add' link for changing the website address" which {

          "has the correct text" in {
            elementText("#vat-website-address-status") shouldBe "Add"
          }

          "links to the correspondence details service" in {
            element("#vat-website-address-status").attr("href") shouldBe mockConfig.vatCorrespondenceChangeWebsiteUrl
          }
        }

      }

    }

  }

}
