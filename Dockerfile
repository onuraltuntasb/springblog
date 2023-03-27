FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} springblog-new.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar","/springblog-new.jar"]

