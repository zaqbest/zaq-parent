FROM --platform=$TARGETPLATFORM ubuntu:22.04

ARG TARGETPLATFORM
ARG BUILDPLATFORM

WORKDIR /app

COPY ./entrypoint.sh /app/entrypoint.sh

RUN apt update && \
    apt-get upgrade && \
    apt-get install -y ca-certificates wget curl && \
    apt-get clean && \
    apt-get autoremove && \
    chmod 755 /app/entrypoint.sh && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' >/etc/timezone

CMD ["/bin/bash"]

ENTRYPOINT ["./entrypoint.sh"]
