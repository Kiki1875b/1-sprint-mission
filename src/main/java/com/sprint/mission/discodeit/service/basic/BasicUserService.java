package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User update(User user){
    return userRepository.save(user);
  }

  @Override
  public User findUserById(String id) {
    return userRepository.findById(UUID.fromString(id)).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public List<User> validateAndFindAllUsersIn(List<String> userIds) {
    List<UUID> userUuids = userIds.stream()
        .map(id -> {
          try {
            return UUID.fromString(id);
          } catch (IllegalArgumentException e) {
            // TODO : 예외를 던질지, 로그를 찍을지 고민
            log.warn("Invalid UUID: {}", id);
            return null;
          }
        }).filter(Objects::nonNull)
        .toList();

    // TODO : 상세 exception message 작성
    if (userUuids.isEmpty()) {
      throw new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE);
    }

    return userRepository.findAllByIdIn(userUuids);
  }

  @Override
  public void deleteUser(String id) {
    userRepository.deleteById(UUID.fromString(id));
  }
}
