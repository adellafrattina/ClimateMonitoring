@echo off
cd ..\..\..
del /f /q bin\serverCM.jar
del /f /q bin\coreCM.jar
call scripts\maven\mvnw.cmd -am --projects climate-monitoring-server clean install
pause