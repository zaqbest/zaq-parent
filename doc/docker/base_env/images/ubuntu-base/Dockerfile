FROM --platform=$TARGETPLATFORM ubuntu:20.04

ARG TARGETPLATFORM
ARG BUILDPLATFORM

WORKDIR /app

COPY entrypoint.sh /app/entrypoint.sh

RUN apt-get update && \
    apt install -y iproute2 net-tools iputils-ping procps tcpdump vim wget curl && \
    apt-get clean && \
    apt-get autoremove && \
    chmod 755 /app/entrypoint.sh

CMD ["/bin/bash"]

ENTRYPOINT ["./entrypoint.sh"]
