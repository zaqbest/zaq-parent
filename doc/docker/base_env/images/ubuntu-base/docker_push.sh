#!env bash

export DOCKER_REGISTRY_URL=harbor-dev.fusionfintrade.com:6443
export DOCKER_PROJECT=lptest
# 配置正确的用户名密码变量
export DOCKER_USERNAME="XXXXXXXXXXXXXXX"
export DOCKER_PASSWORD="XXXXXXXXXXXXXXX"

# 登录harbor
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin $DOCKER_REGISTRY_URL

docker buildx build \
--sbom=false --provenance=false \
--builder multiarch-builder \
-t $DOCKER_HARBOR_REGISTER_URL/$DOCKER_PROJECT/ubuntu-base:multi-arch \
--platform linux/amd64,linux/arm64 \
-f Dockerfile \
--push .

docker logout $DOCKER_REGISTRY_URL

