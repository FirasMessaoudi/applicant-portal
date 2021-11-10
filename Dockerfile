# syntax=docker/dockerfile:experimental
ARG REGISTRY
# stage : download remote files
FROM ${REGISTRY}/abdennour/curl as curl
WORKDIR /data
ARG MAVEN_SETTINGS_XML_URL
ENV MAVEN_SETTINGS_XML_URL=${MAVEN_SETTINGS_XML_URL}
RUN curl -o ./settings.xml -SsL ${MAVEN_SETTINGS_XML_URL}

# stage : download node modules
FROM ${REGISTRY}/node:12.19.0-alpine as node-modules
WORKDIR  /code/build
ARG NPM_CONFIG_REGISTRY
ENV NPM_CONFIG_REGISTRY=${NPM_CONFIG_REGISTRY}
ARG SASS_BINARY_SITE
ENV SASS_BINARY_SITE=${SASS_BINARY_SITE}
COPY shj-applicant-portal-web-fe/package.json ./
RUN npm install

# stage: build frontend
FROM ${REGISTRY}/node:12.19.0-alpine as frontend-build
# WORKDIR /code/build/target/www/app
WORKDIR /code/build
# by running next cmd, /code/build/node_modules folder will exist
COPY --from=node-modules /code/build/node_modules node_modules/
# by running next cmd, /code/build/{docs,e2e,...} exist
COPY shj-applicant-portal-web-fe/ .
# by running bext cmd, /code/build/dist folder exists
RUN npm run build:qa

# stage: build backend
FROM ${REGISTRY}/maven:3.6.0-jdk-13-alpine as build
WORKDIR /code/build
COPY --from=curl /data/settings.xml .

# copy all source code from host, to the current image (c
COPY . .
## copy frontend built files (bundle js,,)
COPY --from=frontend-build  /code/build/dist/ shj-applicant-portal-web-fe/dist/
RUN --mount=type=cache,target=/root/.m2/repository \
  mvn -s settings.xml -Pprod package -DskipTests -DskipFrontendBuild
RUN mv shj-admin-portal-web/target/*war app.war

# FROM nexus.elm.sa/elm-core/tomcat-wkhtmltopdf-ffmpeg:9.0.14-jre-8-alpine-coresnapshot9 as runtime
# COPY --from=build /code/build/app.war /usr/local/tomcat/webapps/ROOT.war
#FROM ${REGISTRY}/abdennour/tomcat:9.0.14-wkhtmlpdf-ffmpeg as runtime
#USER root
#RUN rm -rf /opt/bitnami/tomcat/webapps/ROOT
#COPY --from=build --chown=1001:0 /code/build/app.war /opt/bitnami/tomcat/webapps/ROOT.war
#RUN mkdir -p /opt/wkhtmltox/bin && cp /bin/cat /opt/wkhtmltox/bin/wkhtmltopdf
#USER 1001

### STAGE DEPLOY ###
FROM ${REGISTRY}/openjdk:13-jdk-alpine as runtime
# FROM ${REGISTRY}/bitnami/tomcat:9.0.50 as runtime
WORKDIR /usr/src
COPY --from=build --chown=1001:0 /code/build/app.war .
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.war"]
