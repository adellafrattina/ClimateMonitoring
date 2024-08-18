@echo off
cd ..\..\..
call scripts\maven\mvnw.cmd -am --projects climate-monitoring-client clean install
pause