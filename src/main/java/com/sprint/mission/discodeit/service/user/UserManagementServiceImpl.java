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
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public User createUser(User user, MultipartFile profile) {
    setStatusToUser(user);
    if (profile != null && !profile.isEmpty()) {
      withProfile(user, profile);
    }
    return userService.saveUser(user);
  }

  @Override
  public User updateUser(String userId, User tmpUser, MultipartFile profile) {
    User userToUpdate = userService.findUserById(userId);
    userToUpdate.updateFields(tmpUser.getUsername(), tmpUser.getEmail(), tmpUser.getPassword());
    if (profile != null && !profile.isEmpty()) {
      withProfile(userToUpdate, profile);
    }
    return userService.saveUser(userToUpdate);
  }

  @Override
  public User findSingleUser(String userId) {
    return userService.findUserById(userId);
  }

  @Override
  public List<User> findAllUsers() {
    return userService.findAllUsers();
  }

  private void setStatusToUser(User user) {
    UserStatus status = UserStatus.create(user);
    user.updateStatus(status);
  }


  private void withProfile(User user, MultipartFile file) {
    try {
      BinaryContent profile = binaryContentMapper.toProfileBinaryContent(file);
      user.updateProfileImage(profile);
      binaryContentService.save(profile, file.getBytes());
    } catch (IOException e) {
      // TODO : 저장된 파일 삭제
      throw new CustomException(ErrorCode.FILE_ERROR);
    }
  }
}
