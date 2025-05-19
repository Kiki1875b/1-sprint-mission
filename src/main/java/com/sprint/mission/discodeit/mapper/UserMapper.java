package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(uses = BinaryContentMapper.class, imports = {UUID.class, PasswordEncryptor.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "password", expression = "java(encoder.encode(dto.password()))")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "status", ignore = true)
    User toEntity(CreateUserRequest dto, @Context PasswordEncoder encoder);

    @Mapping(target = "username", source = "newUsername")
    @Mapping(target = "email", source = "newEmail")
    @Mapping(target = "password", expression = "java(encoder.encode(dto.newPassword()))")
    User toEntity(UserUpdateDto dto, @Context PasswordEncoder encoder);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "online", source = "status", qualifiedByName = "userStatusSetter")
    UserResponseDto toDto(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "profileId", source = "profile.id")
    CreateUserResponse toCreateUserResponse(User user);

    @Mapping(target = "id", source = "status.id")
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "lastActivityAt", source = "status.lastActiveAt")
    UserStatusResponseDto withStatus(User user);

    List<UserResponseDto> toDtoList(List<User> users);

    @Named("userStatusSetter")
    default boolean userStatusToBoolean(UserStatus status) {
        Instant now = Instant.now();
        long minutes = Duration.between(
                (status.getLastActiveAt() != null ? status.getLastActiveAt() : Instant.EPOCH), now)
            .toMinutes();
        if (minutes <= 10) {
            return true;
        }
        return false;
    }

}
