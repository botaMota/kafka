FROM confluentinc/cp-kafka:7.3.0

USER root

ENV CONNECT_PLUGIN_PATH="/usr/share/java,/opt/connectors"
ENV KAFKA_ADVERTISED_LISTENERS="INTERNAL://kafka-spooldir:19092,EXTERNAL://${DOCKER_HOST_IP:-127.0.0.1}:9092,DOCKER://host.docker.internal:29092"

RUN cd /usr/share/java

RUN curl 'https://d1i4a15mxbxib1.cloudfront.net/api/plugins/jcustenborder/kafka-connect-spooldir/versions/2.0.65/jcustenborder-kafka-connect-spooldir-2.0.65.zip'  > spooldir.zip

RUN dnf install unzip -y

RUN unzip spooldir.zip && mv jcustenborder-kafka-connect-spooldir-2.0.65 spooldir-connector

RUN rm spooldir.zip

RUN mkdir {data,error,finished}

RUN curl "https://api.mockaroo.com/api/58605010?count=1000&key=25fd9c80" > "data/csv-spooldir-source.csv"



