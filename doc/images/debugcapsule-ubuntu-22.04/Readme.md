启动容器
```bash
docker run -itd -p 1022:22 --privileged --name debugcapsule01 harbor-dev.fusionfintrade.com:6443/lptest/debugcapsule:1.0.0-ubuntu-22.04
```

连接容器 密码：password
```bash
ssh sshuser@localhost -p 1022
```