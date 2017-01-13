FROM maven:3-jdk-8

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ADD . /usr/src/app
RUN mvn clean install -Dtest=TestWebService
FROM tomcat:8.0-jre8
MAINTAINER "RAJ KUMAR DUBEY" (rajkumar.dubey@heavywater.solutions)
ADD /root/.m2/repository/solutions/heavywater/services/OcrTiffTesseractWebservice/OcrTiffTesseractWebservice/0.0.1-SNAPSHOT/OcrTiffTesseractWebservice-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
