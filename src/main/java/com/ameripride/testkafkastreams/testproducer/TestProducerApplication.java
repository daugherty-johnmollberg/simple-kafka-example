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

import static org.apache.kafka.streams.KeyValue.pair;

@SpringBootApplication
@Slf4j
public class TestProducerApplication {

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
}
