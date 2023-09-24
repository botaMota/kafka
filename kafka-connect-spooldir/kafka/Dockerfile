FROM confluentinc/cp-kafka

USER root

ENV CONNECT_PLUGIN_PATH="/usr/share/java,/opt/connectors" \
    KAFKA_ADVERTISED_LISTENERS="INTERNAL://kafka-spooldir:19092,EXTERNAL://${DOCKER_HOST_IP:-127.0.0.1}:9092,DOCKER://host.docker.internal:29092" \
    KAFKA_ZOOKEEPER_CONNECT=host.docker.internal \
    KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT,DOCKER:PLAINTEXT \
    KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL

RUN adduser mota \
    && dnf install sudo -y \
    && dnf install vim -y \
    && sudo usermod -aG root mota \
    && mkdir /opt/connectors \
    && cd /opt/connectors \
    && curl 'https://d1i4a15mxbxib1.cloudfront.net/api/plugins/jcustenborder/kafka-connect-spooldir/versions/2.0.65/jcustenborder-kafka-connect-spooldir-2.0.65.zip'  > spooldir.zip \
    && dnf install unzip -y \
    && unzip spooldir.zip \
    && mv jcustenborder-kafka-connect-spooldir-2.0.65 spooldir-connector \
    && rm spooldir.zip \
    && mkdir {data,error,finished} \
    && curl "https://api.mockaroo.com/api/58605010?count=1000&key=25fd9c80" > "data/csv-spooldir-source.csv"

EXPOSE 9092







