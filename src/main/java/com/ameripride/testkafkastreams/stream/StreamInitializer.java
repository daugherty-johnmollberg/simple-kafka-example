package com.ameripride.testkafkastreams.stream;

import org.apache.kafka.streams.KafkaStreams;

public interface StreamInitializer {

    KafkaStreams init(StreamProperties properties);

}
