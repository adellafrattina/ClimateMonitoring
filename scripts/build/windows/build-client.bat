@echo off
cd ..\..\..
scripts\maven\mvnw.cmd -am --projects climate-monitoring-client clean install
pause