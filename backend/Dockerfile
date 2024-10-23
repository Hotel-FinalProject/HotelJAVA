FROM eclipse-temurin:17-jdk-jammy as builder
VOLUME /tmp
COPY build/libs/erppro-0.0.1-SNAPSHOT.jar app.jar
ENV USE_POST 8081
ENV USE_PROFILE server
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${USE_PROFILE}", "-Dserver.port=${USE_POST}", "/app.jar"]


# gradle로 된 프로젝트일 경우 ->
# docker build -t sumin22/erppro:0.1  .
