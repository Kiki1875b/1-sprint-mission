package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMasterFacadeImpl implements UserMasterFacade {

  private final UserCreationFacade userCreationFacade;
  private final UserUpdateFacade userUpdateFacade;
  private final UserFindFacade userFindFacade;
  private final UserDeleteFacade userDeleteFacade;

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
    return userFindFacade.findUserById(id);
  }

  @Override
  public UserResponseDto updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto) {
    return userUpdateFacade.updateUser(userId, profile, updateDto);
  }

  @Override
  public List<UserResponseDto> findAllUsers() {
    return userFindFacade.findAllUsers();
  }

  @Override
  public void deleteUser(String id) {
    userDeleteFacade.delete(id);
  }
}
