#!env bash

export DOCKER_REGISTRY_URL=${DEV_DOCKER_REGISTRY_URL}
export DOCKER_PROJECT=${DEV_DOCKER_PROJECT}
# 配置正确的用户名密码变量
export DOCKER_USERNAME=${DEV_DOCKER_REGISTRY_USERNAME}
export DOCKER_PASSWORD=${DEV_DOCKER_REGISTRY_PASSWORD}

export IMAGE_ID=$DOCKER_REGISTRY_URL/$DOCKER_PROJECT/openjdk8:1.0.0-alpine-3.18
export PLATFROM=linux/amd64,linux/arm64

# 登录harbor
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin $DOCKER_REGISTRY_URL

docker buildx build \
--sbom=false --provenance=false \
--builder multiarch-builder \
-t $IMAGE_ID \
--platform $PLATFROM \
-f Dockerfile \
--push .

docker logout $DOCKER_REGISTRY_URL

