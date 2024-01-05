package io.github.susimsek.springkafkasamples.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(
    info = @Info(
        title = "Spring Kafka Sample REST APIs Documentation",
        description = "Spring Kafka Sample REST APIs Documentation",
        version = "v1",
        contact = @Contact(
            name = "Şuayb Şimşek",
            email = "suaybsimsek58@gmail.com",
            url = "https://www.susimsek.github.io"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    )
)
public class OpenApiConfig {

}
