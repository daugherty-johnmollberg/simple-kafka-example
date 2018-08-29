package com.ameripride.testkafkastreams.stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.streams.KafkaStreams;


public class StreamImpl extends StreamProperties implements Stream {

    private KafkaStreams kafkaStreams;

    private final StreamInitializer streamInitializer;

    public StreamImpl(StreamInitializer streamInitializer) {
        this.streamInitializer = streamInitializer;
    }

    @Override
    public KafkaStreams getStreams() {
        return this.kafkaStreams;
    }

    @PostConstruct
    public void init() {
        kafkaStreams = streamInitializer.init(this);
        kafkaStreams.start();
    }

    @PreDestroy
    public void destroy() {
        try {
            getStreams().close();
        } catch (Exception e) {
            // ignored
        }
    }
}
