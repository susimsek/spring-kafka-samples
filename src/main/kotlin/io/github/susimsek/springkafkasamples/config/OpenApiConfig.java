package io.github.susimsek.springkafkasamples.config;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    @Profile("!prod")
    public GroupedOpenApi actuatorApi(
        OpenApiCustomizer actuatorOpenApiCustomiser,
        OperationCustomizer actuatorCustomizer,
        WebEndpointProperties endpointProperties,
        @Value("${springdoc.version}") String appVersion){
        return GroupedOpenApi.builder()
            .group("actuator")
            .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
            .addOpenApiCustomizer(actuatorOpenApiCustomiser)
            .addOperationCustomizer(actuatorCustomizer)
            .pathsToExclude("/health/*")
            .addOpenApiCustomizer(openApi -> openApi.info(new io.swagger.v3.oas.models.info.Info()
                .title("Actuator API")
                .version(appVersion)))
            .build();
    }

    @Bean
    public GroupedOpenApi circuitBreakerGroup(@Value("${springdoc.version}") String appVersion) {
        return GroupedOpenApi.builder().group("circuit-breaker")
            .addOpenApiCustomizer(openApi -> openApi.info(new io.swagger.v3.oas.models.info.Info()
                .title("Circuit Breaker Demo API").version(appVersion)))
            .pathsToMatch("/api/v1/circuit-breaker/**")
            .build();
    }

    @Bean
    public GroupedOpenApi kafkaGroup(@Value("${springdoc.version}") String appVersion) {
        return GroupedOpenApi.builder().group("kafka")
            .addOpenApiCustomizer(openApi -> openApi.info(new io.swagger.v3.oas.models.info.Info()
                .title(" Kafka Demo API").version(appVersion)))
            .pathsToMatch("/api/v1/kafka/**")
            .build();
    }
}
