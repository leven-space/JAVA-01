FROM levenchen/openjdk-docker-plus
RUN mkdir /usr/local/activemq
ADD apache-activemq-5.16.1-bin.tar.gz /usr/local/activemq
COPY helloworld.sh /usr/local
RUN chmod +x /usr/local/helloworld.sh

WORKDIR /usr/local/activemq
ENTRYPOINT /usr/local/helloworld.sh