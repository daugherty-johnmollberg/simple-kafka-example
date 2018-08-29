package com.ameripride.testkafkastreams.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class BaseKafkaProperties {

    @Value("${com.ameripride.kafka.schema-registry}")
    private String schemaRegistry;

    @Value("${com.ameripride.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${com.ameripride.kafka.application-server}")
    private String applicationServer;
}
