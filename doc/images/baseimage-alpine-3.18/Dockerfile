FROM --platform=$TARGETPLATFORM alpine:3.18

ARG TARGETPLATFORM
ARG BUILDPLATFORM

MAINTAINER lipan <lipan.me@qq.com>

WORKDIR /app

COPY entrypoint.sh /app/entrypoint.sh

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories && \
    apk update && \
    apk add curl wget tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo Asia/Shanghai > /etc/timezone && \
    apk del tzdata && \
    rm -rf /tmp/* /var/cache/apk/* && \
    chmod 755 /app/entrypoint.sh

CMD ["/bin/sh"]

ENTRYPOINT ["./entrypoint.sh"]
