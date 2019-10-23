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

package views.customerInfo

import assets.CircumstanceDetailsTestConstants._
import assets.CustomerDetailsTestConstants.individual
import assets.DeregistrationTestConstants._
import assets.PPOBAddressTestConstants
import assets.PPOBAddressTestConstants.ppobModelMax
import assets.messages.{BaseMessages, ReturnFrequencyMessages, CustomerCircumstanceDetailsPageMessages => viewMessages}
import models.circumstanceInfo.{CircumstanceDetails, MTDfBMandated}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import utils.ImplicitDateFormatter._
import views.ViewBaseSpec

class CustomerCircumstanceDetailsViewSpec extends ViewBaseSpec with BaseMessages {

  "Rendering the Customer Details page" when {

    "an Individual" when {

      "with full information" when {

        "with no pending changes" when {

          "registered for VAT" when {

            "useVatReturnPeriodFrontend feature switch is off" should {

              lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
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
                element("#breadcrumb-vat").attr("href") shouldBe "ye olde vat summary url"
              }

              "have a section for registration status" which {

                "has a registration header" in {
                  elementText("#registration-section > h2") shouldBe viewMessages.registrationStatusHeading
                }

                "has a registration status header" in {
                  elementText("#registration-status-text") shouldBe viewMessages.statusText
                }

                "displays the correct registration status" in {
                  elementText("#registration-status") shouldBe viewMessages.registeredStatus
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

                "has the correct heading" in {
                  elementText("#vat-return-dates-text") shouldBe viewMessages.returnFrequencyHeading
                }

                "has the correct current frequency" in {
                  elementText("#vat-return-dates") shouldBe ReturnFrequencyMessages.option3Mar
                }

                "has a change link" which {

                  s"has the wording '${viewMessages.change}'" in {
                    elementText("#vat-return-dates-status") shouldBe viewMessages.change
                  }

                  s"has a link to ${controllers.returnFrequency.routes.ChooseDatesController.show().url}" in {
                    element("#vat-return-dates-status").attr("href") shouldBe controllers.returnFrequency.routes.ChooseDatesController.show().url
                  }
                }
              }

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

              "not display the making tax digital section" in {
                elementExtinct("#mtd-section")
              }
            }

            "useVatReturnPeriodFrontend feature switch is on" should {

              lazy val view = {
                mockConfig.features.useVatReturnPeriodFrontend(true)
                views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividual)(user, messages, mockConfig)
              }

              lazy implicit val document: Document = Jsoup.parse(view.body)

              "have a section for return frequency" which {

                s"has a change link to ${mockConfig.vatReturnPeriodFrontendUrl}" in {
                  element("#vat-return-dates-status").attr("href") shouldBe mockConfig.vatReturnPeriodFrontendUrl
                }
              }
            }
          }
        }

        "with pending changes" when {

          "deregistering" when {

            "deregistration date is in the future" should {

              lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelFutureDereg)(user, messages, mockConfig)
              lazy implicit val document: Document = Jsoup.parse(view.body)

              "have a section for registration status" which {

                "displays the correct registration status" in {
                  elementText("#registration-status") shouldBe viewMessages.futureDereg(toLongDate(futureDate))
                }

                "has the 'how to register' link" in {
                  elementText("#registration-status-link") shouldBe viewMessages.howToRegister
                  element("#registration-status-link").attr("href") shouldBe "https://www.gov.uk/vat-registration/how-to-register"
                }
              }
            }

            "deregistration is still pending" should {

              lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelDeregPending)(user, messages, mockConfig)
              lazy implicit val document: Document = Jsoup.parse(view.body)

              "have a section for registration status" which {

                "displays the correct registration status" in {
                  elementText("#registration-status") shouldBe viewMessages.deregPending
                }

                "states that the decision is 'pending'" in {
                  elementText("#registration-status-link") shouldBe viewMessages.pending
                }
              }

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
          }

          "with pending email" should {

            lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationPendingEmailModel)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-email-address-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
              element("#vat-email-address-status").attr("aria-label") shouldBe
                viewMessages.pendingEmailAddressHidden
            }
          }

