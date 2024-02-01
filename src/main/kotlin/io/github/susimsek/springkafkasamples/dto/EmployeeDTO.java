package io.github.susimsek.springkafkasamples.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Schema(
    name = "Employee",
    description = "Schema to hold Employee information"
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO extends RepresentationModel<EmployeeDTO> {

    @Schema(
        description = "unique identifier of employee", example = "1"
    )
    private Long id;

    @Schema(
        description = "Firstname of the customer", example = "Frodo"
    )
    private String firstName;

    @Schema(
        description = "Firstname of the customer", example = "Baggins"
    )
    private String lastName;
}