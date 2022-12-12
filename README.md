# Manage VAT Subscription Frontend

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Summary
This is the repository for the Manage Vat Subscription frontend.

This service provides end users with a mechanism to manage their VAT subscription.
It allows the users to both view and change their business name, business address, bank account details (for repayments) and their VAT return dates.

Backend: https://github.com/hmrc/vat-subscription

Stub: https://github.com/hmrc/vat-subscription-dynamic-stub

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a JRE to run.

### To run the application locally execute the following:

In order to run this microservice, you must have SBT installed. You should then be able to start the application using:

`./run.sh`

## Testing
`sbt clean coverage test it:test coverageReport`

## License 

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
