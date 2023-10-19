FROM --platform=$TARGETPLATFORM harbor-dev.fusionfintrade.com:6443/lptest/baseimage:1.0.0-alpine-3.18

ARG TARGETPLATFORM
ARG BUILDPLATFORM

MAINTAINER lipan <lipan.me@qq.com>

ENV KUBE_LATEST_VERSION="v1.23.17"

RUN apk add --update ca-certificates && \
    apk add --update -t deps && \
    apk add --update gettext && \
    curl -L https://storage.googleapis.com/kubernetes-release/release/${KUBE_LATEST_VERSION}/bin/${TARGETPLATFORM}/kubectl -o /usr/local/bin/kubectl && \
    chmod +x /usr/local/bin/kubectl && \
    apk del --purge deps && \
    rm /var/cache/apk/*

ENTRYPOINT ["kubectl"]
CMD ["--help"]
