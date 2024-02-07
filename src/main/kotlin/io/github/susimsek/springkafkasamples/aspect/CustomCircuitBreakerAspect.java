package io.github.susimsek.springkafkasamples.aspect;

import static io.github.susimsek.springkafkasamples.util.SupplierUtil.rethrowSupplier;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.susimsek.springkafkasamples.annotation.CircuitBreaker;
import java.time.Duration;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CustomCircuitBreakerAspect {

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Pointcut(
        value = "@annotation(circuitBreaker)",
        argNames = "circuitBreaker")
    public void matchAnnotatedMethod(CircuitBreaker circuitBreaker) {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around(
        value = "matchAnnotatedMethod(circuitBreakerAnnotation) && args(failureSwitchEnabled,..)",
        argNames = "joinPoint,circuitBreakerAnnotation,failureSwitchEnabled")
    public Object circuitBreakerAroundAdvice(ProceedingJoinPoint joinPoint,
                                             CircuitBreaker circuitBreakerAnnotation,
                                             boolean failureSwitchEnabled) {
        var circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowSize(10)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .failureRateThreshold(50)
            .slowCallRateThreshold(100)
            .minimumNumberOfCalls(5)
            .permittedNumberOfCallsInHalfOpenState(3)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .waitDurationInOpenState(Duration.ofSeconds(60))
            .slowCallDurationThreshold(Duration.ofMillis(2))
            .ignoreExceptions(Throwable.class)
            .build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
            .cancelRunningFuture(true)
            .timeoutDuration(Duration.ofSeconds(50))
            .build();
        Supplier<Object> supplier= rethrowSupplier(joinPoint::proceed);
        circuitBreakerFactory
            .configureDefault(id -> new Resilience4JConfigBuilder(circuitBreakerAnnotation.value())
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(timeLimiterConfig)
                .build());
        return circuitBreakerFactory
            .create(circuitBreakerAnnotation.value())
            .run(supplier, this::fallback);
    }

    @SneakyThrows
    public String fallback(Throwable ex) {
        if (ex instanceof CallNotPermittedException) {
            return "Recovered: " + ex;
        }
        throw ex;
    }

}