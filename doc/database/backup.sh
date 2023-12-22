#!/usr/bin/env bash

DB_HOST=
DB_PORT=
DB_USER=
DB_PASSWORD=
DBS=""

BACKUP_DIR=/Users/lipan/MyData/tmpDir/dbbackup
FILENAME=aireport_`date +%Y%m%d%H%M%S`

mysqldump -h${DB_HOST} -P${DB_PORT} -p${DB_PASSWORD} -u${DB_USER} --skip-extended-insert --databases ${DBS} > ${BACKUP_DIR}/${FILENAME}.sql
