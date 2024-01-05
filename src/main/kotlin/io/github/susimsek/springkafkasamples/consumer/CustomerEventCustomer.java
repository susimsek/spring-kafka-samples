package io.github.susimsek.springkafkasamples.consumer;

import io.github.susimsek.springkafkasamples.dto.Customer;
import io.github.susimsek.springkafkasamples.exception.RetryableMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomerEventCustomer {

    @RetryableTopic(
        attempts = "${spring.kafka.retries.customer.maxRetryAttempts}",
        autoCreateTopics = "${spring.kafka.retries.customer.autoCreateTopics}",
        backoff = @Backoff(
            delayExpression = "${spring.kafka.retries.customer.waitDuration}",
            multiplierExpression = "${spring.kafka.retries.customer.exponentialBackoffMultiplier}"),
        include = {RetryableMessagingException.class},
        sameIntervalTopicReuseStrategy = SameIntervalTopicReuseStrategy.MULTIPLE_TOPICS,
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
        dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    @KafkaListener(topics = "${spring.kafka.bindings.customer.topic}",
        groupId = "${spring.kafka.bindings.customer.group}")
    public void consume(Customer payload) {
        log.info("customer event consumer:  payload: {} ", payload.toString());
        throw new RetryableMessagingException("Retry event.");
    }

    @DltHandler
    public void dlt(Customer data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("Event from topic "+topic+" is dead lettered - event:" + data);
    }
}