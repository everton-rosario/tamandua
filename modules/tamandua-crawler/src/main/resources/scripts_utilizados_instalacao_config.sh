sudo useradd everton -d /home/everton -m
sudo useradd andre -d /home/andre -m
sudo useradd rafael -d /home/rafael -m

visudo
## ADICIONAR AS LINHAS:
everton ALL=NOPASSWD: ALL
andre ALL=NOPASSWD: ALL
rafael ALL=NOPASSWD: ALL

sudo chsh -s /bin/bash everton
sudo chsh -s /bin/bash andre
sudo chsh -s /bin/bash rafael

sudo mkdir /download
sudo chown everton /download
cd /download
wget http://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/VerifyItem-Start/jdk-6u18-linux-x64.bin?BundledLineItemUUID=RXlIBe.oRvIAAAEn7HkE2xAU&OrderID=NztIBe.oLMwAAAEn4HkE2xAU&ProductID=p_9IBe.pFJcAAAElWitRSbJV&FileName=/jdk-6u18-linux-x64.bin
mv "http://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/VerifyItem-Start/jdk-6u18-linux-x64.bin?BundledLineItemUUID=RXlIBe.oRvIAAAEn7HkE2xAU&OrderID=NztIBe.oLMwAAAEn4HkE2xAU&ProductID=p_9IBe.pFJcAAAElWitRSbJV&FileName=/jdk-6u18-linux-x64.bin" jdk-6u18-linux-x64.bin
chmod 777 jdk-6u18-linux-x64.bin
./jdk-6u18-linux-x64.bin



sudo /opt/tamandua-crawler/scripts/tamandua-crawler.sh --crawl-track-content --letters=e --provider=terra --threads=5
tail -f /export/logs/tamandua-crawler/*log

sudo /opt/tamandua-crawler/scripts/tamandua-crawler.sh --crawl-track-content --letters=i --provider=terra --threads=5
tail -f /export/logs/tamandua-crawler/*log


C:\tools\jdk-1.6_18\bin\java.exe -Xms1024m -Xmx2048m -XX:+AggressiveHeap -classpath activation-1.1.jar:commons-beanutils-1.8.2.jar:commons-beanutils-core-1.8.0.jar:commons-cli-1.2.jar:commons-codec-1.4.jar:commons-collections-3.2.1.jar:commons-configuration-1.6.jar:commons-digester-2.0.jar:commons-httpclient-3.1.jar:commons-io-1.4.jar:commons-lang-2.4.jar:commons-logging-1.1.1.jar:htmlparser-1.6.jar:jericho-html-3.1.jar:jms-1.1.jar:jmxri-1.2.1.jar:jmxtools-1.2.1.jar:log4j-1.2.15.jar:mail-1.4.jar:slf4j-api-1.5.10.jar:slf4j-log4j12-1.5.10.jar:tamandua-crawler-1.0.0-SNAPSHOT.jar" br.com.tamandua.crawler.App
C:\tools\jdk-1.6_18\bin\java.exe -Xms1024m -Xmx2048m -XX:+AggressiveHeap -classpath activation-1.1.jar:commons-beanutils-1.8.2.jar:commons-beanutils-core-1.8.0.jar:commons-cli-1.2.jar:commons-codec-1.4.jar:commons-collections-3.2.1.jar:commons-configuration-1.6.jar:commons-digester-2.0.jar:commons-httpclient-3.1.jar:commons-io-1.4.jar:commons-lang-2.4.jar:commons-logging-1.1.1.jar:htmlparser-1.6.jar:jericho-html-3.1.jar:jms-1.1.jar:jmxri-1.2.1.jar:jmxtools-1.2.1.jar:log4j-1.2.15.jar:mail-1.4.jar:slf4j-api-1.5.10.jar:slf4j-log4j12-1.5.10.jar:tamandua-crawler-1.0.0-SNAPSHOT.jar" br.com.tamandua.crawler.App
