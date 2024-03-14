#!/usr/bin/env bash

function getRandom() {
    echo "$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 20)"
}

export secret=${getRandom}
export secret=1qaz!QAZ

export placeholder_hostname=10.30.4.50
export placeholder_minio_username=${secret}
export placeholder_minio_password=${secret}
export placeholder_mongodb_password=${secret}
export placeholder_mysql57_password=${secret}
export placeholder_mysql80_password=${secret}
export placeholder_rabbitmq_username=admin
export placeholder_rabbitmq_password=${secret}
export placeholder_redis_password=${secret}

envsubst < docker-compose-tmpl.yml > docker-compose-result.yml
envsubst < redis-single/redis-tmpl.conf > redis-single/redis.conf
envsubst < seata-server/config/registry-tmpl.conf > seata-server/config/registry.conf
envsubst < seata-server/seataServer-tmpl.properties > seata-server/seataServer.properties
envsubst < rocketmq/broker/conf/brokder-tmpl.conf > rocketmq/broker/conf/brokder.conf
