#!/usr/bin/env bash
DEST_USER=${DEV_DOCKER_REGISTRY_USERNAME}
DEST_PASSWORD=${DEV_DOCKER_REGISTRY_PASSWORD}

export SKOPEO_OPTION="--multi-arch all --override-os linux  --insecure-policy --dest-tls-verify=false --dest-username=${DEST_USER} --dest-password=${DEST_PASSWORD}"

function image_sync() {
    IMAGE_SRC=$1
    IMAGE_DST=$2
    printf "同步镜像${IMAGE_SRC} => ${IMAGE_DST} 开始....\n"
    eval "skopeo copy  ${SKOPEO_OPTION}  docker://${IMAGE_SRC} docker://${IMAGE_DST}"
    printf "同步镜像${IMAGE_SRC} => ${IMAGE_DST} 完成!\n"
}

image_sync rancher/rancher:v2.6.6 harbor-dev.fusionfintrade.com:6443/lptest/rancher/rancher:v2.6.6
