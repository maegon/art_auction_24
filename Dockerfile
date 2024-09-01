FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/static/images /app/resources/static/images
EXPOSE 8080
RUN mkdir -p /temp/images/
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]


