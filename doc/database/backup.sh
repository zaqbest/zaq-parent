#!/usr/bin/env bash

DB_HOST=mysql.aireport.svc
DB_PORT=3306
DB_USER=root
DB_PASSWORD=123456
DBS="fusion_chatreport nacos"

BACKUP_DIR=/Users/lipan/MyData/tmpDir/dbbackup
FILENAME=aireport_`date +%Y%m%d%H%M%S`

mysqldump -h${DB_HOST} -P${DB_PORT} -p${DB_PASSWORD} -u${DB_USER} --skip-extended-insert --databases ${DBS} > ${BACKUP_DIR}/${FILENAME}.sql