          "with pending PPOB" should {

            lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationPendingPPOBModel)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#place-of-business-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
              element("#place-of-business-status").attr("aria-label") shouldBe
                viewMessages.pendingBusinessAddressHidden
            }
          }

          "with pending Landline" should {

            lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationPendingPhoneModel)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-landline-number-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
              element("#vat-landline-number-status").attr("aria-label") shouldBe
                viewMessages.pendingLandlineNumbersHidden
            }
          }

          "with pending Mobile" should {

            lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationPendingMobileModel)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-mobile-number-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
              element("#vat-mobile-number-status").attr("aria-label") shouldBe
                viewMessages.pendingMobileNumbersHidden
            }
          }

          "with pending Website" should {

            lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationPendingWebsiteModel)(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"have link text of '${viewMessages.pending}'" in {
              elementText("#vat-website-address-status") shouldBe viewMessages.pending
            }

            s"have the correct aria label text '${viewMessages.pendingEmailAddressHidden}'" in {
              element("#vat-website-address-status").attr("aria-label") shouldBe
                viewMessages.pendingWebsiteAddressHidden
            }
          }
        }
      }

      "deregistered for VAT" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNoPendingIndividualDeregistered)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "have a section for registration status" which {

          "has a registration header" in {
            elementText("#registration-section > h2") shouldBe viewMessages.registrationStatusHeading
          }

          "has a registration status header" in {
            elementText("#registration-status-text") shouldBe viewMessages.statusText
          }

          "displays the correct registration status" in {
            elementText("#registration-status") shouldBe viewMessages.deregStatus(toLongDate(pastDate))
          }

          "has the 'how to register' link" in {
            elementText("#registration-status-link") shouldBe viewMessages.howToRegister
            element("#registration-status-link").attr("href") shouldBe "https://www.gov.uk/vat-registration/how-to-register"
          }
        }
      }

      "with no email address, landline number, mobile number or website" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationModelMin)(user, messages, mockConfig)
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

    "an Organisation" when {

      "with a valid party type" when {

        "with an organisation name" should {

          "have a change details section for the Business Name" which {

            lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(Some("2")))(user, messages, mockConfig)
            lazy implicit val document: Document = Jsoup.parse(view.body)

            s"has the heading '${viewMessages.organisationNameHeading}'" in {
              elementText("#business-name-text") shouldBe viewMessages.organisationNameHeading
            }
          }
        }

        "with no organisation name" should {

          val model: CircumstanceDetails = CircumstanceDetails(
            mandationStatus = MTDfBMandated,
            customerDetails = individual,
            flatRateScheme = None,
            ppob = ppobModelMax,
            bankDetails = None,
            returnPeriod = None,
            deregistration = None,
            changeIndicators = None,
            pendingChanges = None,
            partyType = Some(mockConfig.partyTypes.head)
          )

          lazy val view = views.html.customerInfo.customer_circumstance_details(model)(user, messages, mockConfig)
          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not have a change details section for the Business Name" in {
            document.select("#business-name-text").isEmpty shouldBe true
          }
        }
      }

      "without a valid party type" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(Some("other")))(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "not have a change details section for the Business Name" in {
          document.select("#business-name-text").isEmpty shouldBe true
        }
      }

      "without a party type" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationWithPartyType(None))(user, messages, mockConfig)
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
            mockConfig.features.allowAgentBankAccountChange(true)
            views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
          }

          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not display a breadcrumb trail" in {
            elementExtinct(".breadcrumbs li:nth-of-type(1)")
            elementExtinct(".breadcrumbs li:nth-of-type(2)")
            elementExtinct(".breadcrumbs li:nth-of-type(3)")
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

          "display the 'Change client' link" in {
            println("----------------------------AHHHHHHH---------------------------")
            println(view)
            elementText("#change-client-text") shouldBe viewMessages.newChangeClientDetails
            element("#change-client-link").attr("href") shouldBe
              controllers.agentClientRelationship.routes.ConfirmClientVrnController.changeClient().url
          }

          "display the Finish button" in {
            val finishSelector = "#finish"
            elementText(finishSelector) shouldBe viewMessages.finish
            element(finishSelector).attr("href") shouldBe "/agent-action"
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
            mockConfig.features.allowAgentBankAccountChange(false)
            views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(agentUser, messages, mockConfig)
          }

          lazy implicit val document: Document = Jsoup.parse(view.body)

          "not display the Change Bank Account details row" in {
            elementExtinct("#bank-details-text")
          }
        }
      }

      "client is MTD" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationRegisteredIndividual)(agentUser, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "have a section for making tax digital" which {

          "has the correct section header" in {
            elementText("#mtd-section > h2") shouldBe viewMessages.mtdSectionHeading
          }

          "has the correct row heading" in {
            elementText("#opt-in-text") shouldBe viewMessages.statusText
          }

          "has the correct description" in {
            elementText("#opt-in") shouldBe viewMessages.optedIn
          }

          "has a change link" which {

            s"has the wording '${viewMessages.optOut}'" in {
              elementText("#opt-in-status") shouldBe viewMessages.optOut
            }

            s"has the correct aria label text '${viewMessages.changeMtdStatusHidden}'" in {
              element("#opt-in-status").attr("aria-label") shouldBe viewMessages.changeMtdStatusHidden
            }

            s"has a link to ${mockConfig.vatOptOutUrl}" in {
              element("#opt-in-status").attr("href") shouldBe mockConfig.vatOptOutUrl
            }
          }
        }
      }

      "client is non-MTDfB" should {

        lazy val view = views.html.customerInfo.customer_circumstance_details(customerInformationNonMtd)(user, messages, mockConfig)
        lazy implicit val document: Document = Jsoup.parse(view.body)

        "hide the making tax digital section" in {
          elementExtinct("#mtd-section")
        }
      }
    }
  }

    "the showPhoneNumbersAndWebsite feature switch is disabled" should {

      lazy val view = {
        mockConfig.features.showContactNumbersAndWebsite(false)
        views.html.customerInfo.customer_circumstance_details(customerInformationModelMaxIndividual)(user, messages, mockConfig)
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

      lazy val view = views.html.customerInfo.customer_circumstance_details(overseasCompany)(user, messages, mockConfig)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "not display business name" in {
        elementExtinct("#business-name-text")
        elementExtinct("#business-name")
        elementExtinct("#business-name-status")
      }

      "display PPOB" in {
        elementText("#businessAddressHeading") shouldBe viewMessages.businessAddressHeading
        elementText("#businessAddress > ol > li:nth-child(1)" ) shouldBe "Add Line 1"
      }

      "not give the option to change PPOB" in {
        elementExtinct("#place-of-business-status")
      }
    }
}
