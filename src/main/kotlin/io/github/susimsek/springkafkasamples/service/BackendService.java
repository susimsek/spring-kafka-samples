package io.github.susimsek.springkafkasamples.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.susimsek.springkafkasamples.exception.TimeoutException;
import java.io.IOException;
import java.time.Duration;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackendService {

    private final CircuitBreakerFactory circuitBreakerFactory;

    public String failure(
        boolean failureSwitchEnabled,
        boolean slowCallSwitchEnabled)  {
        if (slowCallSwitchEnabled) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new TimeoutException("process failed because connection failed.");
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
        String companyId) {
        var circuitBreakerConfig = CircuitBreakerConfig.custom()
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
                CallNotPermittedException.class,
                HttpServerErrorException.class,
                TimeoutException.class,
                IOException.class)
            .build();
        var circuitBreakerId = String.format("%s-backendService", companyId);
        Supplier<String> supplier= () -> failure(failureSwitchEnabled, slowCallSwitchEnabled);
        circuitBreakerFactory
            .configureDefault(id -> new Resilience4JConfigBuilder(circuitBreakerId)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
       return
           circuitBreakerFactory
           .create(circuitBreakerId)
           .run(supplier, this::fallback);
    }

    public String fallback(Throwable ex) {
        return "Recovered: " + ex.toString();
    }
}