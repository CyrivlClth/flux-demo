package com.example.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class Sync {
    private final ConcurrentMap<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    public void add(String item) {
        disposableMap.computeIfAbsent(item, key -> Flux.just(key)
                                                       .expand(ignored -> process(key)
                                                               .thenReturn(key)
                                                               .delayElement(Duration.ofSeconds(10)))
                                                       .subscribe());
    }

    public Mono<Void> process(String item) {
        return Mono.fromRunnable(() -> {
                       log.info("item: {}", item);
                       if (item.equals("b")) {
                           try {
                               Thread.sleep(20_000L);
                           } catch (InterruptedException e) {
                               throw new RuntimeException(e);
                           }
                       }
                   })
                   .subscribeOn(Schedulers.boundedElastic())
                   .doOnError(t -> log.error("error", t))
                   .then();
    }

    public static void main(String[] args) throws IOException {
        Sync sync = new Sync();
        sync.add("a");
        sync.add("b");

        System.in.read();
    }
}
