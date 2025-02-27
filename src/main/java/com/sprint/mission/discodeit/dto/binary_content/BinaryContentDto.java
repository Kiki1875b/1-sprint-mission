package com.sprint.mission.discodeit.dto.binary_content;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record BinaryContentDto(
    @NotBlank
    String id,
    @NotBlank
    Instant createdAt,
    @NotBlank
    String fileName,
    @NotBlank
    int size,
    @NotBlank
    String contentType,
    @NotBlank
    String bytes
) {
}
