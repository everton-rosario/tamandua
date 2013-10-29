#!/bin/bash

ME="${0##*/}"

LANG='en_US.UTF-8'
LC_ALL='en_US.UTF-8'

JAVA_OPTS="-Dfile.encoding=UTF-8"

app_home="/opt/tamandua-sitemap"
app_user="everton"
java_exe="java"

class_path="${app_home}/config/"
class_path="${class_path}:`ls ${app_home}/lib/*.jar | tr '\n' ':'`"

class_name="br.com.tamandua.sitemap.App"
class_params="$*"

dir_log="/export/logs/tamandua-sitemap"
out_log="${dir_log}/out.log"
err_log="${dir_log}/err.log"

export PATH LANG LC_ALL JAVA_OPTS

export app_user dir_log java_exe class_path class_name class_params out_log err_log
su -m "${app_user}" -c 'cd "${dir_log}"; "${java_exe}" "${JAVA_OPTS}" -cp "${class_path}" "${class_name}" ${class_params} >> "${out_log}" 2>> "${err_log}" &'