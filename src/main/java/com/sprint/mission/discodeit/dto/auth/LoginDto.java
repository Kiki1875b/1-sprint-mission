package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
    @NotBlank
    String username,
    @NotBlank
    String password
) {
}
