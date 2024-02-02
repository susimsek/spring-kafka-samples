package io.github.susimsek.springkafkasamples.controller;

import io.github.susimsek.springkafkasamples.assembler.EmployeeModelAssembler;
import io.github.susimsek.springkafkasamples.dto.EmployeeDTO;
import io.github.susimsek.springkafkasamples.entity.EmployeeEntity;
import io.github.susimsek.springkafkasamples.mapper.EmployeeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "employee",
    description = "Employee REST APIs"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hateoas")
public class EmployeeController {

    private final EmployeeModelAssembler employeeModelAssembler;
    private final EmployeeMapper employeeMapper;

    @Operation(
        summary = "Create customer ",
        description = "REST API to create new customer"
    )
    @PostMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EmployeeDTO> newEmployee(@RequestBody EmployeeDTO employee)  {
        try {
            var entity = employeeMapper.toEntity(employee);
           var employeeResource = employeeModelAssembler.toModel(entity);
            return ResponseEntity
                .created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(employeeResource);
        }
        catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
        summary = "Get customer details paged list",
        description = "REST API to get customer details with pagination"
    )
    @GetMapping(path = "/paged-employees", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedModel<EmployeeDTO>> findAllWithPagination(@ParameterObject Pageable pageable) {
        var employeeList = List.of(new EmployeeEntity(1L, "Frodo", "Baggins"),
            new EmployeeEntity(2L, "Bilbo", "Baggins"));
        var entities = new PageImpl(employeeList, pageable, employeeList.size());

        var pagedModel =
            employeeModelAssembler .toPagedModel(entities);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(
        summary = "Get customer details list",
        description = "REST API to get customer details"
    )
    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EmployeeDTO>> findAll() {
        var employeeList = List.of(new EmployeeEntity(1L, "Frodo", "Baggins"));
        return ResponseEntity.ok(employeeModelAssembler.toCollectionModel(employeeList));
    }

    @Operation(
        summary = "Get customer details",
        description = "REST API to get customer details"
    )
    @GetMapping(path = "/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDTO> findOne(@PathVariable long id) {
        var entity = new EmployeeEntity(id, "Frodo", "Baggins");
        var employeeResource =
            employeeModelAssembler.toModel(entity);
        return ResponseEntity.ok(employeeResource);
    }
}