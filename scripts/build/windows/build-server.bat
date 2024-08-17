@echo off
cd ..\..\..
scripts\maven\mvnw.cmd -am --projects climate-monitoring-server clean install
pause