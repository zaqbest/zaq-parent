#!/bin/sh

/usr/sbin/sshd -D

exec $@
