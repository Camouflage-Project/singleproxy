FROM alpine:3.12

RUN apk --update add privoxy runit tini

COPY service /etc/service/

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["tini", "--", "/entrypoint.sh"]
