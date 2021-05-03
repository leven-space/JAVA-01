FROM levenchen/openjdk-docker-plus
RUN mkdir /usr/local/kafka
ADD kafka_2.13-2.7.0.tgz /usr/local/kafka
COPY helloworld.sh /usr/local
RUN chmod +x /usr/local/helloworld.sh

WORKDIR /usr/local/kafka
ENTRYPOINT /usr/local/helloworld.sh