package com.ameripride.testkafkastreams.producer;

import com.ameripride.testkafkastreams.config.BaseKafkaProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProducerProperties extends BaseKafkaProperties {

    @Value("${com.ameripride.kafka.producers.default.application-id:}")
    private String applicationId;

    @Value("${com.ameripride.kafka.producers.default.dest-topic:}")
    private String destTopic;

    public Properties buildProperties() {
        final Properties producerProperties = new Properties();

        // Where to find Kafka broker(s).
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.getBootstrapServers());
        // Provide the details of our embedded http service that we'll use to connect to this stream
        // instance and discover locations of stores.
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return producerProperties;
    }
}
