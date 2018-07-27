# Manage VAT Subscription Frontend

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/hmrc/manage-vat-subscription-frontend.svg)](https://travis-ci.org/hmrc/manage-vat-subscription-frontend) 
[![Download](https://api.bintray.com/packages/hmrc/releases/manage-vat-subscription-frontend/images/download.svg)](https://bintray.com/hmrc/releases/manage-vat-subscription-frontend/_latestVersion)

## Summary
This is the repository for the Manage Vat Subscription frontend.

This service provides end users with a mechanism to manage their VAT subscription.
It allows the users to both view and change their Business name, Business address, bank account details (for repayments) and their VAT return dates.

Backend: https://github.com/hmrc/vat-subscription

Stub: https://github.com/hmrc/manage-vat-subscription-dynamic-stub

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.

## Running

To update from Nexus and start all services from the RELEASE version instead of snapshot

```
sm --start VATC_ALL -f
```

### To run the application locally execute the following:
Kill the service ```sm --stop MANAGE_VAT_SUBSCRIPTION_FRONTEND``` and run:
`sbt "run 9150 -Dapplication.router=testOnlyDoNotUseInAppConf.Routes"`

## Testing
`sbt clean coverage test it:test coverageReport`

## Populating Stub
Run `setup.sh` located in `change-vat-acceptance-tests` under `src/test/resources/stubData`. The script takes 1 argument - the name of the environment to populate the stub in. e.g. `./src/test/resources/stubData/setup.sh local`

## License 

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")

