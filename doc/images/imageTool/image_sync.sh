#!/usr/bin/env bash

export SKOPEO_OPTION="--multi-arch all --override-os linux  --insecure-policy --dest-tls-verify=false --dest-username=${DEV_DOCKER_REGISTRY_USERNAME} --dest-password=${DEV_DOCKER_REGISTRY_PASSWORD}"

function image_sync() {
     IMAGE_SRC=$1
     IMAGE_DST=$2

     if [ "x$IMAGE_DST" == "x" ];then
      IMAGE_DST=$(echo "$IMAGE_SRC" | awk -F'/' '{print $NF}')
      IMAGE_DST=${DEV_DOCKER_REGISTRY_URL}/${DEV_DOCKER_REGISTRY_PROJECT}/$IMAGE_DST
     fi

    printf "同步镜像${IMAGE_SRC} => ${IMAGE_DST} 开始....\n"
    eval "skopeo copy  ${SKOPEO_OPTION}  docker://${IMAGE_SRC} docker://${IMAGE_DST}"
    printf "同步镜像${IMAGE_SRC} => ${IMAGE_DST} 完成!\n"
}

#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/nacos-server:v2.1.0
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/nacos-peer-finder-plugin:1.1
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/rabbitmq:3.9.7-management-plugins
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/minio:latest
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/mongo:5.0.2
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/mongo-k8s-sidecar:latest
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/kubernetes-library:kubernetes-zookeeper1.0-3.4.10
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/mysql:5.7
#image_sync registry.cn-hangzhou.aliyuncs.com/zaqbest/csi:xtrabackup-1.0

