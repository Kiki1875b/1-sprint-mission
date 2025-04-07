
FROM amazoncorretto:17 as builder
WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
COPY src/ src/

RUN ./gradlew clean build -x checkstyleMain -x checkstyleTest -x test --no-daemon


FROM amazoncorretto:17
WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
