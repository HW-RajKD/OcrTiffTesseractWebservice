FROM maven:3-jdk-8

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ONBUILD ADD . /usr/src/app
ONBUILD RUN mvn install -Dtest=TestWebService
FROM tomcat:8.0-jre8
MAINTAINER "RAJ KUMAR DUBEY" (rajkumar.dubey@heavywater.solutions)
ADD /target/SampleSkeleton.war /usr/local/tomcat/webapps/
