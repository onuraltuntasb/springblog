FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} springblog-new.jar
ENTRYPOINT ["java","-jar","/springblog-new.jar"]

