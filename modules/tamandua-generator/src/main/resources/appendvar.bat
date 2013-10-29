@echo off
rem Process the first argument
if ""%1"" == """" goto end
if "%CLASSPATH%" == "" goto emptyCP
set CLASSPATH=%CLASSPATH%;%1
shift
goto notEmptyCP
:emptyCP
set CLASSPATH=%1
:notEmptyCP

:end
