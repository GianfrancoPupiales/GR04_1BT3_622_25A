FROM openjdk:21-slim AS build

RUN apt-get update && \
    apt-get install -y maven git && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ARG URL_REPO=https://github.com/GianfrancoPupiales/GR04_1BT3_622_25A.git
ARG BRANCH=master

RUN git clone -b ${BRANCH} ${URL_REPO} /app && \
    ls -la /app

WORKDIR /app

RUN mvn clean package


FROM tomcat:jdk21-openjdk-slim

# Cambiar el puerto de Tomcat de 8080 a 9090
RUN sed -i 's/port="8080"/port="9090"/' /usr/local/tomcat/conf/server.xml

# Crear carpeta para imágenes y darle permisos al usuario que ejecuta Tomcat
RUN mkdir -p /app/uploads/products && \
    chown -R 1000:1000 /app/uploads

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/

EXPOSE 9090

CMD ["catalina.sh", "run"]