#!/bin/bash

ME="${0##*/}"

LANG='en_US.UTF-8'
LC_ALL='en_US.UTF-8'

CRAWLER_HOME="/export/crawled"
JAVA_OPTS="-server -Xms256m -Xmx768m -XX:+AggressiveHeap"

app_home="/opt/tamandua-crawler"
app_user="everton"
java_exe="/download/jdk1.6.0_18/bin/java"

class_path="${app_home}/config/"
class_path="${class_path}:`ls ${app_home}/lib/*.jar | tr '\n' ':'`"

class_name="br.com.tamandua.crawler.App"
class_params="$*"

dir_log="/export/logs/tamandua-crawler"
out_log="${dir_log}/out.log"
err_log="${dir_log}/err.log"

export PATH LANG LC_ALL CRAWLER_HOME JAVA_OPTS

export app_user dir_log java_exe class_path class_name class_params out_log err_log
su -m "${app_user}" -c 'cd "${dir_log}"; "${java_exe}" -cp "${class_path}" "${class_name}" ${class_params} >> "${out_log}" 2>> "${err_log}" &'

