#!/bin/bash

cd ../../..

chmod +x bin
chmod +x ./scripts/maven/mvnw

rm -f bin/serverCM.jar
rm -f bin/coreCM.jar

./scripts/maven/mvnw -am --projects climate-monitoring-client clean install
