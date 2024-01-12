package io.github.susimsek.springkafkasamples.service;

import static java.util.Arrays.asList;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackendService {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public String failure(
        boolean failureSwitchEnabled,
        boolean slowCallSwitchEnabled) {
        if (slowCallSwitchEnabled) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("InterruptedException: {}", e.getMessage());
            }
        }
        if (failureSwitchEnabled) {
            throw new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
        }
        return "Hello World from backend";
    }
    public String doSomething(
        boolean failureSwitchEnabled,
        boolean slowCallSwitchEnabled,
        String userId) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowSize(10)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .failureRateThreshold(50)
            .slowCallRateThreshold(100)
            .minimumNumberOfCalls(5)
            .permittedNumberOfCallsInHalfOpenState(3)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .waitDurationInOpenState(Duration.ofSeconds(5))
            .slowCallDurationThreshold(Duration.ofSeconds(3))
            .recordExceptions(
                HttpServerErrorException.class,
                TimeoutException.class,
                IOException.class)
            .build();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry
            .circuitBreaker(String.format("%s-backendService", userId),
                circuitBreakerConfig);
        Supplier<String> supplier= () -> failure(failureSwitchEnabled, slowCallSwitchEnabled);
        Supplier<String> decoratedSupplier= Decorators
            .ofSupplier(supplier)
            .withCircuitBreaker(circuitBreaker)
            .withFallback(asList(TimeoutException.class,
                    HttpServerErrorException.class),
                this::fallback)
            .decorate();
        return decoratedSupplier.get();
    }

    public String fallback(Throwable ex) {
        return "Recovered: " + ex.toString();
    }
}