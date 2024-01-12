package io.github.susimsek.springkafkasamples.exception;

public class TimeoutException extends RuntimeException {
    public TimeoutException(String message) {
        super(message);
    }
}