package io.github.susimsek.springkafkasamples.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.github.susimsek.springkafkasamples.dto.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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

    @Operation(
        summary = "Create customer ",
        description = "REST API to create new customer"
    )
    @PostMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<Employee>> newEmployee(@RequestBody Employee employee)  {
        try {
            EntityModel<Employee> employeeResource = EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).findOne(employee.id())).withSelfRel());

            return ResponseEntity
                .created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(employeeResource);
        }
        catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
        summary = "Get customer details list",
        description = "REST API to get customer details"
    )
    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CollectionModel<EntityModel<Employee>>> findAll() {
        var employeeList = List.of(new Employee(1L, "Frodo", "Baggins"));

        var employees = employeeList
            .stream()
            .map(employee -> EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).findOne(employee.id())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees")))
            .toList();

        return ResponseEntity.ok(
            CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel()));
    }

    @Operation(
        summary = "Get customer details",
        description = "REST API to get customer details"
    )
    @GetMapping(path = "/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Employee>> findOne(@PathVariable long id) {
        var employee = new Employee(id, "Frodo", "Baggins");
        var employeeResource = EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).findOne(employee.id())).withSelfRel());
        return ResponseEntity.ok(employeeResource);
    }
}