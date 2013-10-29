#!/bin/bash
echo Installing all maven modules...
mvn clean install

echo .
echo Assembling tamandua-aws...
cd tamandua-aws
mvn assembly:assembly

echo .
echo Assembling tamandua-crawler...
cd ../tamandua-crawler
mvn assembly:assembly

echo .
echo Assembling tamandua-generator...
cd ../tamandua-generator
mvn assembly:assembly

echo .
echo Assembling tamandua-indexer...
cd ../tamandua-indexer
mvn assembly:assembly

echo .
echo Assembling tamandua-sitemap...
cd ../tamandua-sitemap
mvn assembly:assembly

# cd ..
# cp tamandua-aws/target/*.zip /opt/tamandua-aws
# cp tamandua-crawler/target/*.zip /opt/tamandua-crawler
# cp tamandua-generator/target/*.zip /opt/tamandua-generator
# cp tamandua-indexer/target/*.zip /opt/tamandua-indexer
# cp tamandua-sitemap/target/*.zip /opt/tamandua-sitemap
# 
# /home/work/servers/tomcat-6.0.20/bin/shutdown.sh
# 
# rm -rf /home/work/servers/tomcat-6.0.20/webapps/web
# rm -rf /home/work/servers/tomcat-6.0.20/webapps/tamandua-ws
# rm -rf /home/work/servers/tomcat-6.0.20/webapps/tamandua-searcher-ws
# rm -rf /home/work/servers/tomcat-6.0.20/webapps/tamandua-redirect
# 
# cp tamandua-web/target/tamandua-web-1.0.6-SNAPSHOT.war /home/work/servers/tomcat-6.0.20/webapps/web.war
# cp tamandua-ws/target/tamandua-ws-1.0.6-SNAPSHOT.war /home/work/servers/tomcat-6.0.20/webapps/tamandua-ws.war
# cp tamandua-searcher-ws/target/tamandua-searcher-ws-1.0.6-SNAPSHOT.war /home/work/servers/tomcat-6.0.20/webapps/tamandua-searcher-ws.war
# cp tamandua-redirect/target/tamandua-redirect-1.0.6-SNAPSHOT.war /home/work/servers/tomcat-6.0.20/webapps/tamandua-redirect.war

# /home/work/servers/tomcat-6.0.20/bin/startup.sh

