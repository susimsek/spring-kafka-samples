package io.github.susimsek.springkafkasamples.exception;

public class RetryableMessagingException extends RuntimeException {

    public RetryableMessagingException(String message) {
        super(message);
    }
}