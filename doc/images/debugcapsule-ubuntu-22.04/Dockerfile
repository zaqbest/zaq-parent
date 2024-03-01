FROM --platform=$TARGETPLATFORM harbor-dev.fusionfintrade.com:6443/lptest/baseimage:1.0.0-ubuntu-22.04

ARG TARGETPLATFORM
ARG BUILDPLATFORM

WORKDIR /app

COPY entrypoint.sh /app/entrypoint.sh

RUN chmod 755 /app/entrypoint.sh && \
    apt-get update && \
    apt install -y ca-certificates && \
    apt install -y openjdk-8-jdk

# Install SSH server
RUN apt-get install -y openssh-server && \
    useradd -rm -d /home/sshuser -s /bin/bash -g root -G sudo -u 1000 sshuser && \
    echo 'sshuser:password' | chpasswd && \
    mkdir /var/run/sshd && \
    apt-get clean && \
    apt-get autoremove

EXPOSE 22

CMD ["/bin/bash"]

ENTRYPOINT ["./entrypoint.sh"]
