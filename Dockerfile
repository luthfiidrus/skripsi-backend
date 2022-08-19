FROM openjdk:17-oracle
EXPOSE 8080
ADD target/final-0.0.1-SNAPSHOT.jar final.jar
ENTRYPOINT ["java", "-jar", "/final.jar"]