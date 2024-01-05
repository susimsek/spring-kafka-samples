package io.github.susimsek.springkafkasamples.config;


import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.kafka")
@Getter
@Setter
public class KafkaProperties {
    private Map<String, Binding> bindings;

    @Getter
    @Setter
    public static class Binding {
        private String topic;
        private String group;
    }
}
