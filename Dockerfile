FROM openjdk:17-jdk

WORKDIR /email

COPY target/email-management-0.0.1-SNAPSHOT.jar /email/email.jar

CMD ["java", "-jar", "/email/email.jar"]
