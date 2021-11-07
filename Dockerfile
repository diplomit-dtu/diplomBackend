FROM node:16-alpine AS REACT
WORKDIR /tmp
COPY /web/package.json  ./
COPY /web/src ./src
COPY /web/public ./public
RUN yarn install
RUN yarn build

FROM maven:openjdk AS MAVEN
WORKDIR /tmp
COPY /src/ ./src
COPY /pom.xml ./
COPY --from=REACT /tmp/build ./src/main/webapp/
RUN mvn package

FROM openjdk:14-alpine
WORKDIR /tmp
COPY --from=MAVEN /tmp/target ./
EXPOSE 8080
CMD ["java","-jar", "/tmp/dependency/webapp-runner.jar", "--port","8080", "*.war"]