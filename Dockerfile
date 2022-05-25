FROM azul/zulu-openjdk-alpine:11
RUN apk add --no-cache curl
RUN apk add --no-cache libcrypto1.1=1.1.1n-r0 libssl1.1=1.1.1n-r0 apk-tools=2.12.7-r0 libretls=3.3.3p1-r3 zlib-static
RUN apk upgrade busybox ssl_client
VOLUME /tmp
ARG ARTIFACT_NAME
ADD target/${ARTIFACT_NAME} app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
HEALTHCHECK --interval=5m --timeout=3s --start-period=1m \
CMD curl --fail --location --request GET 'http://localhost:9001/actuator/health' || exit 1