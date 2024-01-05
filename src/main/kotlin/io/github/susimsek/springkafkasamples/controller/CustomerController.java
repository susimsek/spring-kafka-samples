package io.github.susimsek.springkafkasamples.controller;

import io.github.susimsek.springkafkasamples.dto.Customer;
import io.github.susimsek.springkafkasamples.producer.CustomerEventProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "customer",
    description = "Customer REST APIs"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerEventProducer customerEventProducer;

    @Operation(
        summary = "Publish customer event",
        description = "REST API to publish customer event"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Ok"
    )
    @PostMapping("/publish")
    public ResponseEntity<Customer> publish(@RequestBody Customer customer) {
        customerEventProducer.publish(customer);
        return ResponseEntity.ok(customer);
    }
}