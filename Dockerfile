#FROM openjdk:17-jdk-slim
#
## bash 설치
#RUN apt-get update && apt-get install -y bash dos2unix && rm -rf /var/lib/apt/lists/*
#
## Gradle Wrapper를 실행하여 JAR 파일을 빌드합니다.
#COPY . /app
#WORKDIR /app
#
## gradlew 파일의 줄 바꿈 형식을 변환합니다.
#RUN dos2unix ./gradlew
#
## gradlew에 실행 권한을 부여합니다.
#RUN chmod +x ./gradlew
#
## Gradle 빌드를 수행합니다. (테스트를 생략)
#RUN bash ./gradlew clean build -x test
#
## JAR 파일을 컨테이너에 복사합니다.
#COPY build/libs/*.jar app.jar
#
## 환경 변수 설정
#ARG DB_HOST
#ARG DB_USER
#ARG DB_PASSWORD
#
## 실제 환경 변수를 설정
#ENV DB_HOST=${DB_HOST}
#ENV DB_USER=${DB_USER}
#ENV DB_PASSWORD=${DB_PASSWORD}
#
#EXPOSE 8080
#
#ENTRYPOINT ["java","-jar","/app.jar"]

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