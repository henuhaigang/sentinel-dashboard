FROM pc-shop:jdk1.8
VOLUME /tmp
ADD sentinel-dashboard.jar sentinel-dashboard.jar
RUN sh -c 'touch /sentinel-dashboard.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /sentinel-dashboard.jar" ]
