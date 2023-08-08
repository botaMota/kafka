package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerDemoWithShutdown {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerDemoWithShutdown.class.getName());

    public static void main(String[] args) {
        log.info("Start Consumer demo");
        String groupId = "my-java-app";
        String topic = "demo_java_topic";

        //connect to Conduktor playground
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"6rGeAvSoZRGtIPreLzsyeX\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI2ckdlQXZTb1pSR3RJUHJlTHpzeWVYIiwib3JnYW5pemF0aW9uSWQiOjc1NDkwLCJ1c2VySWQiOjg3ODI5LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiIyNWI2NTdiOC1lZjJkLTRiMDItOTIyNS01NzQ4YzVmZWI3MjgifX0.emvk_xf5H9IEa2qYWY1izD3lrew9rAuYbMpsgj7GSIM\";");
        properties.setProperty("sasl.mechanism","PLAIN");

        //set consumer properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");


        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer(properties);

        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()....");
                consumer.wakeup();
                //join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            //read data
            consumer.subscribe(Arrays.asList(topic));

            while (true){
                log.info("Polling");
                ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String,String> record:records){
                    log.info("key: "+record.key()+" , value: "+record.value());
                    log.info("partition: "+record.partition()+" , offset: "+record.offset());
                }
            }
        }catch (WakeupException e){
            log.info("Consumer shutdown");
        }catch (Exception e){
            log.error("Unexpected exception: "+e.getMessage());
        }finally {
            //close the consumer. this will also commit the offsets
            consumer.close();
            log.info("The consumer is shut down");
        }



    }
}