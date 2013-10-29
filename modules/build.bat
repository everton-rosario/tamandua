@echo off
echo Installing all maven modules...
call mvn clean install

echo .
echo Assembling tamandua-aws...
cd tamandua-aws
call mvn assembly:assembly

echo .
echo Assembling tamandua-crawler...
cd ..\tamandua-crawler
call mvn assembly:assembly

echo .
echo Assembling tamandua-generator...
cd ..\tamandua-generator
call mvn assembly:assembly

echo .
echo Assembling tamandua-indexer...
cd ..\tamandua-indexer
call mvn assembly:assembly

echo .
echo Assembling tamandua-sitemap...
cd ..\tamandua-sitemap
call mvn assembly:assembly

rem cd ..
rem copy /Y tamandua-aws\target\*.zip c:\opt\tamandua-aws
rem copy /Y tamandua-crawler\target\*.zip c:\opt\tamandua-crawler
rem copy /Y tamandua-generator\target\*.zip c:\opt\tamandua-generator
rem copy /Y tamandua-indexer\target\*.zip c:\opt\tamandua-indexer
rem copy /Y tamandua-sitemap\target\*.zip c:\opt\tamandua-sitemap
rem 
rem call C:\tools\apache-tomcat-6.0.20\bin\shutdown.bat
rem 
rem del /Q /S C:\tools\apache-tomcat-6.0.20\webapps\web
rem del /Q /S C:\tools\apache-tomcat-6.0.20\webapps\tamandua-ws
rem del /Q /S C:\tools\apache-tomcat-6.0.20\webapps\tamandua-searcher-ws
rem del /Q /S C:\tools\apache-tomcat-6.0.20\webapps\tamandua-redirect
rem 
rem copy /Y tamandua-web\target\tamandua-web-1.0.6-SNAPSHOT.war C:\tools\apache-tomcat-6.0.20\webapps\web.war
rem copy /Y tamandua-ws\target\tamandua-ws-1.0.6-SNAPSHOT.war C:\tools\apache-tomcat-6.0.20\webapps\tamandua-ws.war
rem copy /Y tamandua-searcher-ws\target\tamandua-searcher-ws-1.0.6-SNAPSHOT.war C:\tools\apache-tomcat-6.0.20\webapps\tamandua-searcher-ws.war
rem copy /Y tamandua-redirect\target\tamandua-redirect-1.0.6-SNAPSHOT.war C:\tools\apache-tomcat-6.0.20\webapps\tamandua-redirect.war

rem rem call C:\tools\apache-tomcat-6.0.20\bin\startup.bat

