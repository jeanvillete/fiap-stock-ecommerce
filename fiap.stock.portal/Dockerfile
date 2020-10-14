FROM openjdk:8-jdk-alpine

WORKDIR '/opt/app'

COPY target/fiap.stock.portal-1.0-SNAPSHOT.jar .

EXPOSE 8383

ENTRYPOINT ["java","-jar","fiap.stock.portal-1.0-SNAPSHOT.jar"]