package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.sprint.mission.discodeit.constant.UserConstant.PASSWORD_MIN_LENGTH;
import static com.sprint.mission.discodeit.constant.UserConstant.USERNAME_MAX_LENGTH;
import static com.sprint.mission.discodeit.constant.UserConstant.USERNAME_MIN_LENGTH;


public record CreateUserRequest(
    @NotBlank
    @Size(
        min = USERNAME_MIN_LENGTH,
        max = USERNAME_MAX_LENGTH
    )
    String username,
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH)
    String password,
    @NotBlank
    @Email
    String email
) {

}
