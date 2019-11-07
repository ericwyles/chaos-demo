package com.ericwyles.chaosdemo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChaosService {

    RemoteClient client;

    public ChaosService(RemoteClient client) {
        this.client = client;
    }

    // this method has circuit breaker with fallback
    @CircuitBreaker(name = "getMessageId", fallbackMethod = "getMessageIdFallback")
    public Integer getMessageId() {
        return client.getRandom();
    }

    public Integer getMessageIdFallback(Throwable t) {
        log.warn("Received an exception, returning fallback value.");
        return -1;
    }

    // this method has no circuit breaker or fallback
    public Integer getMessageIdBrittle() {
        return client.getRandom();
    }
}
