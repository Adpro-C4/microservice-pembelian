FROM openjdk:17-slim-bullseye

WORKDIR /app

COPY build/libs/pembelian-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]