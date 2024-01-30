#!/usr/bin/env bash

function getRandom() {
    echo "$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 20)"
}

export placeholder_hostname=10.10.1.1
export placeholder_minio_username=$(getRandom)
export placeholder_minio_password=$(getRandom)
export placeholder_mongodb_password=$(getRandom)
export placeholder_mysql57_password=$(getRandom)
export placeholder_mysql80_password=$(getRandom)
export placeholder_nacos_database=$(getRandom)
export placeholder_nacos_database_host=mysql80
export placeholder_nacos_database_port=3306
export placeholder_nacos_username=nacos
export placeholder_nacos_password=$(getRandom)
export placeholder_rabbitmq_username=admin
export placeholder_rabbitmq_password=$(getRandom)

envsubst < docker-compose-template.yml > docker-compose-result.yml
