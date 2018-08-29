package com.ameripride.testkafkastreams.testconsumer;

import com.ameripride.testkafkastreams.stream.Stream;
import com.ameripride.testkafkastreams.stream.StreamImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class TestCopyStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCopyStreamApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("com.ameripride.kafka.streams.test-consumer-1")
    public Stream testConsumer1() {
        return this.buildCopyStream();
    }

    @Bean
    @ConfigurationProperties("com.ameripride.kafka.streams.test-consumer-2")
    public Stream testConsumer2() {
        return this.buildCopyStream();
    }

    private Stream buildCopyStream() {
        return new StreamImpl(properties -> {
            final StreamsBuilder myBuilder = new StreamsBuilder();

            log.info(String.format(
                    "building copy stream: %s to %s",
                    properties.getSourceTopic(),
                    properties.getDestTopic()));

            myBuilder.stream(properties.getSourceTopic(), Consumed.with(Serdes.String(), Serdes.String()))
                    .to(properties.getDestTopic(), Produced.with(Serdes.String(), Serdes.String()));

            return new KafkaStreams(myBuilder.build(), properties.buildProperties());
        });
    }
}
