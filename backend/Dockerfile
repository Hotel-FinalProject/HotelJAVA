FROM eclipse-temurin:17-jdk-jammy as builder

# JAR 파일 복사
VOLUME /tmp
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# 기본 환경 변수 설정 (필요한 값들은 실행 시 주입)
ENV USE_PROFILE=server
ENV DATABASE_URL=${DATABASE_URL}
ENV DB_NAME=${DB_NAME}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV MAIL_SECRET=${MAIL_SECRET}
ENV NAVER_CLIENT_ID=${NAVER_CLIENT_ID}
ENV NAVER_CLIENT_SECRET=${NAVER_CLIENT_SECRET}
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV API_KEY=${API_KEY}
ENV IMPORT_KEY=${IMPORT_KEY}
ENV IMPORT_SECRET_KEY=${IMPORT_SECRET_KEY}
ENV ACCESS_KEY_ID=${ACCESS_KEY_ID}
ENV ACCESS_SCRETKEY_ID=${ACCESS_SCRETKEY_ID}

# Spring Boot 애플리케이션 실행 시 환경 변수 사용
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${USE_PROFILE}", \
    "-Dserver.port=8081", \
    "-Dspring.datasource.url=${DATABASE_URL}", \
    "-Dspring.datasource.username=${DB_USERNAME}", \
    "-Dspring.datasource.password=${DB_PASSWORD}", \
    "-Dspring.mail.username=sudongtaemsa6th@gmail.com", \
    "-Dspring.mail.password=${MAIL_SECRET}", \
    "-Dapi.key=${API_KEY}", \
    "-Dimport.api.key=${IMPORT_KEY}", \
    "-Dimport.secret.key=${IMPORT_SECRET_KEY}", \
    "-Daws.access.key.id=${ACCESS_KEY_ID}", \
    "-Daws.secret.access.key=${ACCESS_SCRETKEY_ID}", \
    "/app.jar"]
