package io.github.susimsek.springkafkasamples.dto;

public record PostDTO(
    String userId,
    Long id,
    String title,
    String body) {

}