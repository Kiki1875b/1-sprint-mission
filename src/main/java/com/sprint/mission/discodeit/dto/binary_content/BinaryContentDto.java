package com.sprint.mission.discodeit.dto.binary_content;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
    @NotBlank
    UUID id,
    @NotBlank
    String fileName,
    @NotBlank
    Long size,
    @NotBlank
    String contentType,
    @NotBlank
    byte[] bytes
) {
}
