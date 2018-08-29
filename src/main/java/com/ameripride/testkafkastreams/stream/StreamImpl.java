package com.ameripride.testkafkastreams.stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;

@Slf4j
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
        log.info(String.format("Starting stream with properties %s", this.toString()));
        kafkaStreams = streamInitializer.init(this);
        kafkaStreams.start();
    }

    @PreDestroy
    public void destroy() {
        log.error("destroying stream");
        try {
            getStreams().close();
        } catch (Exception e) {
            // ignored
        }
    }
}
