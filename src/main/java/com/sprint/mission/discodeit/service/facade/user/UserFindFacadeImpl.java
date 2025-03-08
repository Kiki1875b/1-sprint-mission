package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFindFacadeImpl implements UserFindFacade {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserResponseDto findUserById(String id) {
    User user = userService.findUserById(id);
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public List<UserResponseDto> findAllUsers() {
    List<User> users = userService.findAllUsers();
    return users.stream().map(userMapper::toDto).toList();
  }
}
