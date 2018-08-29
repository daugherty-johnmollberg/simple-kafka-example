package com.ameripride.testkafkastreams.producer;

import org.apache.kafka.clients.producer.KafkaProducer;

public interface ProducerInitializer {

    KafkaProducer init(ProducerProperties properties);
}
