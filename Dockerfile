FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/escola-0.0.1-SNAPSHOT.jar /app/escola.jar
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java -jar escola.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-default}"]