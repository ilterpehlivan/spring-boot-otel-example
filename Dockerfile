#FROM docker.io/maven:3.9.2-eclipse-temurin-20 as DEPS
#WORKDIR /cache
#COPY internal-lib/pom.xml internal-lib/pom.xml
#COPY movie-service/pom.xml movie-service/pom.xml
#COPY user-service/pom.xml user-service/pom.xml
#
#COPY pom.xml .
#
#RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.6.0:go-offline

FROM docker.io/maven:3.9.2-eclipse-temurin-20 as maven
WORKDIR /build

COPY internal-lib/pom.xml internal-lib/pom.xml
COPY movie-service/pom.xml movie-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY pom.xml .

COPY internal-lib/src /build/internal-lib/src
COPY user-service/src /build/user-service/src
COPY movie-service/src /build/movie-service/src


RUN mvn -B -e  clean package -DskipTests=true

FROM eclipse-temurin:20-jdk
WORKDIR /app
# COPY --from=maven /app/target/libs libs
ARG SERVICE_FOLDER
COPY --from=maven /build/${SERVICE_FOLDER}/target/*.jar app.jar

# debug with docker build --progress=plain .
# RUN ls -lrt

ENTRYPOINT ["java","-jar","./app.jar"]
