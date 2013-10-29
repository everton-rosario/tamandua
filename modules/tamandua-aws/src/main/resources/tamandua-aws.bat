@ECHO OFF

rem ##################
rem Batch Runner (windows version) for tamandua-aws

set PATHBEFORE=%CD%
set ME=tamandua-aws.bat

set JAVA_OPTS=-Xms256m -Xmx256m -XX:+AggressiveHeap -Dfile.encoding=UTF-8

set app_home=c:\opt\tamandua-aws
set app_user=everton
set java_exe="%JAVA_HOME%\bin\java.exe"

set class_name=br.com.tamandua.aws.App

set dir_log=c:\export\logs\tamandua-aws
set out_log="%dir_log%\out.log"
set err_log="%dir_log%\err.log"

IF EXIST %dir_log% goto hasDirLog
mkdir c:\export\logs\tamandua-aws
:hasDirLog
cd %app_home%\lib

set CLASSPATH=%app_home%\config\
for /f %%a IN ('dir /b *.jar') do call %app_home%\scripts\appendvar.bat %%a


%java_exe% %JAVA_OPTS% -classpath "%CLASSPATH%" %class_name% %*

cd %PATHBEFORE%
