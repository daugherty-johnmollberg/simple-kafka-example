package com.ameripride.testkafkastreams.producer;

import org.apache.kafka.clients.producer.KafkaProducer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ProducerImpl extends ProducerProperties implements Producer {

    private KafkaProducer producer;

    private final ProducerInitializer producerInitializer;

    public ProducerImpl(ProducerInitializer producerInitializer) {
        this.producerInitializer = producerInitializer;
    }

    @Override
    public KafkaProducer getProducer() {
        return this.producer;
    }

    @PostConstruct
    public void init() {
        producer = producerInitializer.init(this);
    }

    @PreDestroy
    public void destroy() {
        try {
            getProducer().close();
        } catch (Exception e) {
            // ignored
        }
    }
}
