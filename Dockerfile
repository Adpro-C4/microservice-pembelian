FROM openjdk:23-slim-bullseye

WORKDIR /app

COPY build/libs/pembelian-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]


WORKDIR /app


COPY build/libs/pembelian-0.0.1-SNAPSHOT.jar /app/app.jar


CMD ["java", "-jar", "app.jar"]