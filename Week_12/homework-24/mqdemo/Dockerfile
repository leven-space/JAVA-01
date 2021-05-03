FROM levenchen/openjdk-docker-plus
RUN mkdir /usr/local/workspace
ADD /target/mqdemo-0.0.1-SNAPSHOT.jar /usr/local/workspace/app.jar
WORKDIR /usr/local/workspace
ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","app.jar"]