FROM amazoncorretto:17
WORKDIR /app
COPY . .
RUN ./gradlew clean build --no-daemon

EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

CMD ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
