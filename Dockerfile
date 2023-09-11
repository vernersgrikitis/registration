FROM openjdk:17-jdk-alpine

RUN addgroup -S registration && adduser -S appuser -G registration

USER appuser

COPY target/registration-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]