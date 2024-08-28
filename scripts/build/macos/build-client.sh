#!/bin/bash
cd ../../..
rm -f bin/clientCM.jar
rm -f bin/coreCM.jar
./scripts/maven/mvnw -am --projects climate-monitoring-client clean install
