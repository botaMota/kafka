package io.conduktor.demo.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.EventSource.Builder;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        BackgroundEventHandler eventHandler = new WikimediaChangeHandler();
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        Builder builder = new Builder(URI.create(url));
        EventSource eventSource = builder.build();

        //start the producer in another thread
        //eventSource.start();


    }
}
