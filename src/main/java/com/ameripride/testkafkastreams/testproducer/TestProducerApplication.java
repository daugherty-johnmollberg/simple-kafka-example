package com.ameripride.testkafkastreams.testproducer;

import com.ameripride.testkafkastreams.producer.Producer;
import com.ameripride.testkafkastreams.producer.ProducerImpl;
import com.ameripride.testkafkastreams.producer.ProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KeyValue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.apache.kafka.streams.KeyValue.pair;

@SpringBootApplication
@Slf4j
public class TestProducerApplication {

    private static int recordCounter = 1;

    public static void main(String[] args) {
        SpringApplication.run(TestProducerApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("com.ameripride.kafka.producers.test-producer-1")
    Producer testProducer1() {
        List<KeyValue<String, String>> records = Arrays.asList(
                pair("1", "test1"),
                pair("2", "test2"),
                pair("3", "test3")
        );

        return new ProducerImpl(properties -> buildProducer(properties, records));
    }

//    @Bean
//    @ConfigurationProperties("com.ameripride.kafka.producers.test-producer-2")
    Producer testProducer2() {
        return new ProducerImpl(this::buildContinuousProducer);
    }

    private <K, V> KafkaProducer buildProducer(ProducerProperties properties, List<KeyValue<K, V>> records) {
        KafkaProducer<K, V> producer = new KafkaProducer<>(properties.buildProperties());

        records.forEach(record -> {
            log.info(String.format("Producing record: key: %s value: %s", record.key, record.value));
            producer.send(new ProducerRecord<>(
                    properties.getDestTopic(),
                    record.key,
                    record.value));
        });

        return producer;
    }

    private KafkaProducer<String, String> buildContinuousProducer(ProducerProperties properties) {
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties.buildProperties());

        Timer timer = new Timer();
        timer.schedule(new TimedProduceRecord(producer, properties), 0, 60000);

        return producer;
    }

    private class TimedProduceRecord extends TimerTask {
        private final ProducerProperties properties;

        private final KafkaProducer<String, String> producer;

        TimedProduceRecord(KafkaProducer<String, String> producer, ProducerProperties properties) {
            this.producer = producer;
            this.properties = properties;
        }

        public void run() {
            String key = getKey();
            int intKey = Integer.parseInt(key);
            String value = String.format("test %s", key);

            if ((intKey > 5 && intKey < 11) || intKey > 11) {
                log.info(String.format("Skipping record %s", key));
            } else {
                log.info(String.format("Producing record: key: %s value: %s", key, value));
                producer.send(new ProducerRecord<>(
                        properties.getDestTopic(),
                        key,
                        value));
            }
        }


        private String getKey() {
            return String.valueOf(recordCounter++);
        }
    }
}