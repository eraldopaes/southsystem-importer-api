FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/southsystem-importer-api-0.0.1-SNAPSHOT.jar southsystem-importer-api.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/southsystem-importer-api.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]