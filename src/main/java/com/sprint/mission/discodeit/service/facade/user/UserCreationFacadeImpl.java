package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreationFacadeImpl implements UserCreationFacade {

  private final UserService userService;
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public UserResponseDto createUser(CreateUserRequest userDto, MultipartFile profile) {

    User user = userMapper.toEntity(userDto);

    BinaryContent profileBinary = null;

    if (profile != null && !profile.isEmpty()) {
      try {
        profileBinary = binaryContentMapper.toProfileBinaryContent(profile);
        binaryContentStorage.put(profileBinary.getId(), profile.getBytes());
        user.updateProfileImage(profileBinary);
      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_ERROR);
      }
    }


    user.updateStatus(UserStatus.create(user));
    userService.saveUser(user);
    return userMapper.toDto(user);
  }
}
