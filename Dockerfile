#FROM maven:3.6.0-jdk-11-slim AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml package

#
# Package stage
#


# we reference it directly

FROM maven:3.6.3-openjdk-15 as build
ARG google_api_key
ARG mongodb_uri
ENV GOOGLE_API_KEY=$google_api_key
ENV MONGODB_URI=$mongodb_uri
WORKDIR /home/app
COPY src /home/app/src
COPY pom.xml /home/app
EXPOSE 80
RUN mvn package

#ENTRYPOINT ["java","$JAVA_OPTS","-jar","target/dependency/webapp-runner.jar","--port", "$PORT","target/*.war"]

FROM tomcat:jdk15-openjdk-slim
COPY --from=build /home/app/target/diplompBackend.war /usr/local/tomcat
#ENTRYPOINT ["java","$JAVA_OPTS","-jar","target/dependency/webapp-runner.jar","--port", "$PORT","target/*.war"]
#EXPOSE 80
#ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]