FROM --platform=$TARGETPLATFORM harbor-dev.fusionfintrade.com:6443/lptest/baseimage:1.0.0-alpine-3.18

ARG TARGETPLATFORM
ARG BUILDPLATFORM

MAINTAINER lipan <lipan.me@qq.com>

WORKDIR /app

COPY entrypoint.sh /app/entrypoint.sh

RUN apk add openjdk8 && \
    rm -rf /tmp/* /var/cache/apk/* && \
    chmod 755 /app/entrypoint.sh

CMD ["/bin/sh"]

ENTRYPOINT ["./entrypoint.sh"]
