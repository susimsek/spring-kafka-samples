package io.github.susimsek.springkafkasamples.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Customer",
    description = "Schema to hold Customer information"
)
public record Customer(

    @Schema(
        description = "unique identifier of customer", example = "1"
    )
    Long id,

    @Schema(
        description = "Name of the customer", example = "John"
    )
    String name,

    @Schema(
        description = "Email address of the customer", example = "john@gmail.com"
    )
    String email) {

}