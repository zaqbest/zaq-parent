FROM --platform=$TARGETPLATFORM harbor-dev.fusionfintrade.com:6443/lptest/baseimage:1.0.0-ubuntu-22.04

ARG TARGETPLATFORM
ARG BUILDPLATFORM

WORKDIR /app

COPY entrypoint.sh /app/entrypoint.sh

RUN chmod 755 /app/entrypoint.sh && \
    apt-get update && \
    apt install -y ca-certificates && \
    apt install -y openjdk-8-jdk && \
    apt-get clean && \
    apt-get autoremove

CMD ["/bin/bash"]

ENTRYPOINT ["./entrypoint.sh"]
