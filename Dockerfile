#FROM openjdk:21-jdk
#ADD target/iPeyu-Backend-admin-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
FROM openjdk:21-jdk
WORKDIR /app
VOLUME /app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
