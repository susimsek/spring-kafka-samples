package io.github.susimsek.springkafkasamples.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.susimsek.springkafkasamples.annotation.CircuitBreaker;
import io.github.susimsek.springkafkasamples.client.JSONPlaceHolderClient;
import io.github.susimsek.springkafkasamples.dto.PostDTO;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final JSONPlaceHolderClient jsonPlaceHolderClient;

    public String failure(
        boolean failureSwitchEnabled,
        boolean slowCallSwitchEnabled)  {
        if (slowCallSwitchEnabled) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "slow Hello World from backend";
        }
        if (failureSwitchEnabled) {
            throw new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
        }
        return "Hello World from backend";
    }

    @CircuitBreaker
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
            .waitDurationInOpenState(Duration.ofSeconds(60))
            .slowCallDurationThreshold(Duration.ofSeconds(3))
            .ignoreExceptions(Throwable.class)
            .build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
            .cancelRunningFuture(true)
            .timeoutDuration(Duration.ofSeconds(50))
            .build();
        var circuitBreakerId = String.format("%s-backendService", companyId);
        Supplier<String> supplier= () -> failure(failureSwitchEnabled, slowCallSwitchEnabled);
        circuitBreakerFactory
            .configureDefault(id -> new Resilience4JConfigBuilder(circuitBreakerId)
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(timeLimiterConfig)
                .build());
       return circuitBreakerFactory
           .create(circuitBreakerId)
           .run(supplier, this::fallback);
    }

    @SneakyThrows
    public String fallback(Throwable ex) {
        if (ex instanceof CallNotPermittedException) {
            return "Recovered: " + ex;
        }
        throw ex;
    }


    @CircuitBreaker("ALL_POSTS")
    public List<PostDTO> getPosts( boolean failureSwitchEnabled){
        return jsonPlaceHolderClient.getPosts();
    }
}