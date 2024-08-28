#!/bin/bash
cd "$(dirname "$0")/../../../"
rm -f bin/serverCM.jar
rm -f bin/coreCM.jar
./scripts/maven/mvnw -am --projects climate-monitoring-server clean install
