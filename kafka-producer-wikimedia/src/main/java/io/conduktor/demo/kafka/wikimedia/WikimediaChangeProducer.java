package io.conduktor.demo.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.EventSource.Builder;
import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.StreamException;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
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

        //set high throughput producer configs
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));

        //create producer
        KafkaProducer<String,String> producer = new KafkaProducer(properties);

        String topic = "wikimedia.recentchange";

        BackgroundEventHandler eventHandler = new WikimediaChangeHandler();
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        Builder builder = new Builder(URI.create(url));
        EventSource eventSource = builder.build();

        try {
            eventSource.start();
            Iterable<MessageEvent> messageEvents = eventSource.messages();

            messageEvents.forEach(msg ->{
                log.info("details msg : "+msg.getLastEventId() + " | "+msg.getEventName());
                ProducerRecord<String,String> producerRecord = new ProducerRecord(topic ,msg.getData());
                producer.send(producerRecord);
            });

            producer.close();
        } catch (StreamException e) {
            throw new RuntimeException(e);
        }



        //start the producer in another thread
        //eventSource.start();


    }
}
