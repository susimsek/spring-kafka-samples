package io.github.susimsek.springkafkasamples.aspect;

import io.github.susimsek.springkafkasamples.annotation.CircuitBreaker;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomCircuitBreakerAspect {

    @Pointcut(
        value = "@annotation(circuitBreaker)",
        argNames = "circuitBreaker")
    public void matchAnnotatedMethod(CircuitBreaker circuitBreaker) {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    @Around(
        value = "matchAnnotatedMethod(circuitBreakerAnnotation) && args(failureSwitchEnabled,..)",
        argNames = "joinPoint,circuitBreakerAnnotation,failureSwitchEnabled")
    public Object circuitBreakerAroundAdvice(ProceedingJoinPoint joinPoint,
                                             CircuitBreaker circuitBreakerAnnotation,
                                             boolean failureSwitchEnabled) throws Throwable {
        if (!failureSwitchEnabled) {
            return joinPoint.proceed();
        }
        Logger log = logger(joinPoint);
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getName());
            throw e;
        }
    }
}