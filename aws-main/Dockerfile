FROM openjdk:11-slim as runtime
EXPOSE 8801
#Set app home folder
ENV APP_HOME /app
# add curl
RUN apt update && apt-get install -y curl

#Possibility to set JVM options (https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)
ENV JAVA_OPTS=""
#Create base app folder
RUN mkdir $APP_HOME
#Create folder to save configuration files
RUN mkdir $APP_HOME/config
#Create folder with application logs
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
VOLUME $APP_HOME/config
WORKDIR $APP_HOME
# Just echo so we can see, if everything is there :)
RUN ls -l
#Copy executable jar file from the builder image
ARG JAR_FILE
COPY target/$JAR_FILE app.jar

HEALTHCHECK --interval=3s --start-period=60s --timeout=3s CMD curl --fail http://localhost:8801/actuator/health 2>&1 | grep UP || exit 1

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
#Second option using shell form:
#ENTRYPOINT exec java $JAVA_OPTS -jar app.jar $0 $@
