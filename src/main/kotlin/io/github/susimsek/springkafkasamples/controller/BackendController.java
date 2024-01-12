package io.github.susimsek.springkafkasamples.controller;

import io.github.susimsek.springkafkasamples.service.BackendService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "circuit-breaker",
    description = "Circuit Breaker demo REST APIs"
)
@RestController
@RequestMapping(value = "/api/v1/circuit-breaker")
@RequiredArgsConstructor
public class BackendController {

    private final BackendService backendService;

    @GetMapping("failure")
    public String failure(
        @Parameter(description = "failure switch enabled", example = "true")
        @RequestParam(defaultValue = "true") boolean failureSwitchEnabled,
        @Parameter(description = "slow call switch enabled", example = "false")
        @RequestParam(defaultValue = "false") boolean slowCallSwitchEnabled,
        @Parameter(description = "user unique identifier", example = "test1")
        @RequestParam(defaultValue = "test1") String companyId){
        return backendService.doSomething(
            failureSwitchEnabled, slowCallSwitchEnabled, companyId);
    }
}