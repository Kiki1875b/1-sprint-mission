package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public User createUser(User user, MultipartFile profile) {

    User createdUser;

    if (profile == null || profile.isEmpty()) {
      createdUser = withoutProfile(user);
    } else {
      createdUser = withProfile(user, profile);
    }

    return createdUser;
  }

  @Override
  public User updateUser(String userId, User user, MultipartFile profile) {
    return null;
  }

  private User withoutProfile(User user) {
    UserStatus status = UserStatus.create(user);
    user.updateStatus(status);
    return userService.saveUser(user);
  }

  private User withProfile(User user, MultipartFile file) {
    UserStatus status = UserStatus.create(user);
    user.updateStatus(status);

    try {
      BinaryContent profile = binaryContentMapper.toProfileBinaryContent(file);
      user.updateProfileImage(profile);
      binaryContentService.save(profile, file.getBytes());
    } catch (IOException e) {
      // TODO : 저장된 파일 삭제
      throw new CustomException(ErrorCode.FILE_ERROR);
    }

    return userService.saveUser(user);
  }
}
