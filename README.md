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
Kill the service ```sm --stop MANAGE_VAT_SUBSCRIPTION_FRONTEND``` and run:
`sbt "run 9150 -Dapplication.router=testOnlyDoNotUseInAppConf.Routes"`
or alternatively navigate to the cloned frontend repo and run `./run.sh`

## Testing
`sbt clean coverage test it:test coverageReport`

## Populating Stub
Run `populate_stub.sh` located in `vat-view-change-stub-data`. The script takes 1 argument - the name of the environment to populate the stub in. e.g. `./populate_stub.sh local`.
`local` is the default value for this argument if it is not provided.

## License 

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
