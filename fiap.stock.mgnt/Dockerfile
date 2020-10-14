FROM openjdk:8-jdk-alpine

WORKDIR '/opt/app'

COPY target/fiap.stock.mgnt-1.0-SNAPSHOT.jar .

EXPOSE 8282

ENTRYPOINT ["java","-jar","fiap.stock.mgnt-1.0-SNAPSHOT.jar"]