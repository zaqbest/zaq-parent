#!env bash

# 配置正确的用户名密码变量
export DOCKER_REGISTRY_URL=${DEV_DOCKER_REGISTRY_URL}
export DOCKER_REGISTRY_PROJECT=${DEV_DOCKER_REGISTRY_PROJECT}
export DOCKER_REGISTRY_USERNAME=${DEV_DOCKER_REGISTRY_USERNAME}
export DOCKER_REGISTRY_PASSWORD=${DEV_DOCKER_REGISTRY_PASSWORD}

export PLATFORMS=${PLATFORMS:=linux/amd64,linux/arm64}
export IMAGE_NAME=baseimage:1.0.0-ubuntu-22.04
export DOCKERFILE_PATH=./Dockerfile
export CONTEXT_PATH=.

##################################################################################
# 下面内容为固定部分
##################################################################################
if [[ "${PLATFORMS}" == "*amd64*" ]] && [[ "${PLATFORMS}" == "*arm64*" ]];
then
    BUILDER_NAME=multiarch-builder
elif [[ "${PLATFORMS}" == "*arm64*" ]]; then
    BUILDER_NAME=arm64-builder
else
    BUILDER_NAME=amd64-builder
fi

# 创建builderx
if ! docker buildx inspect ${BUILDER_NAME} >/dev/null 2>&1; then
  docker buildx create --name ${BUILDER_NAME} --platform $PLATFORMS
fi

# 登录harbor
echo "${DOCKER_REGISTRY_PASSWORD}" | docker login -u "${DOCKER_REGISTRY_USERNAME}" --password-stdin $DOCKER_REGISTRY_URL

docker buildx build \
--sbom=false --provenance=false \
--builder ${BUILDER_NAME} \
-t $DOCKER_REGISTRY_URL/$DOCKER_REGISTRY_PROJECT/${IMAGE_NAME} \
--platform ${PLATFORMS} \
-f ${DOCKERFILE_PATH} \
--push ${CONTEXT_PATH}

docker logout $DOCKER_REGISTRY_URL

