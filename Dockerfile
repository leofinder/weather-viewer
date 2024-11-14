FROM gradle:8.10-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY gradlew ./
RUN gradle dependencies --no-daemon
COPY src/ src/
RUN gradle build \
    --no-daemon \
    --parallel \
    --build-cache \
    -x test

FROM tomcat:10.1-jre17
COPY --from=build /app/build/libs/*.war $CATALINA_HOME/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]