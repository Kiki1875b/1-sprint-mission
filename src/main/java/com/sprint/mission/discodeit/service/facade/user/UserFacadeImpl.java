package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final UserManagementService userManagementService;
  private final UserMapper userMapper;

  @Override
  public UserResponseDto createUser(CreateUserRequest request, MultipartFile profile) {
    User user = userMapper.toEntity(request);
    User createdUser = userManagementService.createUser(user, profile);
    return userMapper.toDto(createdUser);
  }

  @Override
  public UserResponseDto findUserById(String id) {
    User user = userManagementService.findSingleUser(id);
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public UserResponseDto updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto) {
    User tmpUser = userMapper.toEntity(updateDto);
    User updatedUser =  userManagementService.updateUser(userId, tmpUser, profile);
    return userMapper.toDto(updatedUser);
  }

  @Override
  public List<UserResponseDto> findAllUsers() {
    List<User> users = userManagementService.findAllUsers();
    return userMapper.toDtoList(users);
  }

  @Override
  @Transactional
  public void deleteUser(String id) {
    userManagementService.deleteUser(id);
  }
}
