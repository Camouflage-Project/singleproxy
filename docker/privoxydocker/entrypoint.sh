#!/bin/sh


sed -i "s/8118/$1/g" /etc/service/privoxy/config

cat /etc/service/privoxy/config

/bin/echo "hello world"
runsvdir /etc/service