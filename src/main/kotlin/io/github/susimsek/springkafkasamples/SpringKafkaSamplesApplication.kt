package io.github.susimsek.springkafkasamples

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKafkaSamplesApplication

fun main(args: Array<String>) {
    runApplication<SpringKafkaSamplesApplication>(*args)
}