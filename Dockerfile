FROM openjdk:21-slim AS build
# Instala Maven
RUN apt-get update && \
    apt-get install -y maven git && \
    apt-get clean &&

ARG URL_REPO=https://github.com/GianfrancoPupiales/GR04_1BT3_622_25A.git
ARG BRANCH=docker

RUN git clone -b ${BRANCH} ${URL_REPO} /app && \
    ls -la /app

WORKDIR /app

RUN mvn clean package

FROM tomcat:10.1.28-jdk21-slim

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/

EXPOSE 9090

CMD ["catalina.sh", "run"]


