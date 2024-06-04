# Stage 1: Download JAR from Nexus using Maven
FROM maven:3.8.1-openjdk-11-slim AS downloader

ARG VERSION
ARG HOST_IP
ARG NEXUS_PORT
ARG GROUP_ID
ARG ARTIFACT_ID

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:get -DremoteRepositories=http://${HOST_IP}:${NEXUS_PORT}/repository/maven-public -DgroupId=${GROUP_ID} -DartifactId=${ARTIFACT_ID} -Dversion=${VERSION} -Dtransitive=false && \
    mvn dependency:copy -Dartifact=${GROUP_ID}:${ARTIFACT_ID}:${VERSION} -DoutputDirectory=.

# Stage 2: Build final image with the downloaded JAR
FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=downloader /app/*.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
