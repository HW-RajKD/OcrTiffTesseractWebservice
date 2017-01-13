FROM maven:3.2-jdk-8-onbuild
CMD ["mvn clean install -Dtest=TestWebService"]
FROM tomcat:8.0-jre8
MAINTAINER "RAJ KUMAR DUBEY" (rajkumar.dubey@heavywater.solutions)
ADD /target/SampleSkeleton.war /usr/local/tomcat/webapps/
