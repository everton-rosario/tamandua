@ECHO OFF

rem ##################
rem Batch Runner (windows version) for tamandua-images

set PATHBEFORE=%CD%
set ME=tamandua-images.bat

set JAVA_OPTS=-Xms1024m -Xmx2048m -XX:+AggressiveHeap -Dfile.encoding=UTF-8

set app_home=c:\opt\tamandua-images
set app_user=everton
set java_exe="%JAVA_HOME%\bin\java.exe"

set class_name=br.com.tamandua.images.App

set dir_log=c:\export\logs\tamandua-images
set out_log="%dir_log%\out.log"
set err_log="%dir_log%\err.log"

IF EXIST %dir_log% goto hasDirLog
mkdir c:\export\logs\tamandua-images
:hasDirLog
cd %app_home%\lib

set CLASSPATH=%app_home%\config\
for /f %%a IN ('dir /b *.jar') do call %app_home%\scripts\appendvar.bat %%a


%java_exe% %JAVA_OPTS% -classpath "%CLASSPATH%" %class_name% %*

cd %PATHBEFORE%
