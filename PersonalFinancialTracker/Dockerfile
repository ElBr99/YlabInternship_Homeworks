FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true
CMD ["mvn", "liquibase:update", "-Pdocker"]
CMD ["java", "-jar", "/app/*.jar"]