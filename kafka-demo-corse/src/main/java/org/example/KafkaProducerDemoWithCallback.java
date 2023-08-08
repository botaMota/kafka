package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerDemoWithCallback.class.getName());

    public static void main(String[] args) {
        log.info("Start Producer demo with call back");

        //connect to conduktor playground
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"6rGeAvSoZRGtIPreLzsyeX\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI2ckdlQXZTb1pSR3RJUHJlTHpzeWVYIiwib3JnYW5pemF0aW9uSWQiOjc1NDkwLCJ1c2VySWQiOjg3ODI5LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiIyNWI2NTdiOC1lZjJkLTRiMDItOTIyNS01NzQ4YzVmZWI3MjgifX0.emvk_xf5H9IEa2qYWY1izD3lrew9rAuYbMpsgj7GSIM\";");
        properties.setProperty("sasl.mechanism","PLAIN");

        //set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());


        properties.setProperty("batch.size", "400");

        //create producer
        KafkaProducer<String,String> producer = new KafkaProducer(properties);

        for (int j = 0; j < 10 ; j++) {

            for (int i = 0; i < 30 ; i++) {

                //create producer record
                ProducerRecord<String,String> producerRecord = new ProducerRecord("demo_java_topic","Salam layane: "+i);

                //send data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        //Executes every time a record successfully set or an exception is thrown
                        if(e == null){
                            log.info("Received new metadata \n" +
                                    "Topic: "+metadata.topic()+"\n" +
                                    "Partition: "+metadata.partition()+"\n" +
                                    "Offset: "+metadata.offset()+"\n" +
                                    "Timestamp: "+metadata.timestamp()+"\n"
                            );
                        }else{
                            log.error("Error while producing: "+e.getMessage());
                        }
                    }
                });
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }





        //tell the producer to send data and block until done -- synchronous
        producer.flush();

        //flush and close the producer
        producer.close();

        log.info("End producer demo");

    }
}