FROM node AS REACT
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

CMD ["java", "-jar","/tmp/Heroku01.jar"]