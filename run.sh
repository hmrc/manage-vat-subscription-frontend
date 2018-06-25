#!/usr/bin/env bash
sbt 'run 9150 -Dlogger.resource=logback-test.xml -Dapplication.router=testOnlyDoNotUseInAppConf.Routes'