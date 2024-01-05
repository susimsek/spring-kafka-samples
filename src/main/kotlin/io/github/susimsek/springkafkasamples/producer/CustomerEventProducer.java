package io.github.susimsek.springkafkasamples.producer;

import io.github.susimsek.springkafkasamples.config.KafkaProperties;
import io.github.susimsek.springkafkasamples.dto.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventProducer {

    private final KafkaTemplate<String,Customer> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    public static final String CUSTOMER_BINDING_NAME = "customer";

    public void publish(Customer customer) {
        var binding = kafkaProperties.getBindings().get(CUSTOMER_BINDING_NAME);
        kafkaTemplate.send(binding.getTopic(), customer);
    }
}
