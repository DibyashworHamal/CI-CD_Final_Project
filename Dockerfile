FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="Dibyashwor Hamal" \
      version="1.0" \
      description="Event Booking System Spring Boot application"

RUN addgroup -S ebsgroup && adduser -S ebsuser -G ebsgroup

WORKDIR /app
COPY --from=builder --chown=ebsuser:ebsgroup /app/target/*.jar /app/ebs_app.jar

USER ebsuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar"]
CMD ["/app/ebs_app.jar"]