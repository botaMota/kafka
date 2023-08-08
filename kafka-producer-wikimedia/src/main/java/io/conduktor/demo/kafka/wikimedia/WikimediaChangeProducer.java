package io.conduktor.demo.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.EventHandler;
import java.net.URI;
import java.util.Properties;

public class WikimediaChangeProducer {

    private static final Logger log = LoggerFactory.getLogger(WikimediaChangeProducer.class.getName());
    public static void main(String[] args) {

        log.info("Start Producer demo");

        //connect to local conduktor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");


        //set producer properties
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create producer
        KafkaProducer<String,String> producer = new KafkaProducer(properties);

        String topic = "wikimedia.recentchange";

        EventHandler eventHandler = TODO;
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        //start the producer in another thread
        eventSource.start();


    }
}