FROM openjdk:17-jdk-slim

# 로컬에서 빌드한 JAR 파일을 컨테이너로 복사합니다.
COPY build/libs/*.jar app.jar

# 환경 변수 설정
ARG DB_HOST
ARG DB_USER
ARG DB_PASSWORD
ARG CORS_ALLOWED_ORIGINS

# 실제 환경 변수를 설정
ENV DB_HOST=${DB_HOST}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV CORS_ALLOWED_ORIGINS=${CORS_ALLOWED_ORIGINS}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]

## Stage 1: Build
#FROM gradle:7.5-jdk17 AS build
#
## Set the working directory
#WORKDIR /app
#
## Copy the Gradle wrapper and the build.gradle file
#COPY gradlew .
#COPY gradle /app/gradle
#COPY build.gradle settings.gradle /app/
#
## Copy the source code into the container
#COPY src /app/src
#
## Install dos2unix
#RUN apt-get update && apt-get install -y dos2unix
#
## Convert gradlew to UNIX format
#RUN dos2unix gradlew
#
#RUN pwd
#RUN ls -al
#RUN echo hello
#
## Ensure the Gradle wrapper has execution permissions
#RUN chmod +x gradlew
#RUN ls -al gradlew
## Run Gradle build
#RUN ./gradlew build --no-daemon
#
## Stage 2: Run
#FROM openjdk:17-jdk-slim
#
## Set the working directory
#WORKDIR /app
#
## Copy the built JAR file from the build stage
#COPY --from=build /app/build/libs/*.jar app.jar
#
## Environment variables
#ARG DB_HOST
#ARG DB_USER
#ARG DB_PASSWORD
#ARG CORS_ALLOWED_ORIGINS
#
#ENV DB_HOST=${DB_HOST}
#ENV DB_USER=${DB_USER}
#ENV DB_PASSWORD=${DB_PASSWORD}
#ENV CORS_ALLOWED_ORIGINS=${CORS_ALLOWED_ORIGINS}
#
## Expose port
#EXPOSE 8080
#
## Run the JAR file
#ENTRYPOINT ["java", "-jar", "app.jar"]