FROM maven:3-jdk-8

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ADD . /usr/src/app
RUN mvn clean install -Dtest=TestWebService
FROM tomcat:8.0-jre8
MAINTAINER "RAJ KUMAR DUBEY" (rajkumar.dubey@heavywater.solutions)
#ADD ./target/*.war /usr/local/tomcat/webapps/
ADD /target/OcrTiffTesseractWebservice.war /usr/local/tomcat/webapps/
