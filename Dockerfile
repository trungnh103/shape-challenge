FROM gradle:jdk8 AS build
MAINTAINER trungnh103
COPY --chown=gradle:gradle . /workspace
WORKDIR /workspace
RUN gradle build --no-daemon
FROM openjdk:8-jre-slim
COPY --from=build /workspace/build/libs/*.jar ./app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
