@ECHO OFF

rem ##################
rem Batch Runner (windows version) for tamandua-indexer

set PATHBEFORE=%CD%
set ME=tamandua-indexer.bat

set indexer_HOME=c:\export\crawled
set JAVA_OPTS=-Xms5120m -Xmx5120m -XX:+AggressiveHeap

set app_home=c:\opt\tamandua-indexer
set app_user=everton
set java_exe=%JAVA_HOME%\bin\java.exe

set class_name=br.com.tamandua.indexer.App

set dir_log=c:\export\logs\tamandua-indexer
set out_log="%dir_log%\out.log"
set err_log="%dir_log%\err.log"

IF EXIST %dir_log% goto hasDirLog
mkdir c:\export\logs\tamandua-indexer
:hasDirLog
cd %app_home%\lib

set CLASSPATH=%app_home%\config\
for /f %%a IN ('dir /b *.jar') do call %app_home%\scripts\appendvar.bat %%a


%java_exe% %JAVA_OPTS% -classpath "%CLASSPATH%" %class_name% %*

cd %PATHBEFORE%
