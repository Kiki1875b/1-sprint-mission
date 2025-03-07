package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateFacadeImpl implements UserUpdateFacade {

  private final UserService userService;
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  /**
   * {@link User} 의 BinaryContent profile 은 cascadeType.PERSIST & orphanRemoval = true
   * User 를 불러오고, 필드 업데이트 후, 업데이트할 파일이 있다면 업데이트 후 저장
   */
  @Override
  public UserResponseDto updateUser(String userId, MultipartFile newProfile, UserUpdateDto updateDto) {
    User user = userService.findUserById(userId);
    user.updateFields(updateDto.newUsername(), updateDto.newEmail(), PasswordEncryptor.hashPassword(updateDto.newPassword()));

    BinaryContent profile = null;
    if (newProfile != null && !newProfile.isEmpty()) {
      profile = binaryContentMapper.toProfileBinaryContent(newProfile);
      user.updateProfileImage(profile);
    }

    userService.saveUser(user);
    return userMapper.toDto(user);
  }
}
