@echo off
cd ..\..\..
call scripts\maven\mvnw.cmd -am --projects climate-monitoring-server clean install
pause