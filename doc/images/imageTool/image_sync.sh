#!/usr/bin/env bash

export SKOPEO_OPTION="--multi-arch all --override-os linux  --insecure-policy --dest-tls-verify=false --dest-username=${DEV_DOCKER_REGISTRY_USERNAME} --dest-password=${DEV_DOCKER_REGISTRY_PASSWORD}"

function image_sync() {
    IMAGE_SRC=$1
    IMAGE_DST=$2
    printf "同步镜像${IMAGE_SRC} => ${IMAGE_DST} 开始....\n"
    eval "skopeo copy  ${SKOPEO_OPTION}  docker://${IMAGE_SRC} docker://${IMAGE_DST}"
    printf "同步镜像${IMAGE_SRC} => ${IMAGE_DST} 完成!\n"
}

#image_sync curve25519xsalsa20poly1305/openvpn-socks5 ${DEV_DOCKER_REGISTRY_URL}/${DEV_DOCKER_REGISTRY_PROJECT}/openvpn-socks5
image_sync harbor-dev.fusionfintrade.com:6443/sps/industry-fin-platform:v1.0.0 ${DEV_DOCKER_REGISTRY_URL}/${DEV_DOCKER_REGISTRY_PROJECT}/industry-fin-platform:v1.0.0
image_sync harbor-dev.fusionfintrade.com:6443/sps/sps-rule-web:v1.0.0 ${DEV_DOCKER_REGISTRY_URL}/${DEV_DOCKER_REGISTRY_PROJECT}/sps-rule-web:v1.0.0