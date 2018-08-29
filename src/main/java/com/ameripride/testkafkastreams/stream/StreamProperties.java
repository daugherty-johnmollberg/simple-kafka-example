package com.ameripride.testkafkastreams.stream;

import com.ameripride.testkafkastreams.config.BaseKafkaProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

@EqualsAndHashCode(callSuper = true)
@Data
public class StreamProperties extends BaseKafkaProperties {

    @Value("${com.ameripride.kafka.streams.default.application-id:}")
    private String applicationId;

    @Value("${com.ameripride.kafka.streams.default.commit-interval-ms:}")
    private String commitIntervalMs;

    @Value("${com.ameripride.kafka.streams.default.auto-offset-reset:}")
    private String autoOffsetReset;

    @Value("${com.ameripride.kafka.streams.default.source-topic:}")
    private String sourceTopic;

    @Value("${com.ameripride.kafka.streams.default.dest-topic:}")
    private String destTopic;

    @Value("${com.ameripride.kafka.streams.default.deserialization-error-handler:}")
    private String deserializationErrorHandler;

    @Value("${com.ameripride.kafka.streams.default.state-dir:}")
    private String stateDir;

    public Properties buildProperties() {
        final Properties streamsConfiguration = new Properties();

        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, this.getApplicationId());
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, this.getBootstrapServers());
        // Provide the details of our embedded http service that we'll use to connect to this stream
        // instance and discover locations of stores.
        streamsConfiguration.put(StreamsConfig.APPLICATION_SERVER_CONFIG, this.getApplicationServer());
        // Set to earliest so we don't miss any data that arrived in the topics before the process
        // started
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.getAutoOffsetReset());
        // Set the commit interval to 500ms so that any changes are flushed frequently and the top five
        // charts are updated with low latency.
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, this.getCommitIntervalMs());

        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, this.getStateDir());

        // Behavior for bad messages, kafka default is log and shutdown stream. Application default will be to
        // log and continue processing subsequent messages
        streamsConfiguration.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, this.getDeserializationErrorHandler());

        return streamsConfiguration;
    }

}
