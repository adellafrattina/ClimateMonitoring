@echo off
cd ..\..\..
del /f /q bin\clientCM.jar
del /f /q bin\coreCM.jar
call scripts\maven\mvnw.cmd -am --projects climate-monitoring-client clean install
pause