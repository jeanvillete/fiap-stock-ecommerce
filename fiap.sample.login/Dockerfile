FROM openjdk:8-jdk-alpine

WORKDIR '/opt/app'

COPY target/fiap.sample.login-1.0-SNAPSHOT.jar .

EXPOSE 8181

ENTRYPOINT ["java","-jar","fiap.sample.login-1.0-SNAPSHOT.jar"]