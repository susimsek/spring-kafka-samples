package io.github.susimsek.springkafkasamples.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Employee",
    description = "Schema to hold Employee information"
)
public record Employee(

    @Schema(
        description = "unique identifier of employee", example = "1"
    )
    Long id,

    @Schema(
        description = "Firstname of the customer", example = "Frodo"
    )
    String firstName,

    @Schema(
        description = "Firstname of the customer", example = "Baggins"
    )
    String lastName) {

}