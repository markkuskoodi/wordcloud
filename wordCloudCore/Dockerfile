FROM gradle:7.5.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17.0-jdk

#EXPOSE 9000

COPY --from=build /home/gradle/src/build/libs/wordCloudCore-0.0.1-SNAPSHOT.jar ./wordCloudCore.jar

ENTRYPOINT ["java", "-jar", "wordCloudCore.jar"] 
