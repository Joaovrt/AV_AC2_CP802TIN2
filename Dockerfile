FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/escola-0.0.1-SNAPSHOT.jar /app/escola.jar

EXPOSE 8081

CMD ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-Dserver.port=8081", "-jar", "escola.jar"]
