FROM gradle:7.5.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17.0-jdk

EXPOSE 9001

COPY --from=build /home/gradle/src/build/libs/wordCloudTextProcess-0.0.1-SNAPSHOT.jar ./wordCloudTextProcess.jar

ENTRYPOINT ["java", "-jar", "wordCloudTextProcess.jar"]
