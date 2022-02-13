FROM maven:3.6.3 AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN mvn package

FROM adoptopenjdk/openjdk15-openj9:alpine-jre

WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/brokerage-service*.jar /opt/app/brokerage-service.jar

ENTRYPOINT ["java","-jar","brokerage-service.jar"]