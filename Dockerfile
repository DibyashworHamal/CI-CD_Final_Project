FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Dibyashwor Hamal" \
      version="1.0" \
      description="Event Booking System Spring Boot application"

RUN addgroup -S ebsgroup && adduser -S ebsuser -G ebsgroup

WORKDIR /app

# COPY the exact file Jenkins just downloaded from Nexus!
COPY --chown=ebsuser:ebsgroup target/ebs-app.jar /app/ebs-app.jar

USER ebsuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar"]
CMD ["/app/ebs-app.jar"]