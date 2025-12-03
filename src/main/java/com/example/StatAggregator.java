package com.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@Slf4j
public class StatAggregator {
    private final Sinks.Many<StatEvent> triggerSink = Sinks.many()
                                                           .multicast()
                                                           .onBackpressureBuffer();
    public final AtomicLong count = new AtomicLong();

    public void init() {
        triggerSink.asFlux()
                   .bufferTimeout(50, Duration.ofSeconds(1))
                   .log("xxx", Level.INFO)
                   .flatMap(this::processBatch, 1)
                   .subscribeOn(Schedulers.boundedElastic())
                   .subscribe();
    }

    public void onQueryComplete(String queryId) {
        triggerSink.tryEmitNext(new StatEvent(queryId, LocalDateTime.now()));
    }

    public void show() {
        log.info("StatAggregator: {}", count.get());
    }

    private Mono<Void> processBatch(List<StatEvent> batch) {
        return Mono.fromRunnable(() -> {
                       count.addAndGet(batch.size());
                   })
                   .subscribeOn(Schedulers.boundedElastic())
                   .then();
    }
}