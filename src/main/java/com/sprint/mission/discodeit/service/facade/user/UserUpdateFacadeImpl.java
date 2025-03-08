package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.user.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateFacadeImpl implements UserUpdateFacade {

  private final UserService userService;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  /**
   * {@link User} 의 BinaryContent profile 은 cascadeType.PERSIST & orphanRemoval = true
   * User 를 불러오고, 필드 업데이트 후, 업데이트할 파일이 있다면 업데이트 후 저장
   */
  @Override
  @Transactional
  public UserResponseDto updateUser(String userId, MultipartFile newProfile, UserUpdateDto updateDto) {
    User user = userService.findUserById(userId);
    user.updateFields(updateDto.newUsername(), updateDto.newEmail(), PasswordEncryptor.hashPassword(updateDto.newPassword()));

    BinaryContent profile = null;
    if (newProfile != null && !newProfile.isEmpty()) {
      try {
        profile = binaryContentMapper.toProfileBinaryContent(newProfile);
        binaryContentStorage.put(profile.getId(), newProfile.getBytes());
        user.updateProfileImage(profile);
      }catch (IOException e){
        throw new CustomException(ErrorCode.FILE_ERROR);
      }
    }

    userService.saveUser(user);
    return userMapper.toDto(user);
  }
}
